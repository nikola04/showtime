package rs.edu.raf.showtime.ui.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FilterParams(
    val query: String? = null,
    val genreId: Int? = null,
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val minRating: Float? = null
) {
    fun activeCount(): Int = listOfNotNull(genreId, minYear, maxYear, minRating).size + if (query.isNullOrBlank()) 0 else 1
}

class FilterManager {
    private val _activeFilters = MutableStateFlow(FilterParams())
    val activeFilters: StateFlow<FilterParams> = _activeFilters.asStateFlow()

    fun update(newFilters: FilterParams) {
        _activeFilters.value = newFilters
    }

    fun clear() {
        _activeFilters.value = FilterParams()
    }
}