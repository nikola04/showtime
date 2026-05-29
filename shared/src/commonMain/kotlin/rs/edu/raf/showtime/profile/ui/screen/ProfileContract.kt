package rs.edu.raf.showtime.profile.ui.screen

import rs.edu.raf.showtime.profile.domain.User

object ProfileContract {
    sealed class ScreenState {
        data object Loading : ScreenState()
        data class Success(val profile: User) : ScreenState()
        data class Error(val message: String) : ScreenState()
    }

    data class State(
        val screenState: ScreenState = ScreenState.Loading,
        val favoriteCount: Int = 0,
        val watchlistCount: Int = 0,
        val quizGamesPlayed: Int = 0,
        val bestQuizScore: Float = 0f
    )

    sealed class Event {
        data object Logout : Event()
        data object RetryClicked : Event()
    }
}
