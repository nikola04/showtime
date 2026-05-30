package rs.edu.raf.showtime.movies.ui.screen.moviedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.showtime.movies.data.MovieRepository
import rs.edu.raf.showtime.movies.domain.MovieDetails
import rs.edu.raf.showtime.watchlist.data.WatchlistRepository

class MovieDetailsViewModel(
    private val movieRepository: MovieRepository,
    private val watchlistRepository: WatchlistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailsContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<MovieDetailsContract.Effect>()
    val effect = _effect.receiveAsFlow()

    private var observeJob: Job? = null
    private var syncJob: Job? = null

    private val movieId: String? = savedStateHandle["id"]

    init {
        movieId?.let {
            observeMovie(it)
            syncMovie(it)
        } ?: run {
            _state.update {
                it.copy(screenState = MovieDetailsContract.ScreenState.Error("Invalid movie ID"))
            }
        }
    }

    fun onEvent(event: MovieDetailsContract.Event) {
        when (event) {
            is MovieDetailsContract.Event.RetryClicked -> {
                movieId?.let(::syncMovie)
            }

            is MovieDetailsContract.Event.ToggleWatchlist -> {
                movieId?.let(::toggleWatchlist)
            }

            is MovieDetailsContract.Event.ToggleFavorite -> {
                movieId?.let(::toggleFavorites)
            }

            is MovieDetailsContract.Event.BackClicked -> {
                viewModelScope.launch {
                    _effect.send(MovieDetailsContract.Effect.NavigateBack)
                }
            }

            is MovieDetailsContract.Event.OpenYoutube -> {
                viewModelScope.launch {
                    _effect.send(MovieDetailsContract.Effect.OpenYoutube(event.id))
                }
            }
        }
    }

    private fun toggleWatchlist(movieId: String) {
        viewModelScope.launch {
            try {
                watchlistRepository.toggleMovie(movieId)
            } catch (e: Exception) {
                Napier.e("Failed to toggle watchlist", e)
                _effect.send(MovieDetailsContract.Effect.ShowError("Failed to toggle watchlist for movie"))
            }
        }
    }

    private fun toggleFavorites(movieId: String) {
        viewModelScope.launch {
        }
    }

    private fun observeMovie(movieId: String) {
        observeJob?.cancel()

        observeJob = viewModelScope.launch {
            launch {
                watchlistRepository.observeWatchlistMovieState(movieId).collectLatest { isInWatchlist ->
                    _state.update { state ->
                        val screenState = state.screenState
                        if (screenState is MovieDetailsContract.ScreenState.Success) {
                            state.copy(
                                screenState = screenState.copy(isInWatchlist = isInWatchlist)
                            )
                        } else {
                            state
                        }
                    }
                }
            }

            movieRepository.observeMovieDetails(movieId)
                .collectLatest { movie ->
                    if (movie == null) {
                        _state.update {
                            it.copy(screenState = MovieDetailsContract.ScreenState.Loading)
                        }
                        return@collectLatest
                    }
                    loadExtras(movie)
                }
        }
    }

    private suspend fun loadExtras(movie: MovieDetails) {
        val castDeferred = viewModelScope.async {
            runCatching {
                movieRepository.getCast(movie.imdbId).items
            }.getOrDefault(emptyList())
        }

        val imagesDeferred = viewModelScope.async {
            runCatching {
                movieRepository.getImages(movie.imdbId)
                    .backdrops
                    .take(5)
            }.getOrDefault(emptyList())
        }

        val trailerDeferred = viewModelScope.async {
            runCatching {
                movieRepository.getVideos(movie.imdbId, "Trailer")
                    .firstOrNull {
                        it.site == "YouTube" &&
                                it.type == "Trailer"
                    }
            }.getOrNull()
        }

        val isInWatchlist = watchlistRepository.isMovieInWatchlist(movie.imdbId)

        _state.update {
            it.copy(
                screenState = MovieDetailsContract.ScreenState.Success(
                    movie = movie,
                    cast = castDeferred.await(),
                    images = imagesDeferred.await(),
                    trailer = trailerDeferred.await(),
                    isInWatchlist = isInWatchlist,
                    isFavorite = false
                )
            )
        }
    }

    private fun syncMovie(movieId: String) {
        syncJob?.cancel()

        syncJob = viewModelScope.launch {
            try {
                movieRepository.refreshMovieDetails(movieId)
            } catch (e: Exception) {
                if (e is CancellationException) throw e

                _effect.send(MovieDetailsContract.Effect.ShowError("Failed to sync movie"))

                val hasCachedMovie = movieRepository.hasMovieDetails(movieId)
                if (!hasCachedMovie) {
                    _state.update {
                        it.copy(screenState = MovieDetailsContract.ScreenState.Error("Network request failed"))
                    }
                }
            }
        }
    }
}