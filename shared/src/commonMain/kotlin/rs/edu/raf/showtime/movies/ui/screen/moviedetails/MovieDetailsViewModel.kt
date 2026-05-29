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
import rs.edu.raf.showtime.movies.domain.CastMember
import rs.edu.raf.showtime.movies.domain.MovieDetails
import rs.edu.raf.showtime.movies.domain.MovieImage
import rs.edu.raf.showtime.movies.domain.MovieVideo

class MovieDetailsViewModel(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(MovieDetailsContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<MovieDetailsContract.Effect>()
    val effect = _effect.receiveAsFlow()

    private var currentMovieId: String? = null
    private var observeDetailsJob: Job? = null
    private var syncDetailsJob: Job? = null

    init {
        val movieId: String? = savedStateHandle["id"]
        if (movieId != null) {
            currentMovieId = movieId
            startObservingDetails(movieId)
            syncDetails(movieId)
        } else {
            _state.update { it.copy(screenState = MovieDetailsContract.ScreenState.Error("Invalid movie ID")) }
        }
    }

    fun onEvent(event: MovieDetailsContract.Event) {
        when(event) {
            is MovieDetailsContract.Event.LoadMovie -> {
                currentMovieId = event.movieId
                startObservingDetails(event.movieId)
                syncDetails(event.movieId)
            }
            is MovieDetailsContract.Event.RetryClicked -> {
                currentMovieId?.let { 
                    startObservingDetails(it)
                    syncDetails(it)
                }
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

    private fun startObservingDetails(movieId: String) {
        observeDetailsJob?.cancel()
        observeDetailsJob = viewModelScope.launch {
            repository.observeMovieDetails(movieId).collectLatest { movieDetails ->
                if (movieDetails != null) {
                    fetchExtraData(movieDetails)
                } else {
                    _state.update { it.copy(screenState = MovieDetailsContract.ScreenState.Loading) }
                }
            }
        }
    }

    private suspend fun fetchExtraData(movie: MovieDetails) {
        val castDeferred = viewModelScope.async {
            try {
                repository.getCast(movie.imdbId).items
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                emptyList<CastMember>()
            }
        }
        val imagesDeferred = viewModelScope.async {
            try {
                repository.getImages(movie.imdbId).backdrops.take(5)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                emptyList<MovieImage>()
            }
        }
        val videosDeferred = viewModelScope.async {
            try {
                repository.getVideos(movie.imdbId, type = "Trailer")
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                emptyList<MovieVideo>()
            }
        }

        val cast = castDeferred.await()
        val images = imagesDeferred.await()
        val videos = videosDeferred.await()
        val trailer = videos.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }

        _state.update { 
            it.copy(screenState = MovieDetailsContract.ScreenState.Success(movie, cast, images, trailer)) 
        }
    }

    private fun syncDetails(movieId: String) {
        syncDetailsJob?.cancel()
        syncDetailsJob = viewModelScope.launch {
            try {
                repository.refreshMovieDetails(movieId)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                
                val errorMessage = e::class.simpleName ?: "Sync failed"
                _effect.send(MovieDetailsContract.Effect.ShowError(errorMessage))

                if (_state.value.screenState is MovieDetailsContract.ScreenState.Loading) {
                    _state.update { 
                        it.copy(screenState = MovieDetailsContract.ScreenState.Error(e.message ?: "Unknown error")) 
                    }
                }
            }
        }
    }
}
