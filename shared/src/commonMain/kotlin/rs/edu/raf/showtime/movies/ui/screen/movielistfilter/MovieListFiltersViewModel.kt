package rs.edu.raf.showtime.movies.ui.screen.movielistfilter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import io.github.aakira.napier.Napier
import rs.edu.raf.showtime.movies.data.MovieRepository
import rs.edu.raf.showtime.movies.ui.state.FilterManager
import rs.edu.raf.showtime.movies.ui.screen.movielistfilter.MovieListFiltersContract.State
import rs.edu.raf.showtime.movies.ui.screen.movielistfilter.MovieListFiltersContract.Event
import rs.edu.raf.showtime.movies.ui.screen.movielistfilter.MovieListFiltersContract.Effect
import rs.edu.raf.showtime.movies.ui.state.FilterParams

class MovieListFiltersViewModel(
    private var repository: MovieRepository,
    private var filterManager: FilterManager
): ViewModel() {
    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    init {
        _state.update { it.copy(activeFilters = filterManager.activeFilters.value) }
        loadGenres()
    }

    fun onEvent(event: Event) {
        when(event) {
            is Event.BackClicked -> {
                viewModelScope.launch {
                    _effect.send(Effect.NavigateBack)
                }
            }
            is Event.ClearFiltersClicked -> {
                filterManager.clear()
                _state.update { it.copy(activeFilters = FilterParams()) }
            }
            is Event.RetryClicked -> {
                loadGenres()
            }
            is Event.QueryChanged -> {
                _state.update { it.copy(activeFilters = state.value.activeFilters.copy(query = event.query)) }
            }
            is Event.GenreChanged -> {
                _state.update { it.copy(activeFilters = state.value.activeFilters.copy(genreId = event.genre)) }
            }
            is Event.YearRangeChanged -> {
                _state.update { it.copy(activeFilters = state.value.activeFilters.copy(minYear = event.minYear, maxYear = event.maxYear)) }
            }
            is Event.MinRatingChanged -> {
                _state.update { it.copy(activeFilters = state.value.activeFilters.copy(minRating = event.rating)) }
            }
            is Event.ApplyClicked -> {
                viewModelScope.launch {
                    filterManager.update(state.value.activeFilters.copy())
                    _effect.send(Effect.ApplyFilters)
                }
            }
        }
    }

    private fun loadGenres() {
        viewModelScope.launch {
            try {
                val genres = repository.getGenres()
                _state.update { it.copy(screenState = MovieListFiltersContract.ScreenState.Success(genres)) }
            } catch (e: Exception) {
                Napier.e("Failed to load genres", e)
                _state.update { it.copy(screenState =  MovieListFiltersContract.ScreenState.Error(e.message ?: "failed to fetch genres")) }
            }
        }
    }
}
