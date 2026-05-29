package rs.edu.raf.showtime.movies.ui.screen.movielist

import rs.edu.raf.showtime.movies.domain.Movie
import rs.edu.raf.showtime.movies.ui.state.FilterParams

object MovieListContract {
    sealed class ScreenState {
        data object Loading : ScreenState()
        data class Success(val movies: List<Movie>, val total: Int) : ScreenState()
        data class Error(val message: String) : ScreenState()
        data object Empty : ScreenState()
    }

    data class State(
        val screenState: ScreenState = ScreenState.Loading,
        val sortBy: SortOption = SortOption.RATING,
        val activeFilters: FilterParams = FilterParams(),
        val activeFilterCount: Int = 0,
    )

    sealed class Event {
        data class SortChanged(val option: SortOption) : Event()
        data class FiltersApplied(val filters: FilterParams) : Event()
        data class MovieClicked(val movieId: String) : Event()
        data object FilterButtonClicked : Event()
        data object RetryClicked : Event()
    }

    sealed class Effect {
        data class NavigateToDetails(val movieId: String) : Effect()
        data object NavigateToFilter : Effect()
        data class ShowError(val message: String) : Effect()
    }

    enum class SortOption(val apiValue: String, val label: String, var order: SortOrder) {
        RATING("imdb_rating", "Rating", SortOrder.DESC),
        YEAR("year", "Year", SortOrder.DESC),
        TITLE("title", "Title", SortOrder.ASC),
        POPULARITY("popularity", "Popularity", SortOrder.DESC)
    }

    enum class SortOrder(val value: String) {
        DESC("desc"),
        ASC("ASC")
    }
}