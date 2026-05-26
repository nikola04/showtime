package rs.edu.raf.showtime.ui.screen.moviedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.showtime.data.repository.MovieRepository

class MovieDetailsViewModel(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(MovieDetailsContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<MovieDetailsContract.Effect>()
    val effect = _effect.receiveAsFlow()

    private var currentMovieId: String? = null

    init {
        val movieId: String? = savedStateHandle["id"]
        if (movieId != null) {
            loadMovieDetails(movieId)
        } else {
            _state.update { it.copy(screenState = MovieDetailsContract.ScreenState.Error("Invalid move ID")) }
        }
    }

    fun onEvent(event: MovieDetailsContract.Event) {
        when(event) {
            is MovieDetailsContract.Event.LoadMovie -> {
                currentMovieId = event.movieId
            }
            is MovieDetailsContract.Event.RetryClicked -> {
                currentMovieId?.let { loadMovieDetails(it) }
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

    private fun loadMovieDetails(movieId: String) {
        viewModelScope.launch {
            _state.update { it.copy(screenState = MovieDetailsContract.ScreenState.Loading) }
            try {
                val movieDeferred = async { repository.getMovieDetails(movieId) }
                val castDeferred = async {
                    try { repository.getCast(movieId).items } catch (e: Exception) { emptyList() }
                }
                val imagesDeferred = async {
                    try { repository.getImages(movieId).backdrops.take(5) } catch (e: Exception) { emptyList() }
                }
                val videosDeferred = async {
                    try { repository.getVideos(movieId, type = "Trailer") } catch (e: Exception) { emptyList() }
                }

                val movie = movieDeferred.await()
                val cast = castDeferred.await()
                val images = imagesDeferred.await()
                val videos = videosDeferred.await()

                val trailer = videos.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }

                _state.update { it.copy(screenState = MovieDetailsContract.ScreenState.Success(movie, cast, images, trailer)) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(screenState = MovieDetailsContract.ScreenState.Error(e.message ?: "unknown error"))
                }
            }
        }
    }
}