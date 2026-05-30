package rs.edu.raf.showtime.movies.ui.screen.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import io.github.aakira.napier.Napier
import rs.edu.raf.showtime.movies.data.MovieRepository
import rs.edu.raf.showtime.movies.ui.screen.movielist.MovieListContract.State
import rs.edu.raf.showtime.movies.ui.screen.movielist.MovieListContract.Event
import rs.edu.raf.showtime.movies.ui.screen.movielist.MovieListContract.Effect
import rs.edu.raf.showtime.movies.ui.screen.movielist.MovieListContract.Effect.*
import rs.edu.raf.showtime.movies.ui.state.FilterManager

class MovieListViewModel(
    private val repository: MovieRepository,
    private val filterManager: FilterManager,
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private var observeMoviesJob: Job? = null
    private var syncMoviesJob: Job? = null

    init {
        viewModelScope.launch {
            filterManager.activeFilters.collectLatest { filters ->
                _state.update {
                    it.copy(activeFilters = filters, activeFilterCount = filters.activeCount())
                }

                startObservingMovies()
            }
        }
        syncMovies()
    }

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.RetryClicked -> syncMovies()
            is Event.SortChanged -> {
                _state.update { it.copy(sortBy = event.option) }
                startObservingMovies()
            }
            is Event.MovieClicked -> {
                viewModelScope.launch {
                    _effect.send(NavigateToDetails(event.movieId))
                }
            }
            is Event.FilterButtonClicked -> {
                viewModelScope.launch {
                    _effect.send(NavigateToFilter)
                }
            }

        }
    }

    private fun startObservingMovies() {
        observeMoviesJob?.cancel()
        observeMoviesJob = viewModelScope.launch {
            val filters = _state.value.activeFilters
            repository.observeMovies(
                sortBy = _state.value.sortBy.apiValue,
                sortOrder = _state.value.sortBy.order.value,
                genreId = filters.genreId,
                query = filters.query,
                minYear = filters.minYear,
                maxYear = filters.maxYear,
                minRating = filters.minRating
            ).collect { movies ->
                _state.update {
                    it.copy(
                        screenState = when {
                            movies.isEmpty() -> MovieListContract.ScreenState.Empty
                            else -> MovieListContract.ScreenState.Success(movies, movies.size)
                        }
                    )
                }
            }
        }
    }

    private fun syncMovies() {
        syncMoviesJob?.cancel()
        syncMoviesJob = viewModelScope.launch {
            val hasCachedData = repository.getMoviesCount() > 0

            try {
                if (!hasCachedData)
                    _state.update {
                        it.copy(screenState = MovieListContract.ScreenState.Loading)
                    }

                Napier.d("Starting movie sync...")
                repository.refreshMovies()
                Napier.d("Movie sync completed successfully")
            } catch (e: CancellationException) {
                Napier.d("Movie sync cancelled")
                throw e
            } catch (e: Exception) {
                Napier.e("Failed to sync movies", e)
                
                _effect.send(ShowError("Failed to sync movies"))

                if (!hasCachedData)
                    _state.update {
                        it.copy(screenState = MovieListContract.ScreenState.Error(e.message ?: "Unknown error"))
                    }
            }
        }
    }
}
