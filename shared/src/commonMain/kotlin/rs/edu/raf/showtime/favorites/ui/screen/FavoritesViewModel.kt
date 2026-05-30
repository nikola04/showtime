package rs.edu.raf.showtime.favorites.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.showtime.favorites.data.FavoritesRepository

class FavoritesViewModel(
    val repository: FavoritesRepository
)
    : ViewModel() {
    private val _state = MutableStateFlow(FavoritesContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<FavoritesContract.Effect>()
    val effect = _effect.receiveAsFlow()

    private var observeFavoritesJob: Job? = null
    private var syncFavoritesJob: Job? = null

    init {
        startObservingFavorites()
        syncFavorites()
    }

    fun onEvent(event: FavoritesContract.Event) {
        when(event) {
            is FavoritesContract.Event.RetryClicked -> syncFavorites()
            is FavoritesContract.Event.ToggleFavorite -> toggleFavorites(event.movieId)
        }
    }

    private fun toggleFavorites(movieId: String) {
        viewModelScope.launch {
            try {
                repository.toggleMovie(movieId)
            } catch (e: Exception) {
                Napier.e("Failed to toggle favorites", e)
                _effect.send(FavoritesContract.Effect.ShowError("Failed to toggle favorite for movie"))
            }
        }
    }

    private fun startObservingFavorites() {
        observeFavoritesJob?.cancel()
        observeFavoritesJob = viewModelScope.launch {
            repository.observeFavoriteMovies().collect {
                movies -> _state.update {
                    it.copy(
                        screenState = when {
                            movies.isEmpty() -> FavoritesContract.ScreenState.Empty
                            else -> FavoritesContract.ScreenState.Success(movies)
                        }
                    )
                }
            }
        }
    }

    private fun syncFavorites() {
        syncFavoritesJob?.cancel()
        syncFavoritesJob = viewModelScope.launch {
            val hasCachedData = repository.getFavoriteMoviesCount() > 0

            try {
                if (!hasCachedData)
                    _state.update { it.copy(screenState = FavoritesContract.ScreenState.Loading) }
                repository.refreshFavorite()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Napier.d("Failed to sync favorites", e)
                _effect.send(FavoritesContract.Effect.ShowError("Failed to sync favorites"))

                if (!hasCachedData)
                    _state.update {
                        it.copy(screenState = FavoritesContract.ScreenState.Error("Network request failed"))
                    }
            }
        }
    }
}