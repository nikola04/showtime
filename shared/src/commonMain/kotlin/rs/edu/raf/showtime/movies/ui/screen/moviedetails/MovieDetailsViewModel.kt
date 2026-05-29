package rs.edu.raf.showtime.movies.ui.screen.moviedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class MovieDetailsViewModel(
    private val repository: MovieRepository,
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

            else -> Unit
        }
    }

    private fun observeMovie(movieId: String) {
        observeJob?.cancel()

        observeJob = viewModelScope.launch {
            repository.observeMovieDetails(movieId)
                .collectLatest { movie ->

                    if (movie == null) {
                        _state.update {
                            it.copy(
                                screenState = MovieDetailsContract.ScreenState.Loading
                            )
                        }
                        return@collectLatest
                    }

                    loadExtras(movie)
                }
        }
    }

    private suspend fun loadExtras(movie: MovieDetails) {

        val cast = viewModelScope.async {
            runCatching {
                repository.getCast(movie.imdbId).items
            }.getOrDefault(emptyList())
        }

        val images = viewModelScope.async {
            runCatching {
                repository.getImages(movie.imdbId)
                    .backdrops
                    .take(5)
            }.getOrDefault(emptyList())
        }

        val trailer = viewModelScope.async {
            runCatching {
                repository.getVideos(movie.imdbId, "Trailer")
                    .firstOrNull {
                        it.site == "YouTube" &&
                                it.type == "Trailer"
                    }
            }.getOrNull()
        }

        _state.update {
            it.copy(
                screenState = MovieDetailsContract.ScreenState.Success(
                    movie = movie,
                    cast = cast.await(),
                    images = images.await(),
                    trailer = trailer.await()
                )
            )
        }
    }

    private fun syncMovie(movieId: String) {
        syncJob?.cancel()

        syncJob = viewModelScope.launch {
            try {
                repository.refreshMovieDetails(movieId)
            } catch (e: Exception) {
                if (e is CancellationException) throw e

                _effect.send(MovieDetailsContract.Effect.ShowError("Failed to sync movie"))

                val hasCachedMovie = repository.hasMovieDetails(movieId)
                if (!hasCachedMovie) {
                    _state.update {
                        it.copy(screenState = MovieDetailsContract.ScreenState.Error("Failed to load movie"))
                    }
                }
            }
        }
    }
}