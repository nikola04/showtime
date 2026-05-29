package rs.edu.raf.showtime.watchlist.ui.screen

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
import rs.edu.raf.showtime.watchlist.data.WatchlistRepository

class WatchlistViewModel(
    val repository: WatchlistRepository
)
    : ViewModel() {
    private val _state = MutableStateFlow(WatchlistContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<WatchlistContract.Effect>()
    val effect = _effect.receiveAsFlow()

    private var observeWatchlistJob: Job? = null
    private var syncWatchlistJob: Job? = null

    init {
        startObservingWatchlist()
        syncWatchlist()
    }

    fun onEvent(event: WatchlistContract.Event) {
        when(event) {
            is WatchlistContract.Event.RetryClicked -> syncWatchlist()
            is WatchlistContract.Event.ToggleWatchlist -> toggleWatchlist(event.movieId)
        }
    }

    private fun toggleWatchlist(movieId: String) {
        viewModelScope.launch {
            try {
                repository.toggleMovie(movieId)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _effect.send(WatchlistContract.Effect.ShowError("Failed to update watchlist"))
            }
        }
    }

    private fun startObservingWatchlist() {
        observeWatchlistJob?.cancel()
        observeWatchlistJob = viewModelScope.launch {
            repository.observeWatchlistMovies().collect {
                movies -> _state.update {
                    it.copy(
                        screenState = when {
                            movies.isEmpty() -> WatchlistContract.ScreenState.Empty
                            else -> WatchlistContract.ScreenState.Success(movies)
                        }
                    )
                }
            }
        }
    }

    private fun syncWatchlist() {
        syncWatchlistJob?.cancel()
        syncWatchlistJob = viewModelScope.launch {
            val hasCachedData = repository.getWatchlistMoviesCount() > 0

            try {
                if (!hasCachedData)
                    _state.update { it.copy(screenState = WatchlistContract.ScreenState.Loading) }
                repository.refreshWatchlist()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Napier.d("Failed to sync watchlist")
                val errorMessage = e::class.simpleName ?: "Sync failed"
                _effect.send(WatchlistContract.Effect.ShowError(errorMessage))

                if (!hasCachedData)
                    _state.update {
                        it.copy(screenState = WatchlistContract.ScreenState.Error("Network request failed"))
                    }
            }
        }
    }
}