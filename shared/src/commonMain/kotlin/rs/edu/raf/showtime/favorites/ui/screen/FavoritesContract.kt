package rs.edu.raf.showtime.favorites.ui.screen

import rs.edu.raf.showtime.movies.domain.Movie

object FavoritesContract {
    sealed class ScreenState {
        data object Loading : ScreenState()
        data class Success(val movies: List<Movie>) : ScreenState()
        data class Error(val message: String) : ScreenState()
        data object Empty : ScreenState()
    }

    data class State(val screenState: ScreenState = ScreenState.Loading)

    sealed class Event {
        data object RetryClicked : Event()
        data class ToggleFavorite(val movieId: String) : Event()
    }

    sealed class Effect {
        data class ShowError(val message: String) : Effect()
    }
}