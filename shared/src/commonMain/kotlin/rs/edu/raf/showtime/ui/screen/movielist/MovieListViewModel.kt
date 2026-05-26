package rs.edu.raf.showtime.ui.screen.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.showtime.data.repository.MovieRepository
import rs.edu.raf.showtime.ui.screen.movielist.MovieListContract.State
import rs.edu.raf.showtime.ui.screen.movielist.MovieListContract.Event
import rs.edu.raf.showtime.ui.screen.movielist.MovieListContract.Effect
import rs.edu.raf.showtime.ui.state.FilterManager

class MovieListViewModel(
    private val repository: MovieRepository,
    private val filterManager: FilterManager,
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            filterManager.activeFilters.collect { filters ->
                _state.update {
                    it.copy(activeFilters = filters, activeFilterCount = filters.activeCount())
                }

                loadMovies()
            }
        }
    }

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(Event.LoadMovies)
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.LoadMovies -> loadMovies()
            is Event.RetryClicked -> loadMovies()
            is Event.SortChanged -> {
                _state.update { it.copy(sortBy = event.option) }
                loadMovies()
            }
            is Event.FiltersApplied -> {
                _state.update {
                    it.copy(
                        activeFilters = event.filters,
                        activeFilterCount = event.filters.activeCount()
                    )
                }
                loadMovies()
            }
            is Event.MovieClicked -> {
                viewModelScope.launch {
                    _effect.send(Effect.NavigateToDetails(event.movieId))
                }
            }
            is Event.FilterButtonClicked -> {
                viewModelScope.launch {
                    _effect.send(Effect.NavigateToFilter)
                }
            }
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _state.update { it.copy(screenState = MovieListContract.ScreenState.Loading) }
            try {
                val filters = _state.value.activeFilters
                val result = repository.getMovies(
                    sortBy = _state.value.sortBy.apiValue,
                    sortOrder = _state.value.sortBy.order.value,
                    genreId = filters.genreId,
                    query = filters.query,
                    minYear = filters.minYear,
                    maxYear = filters.maxYear,
                    minRating = filters.minRating
                )
                _state.update {
                    it.copy(
                        screenState = if (result.items.isEmpty())
                            MovieListContract.ScreenState.Empty
                        else
                            MovieListContract.ScreenState.Success(result.items, result.totalItems)
                    )
                }
            } catch (e: Exception) {
                println("MovieRepo Error: ${e.message}")
                _state.update {
                    it.copy(screenState = MovieListContract.ScreenState.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }
}