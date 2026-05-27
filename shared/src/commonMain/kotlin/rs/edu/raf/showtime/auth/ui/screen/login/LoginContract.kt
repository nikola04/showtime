package rs.edu.raf.showtime.auth.ui.screen.login

object LoginContract {
    sealed class ScreenState {
        data object Loading: ScreenState()
        data object Success: ScreenState()
        data class Error(val message: String): ScreenState()
    }

    sealed class State(val screenState: ScreenState.Loading)
}