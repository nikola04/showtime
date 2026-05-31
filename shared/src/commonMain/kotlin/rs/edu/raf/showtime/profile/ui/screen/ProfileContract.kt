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
        val favoriteCount: Long = 0,
        val watchlistCount: Long = 0,
        val quizGamesPlayed: Long = 0,
        val bestQuizScore: Double? = null,
    )

    sealed class Event {
        data object Logout : Event()
        data object RetryClicked : Event()
    }
}
