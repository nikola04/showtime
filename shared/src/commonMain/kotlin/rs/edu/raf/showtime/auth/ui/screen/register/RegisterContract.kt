package rs.edu.raf.showtime.auth.ui.screen.register

object RegisterContract {
    sealed class ScreenState {
        data object Loading: ScreenState()
        data object Success: ScreenState()
        data class Error(val error: String): ScreenState()
    }

    sealed class State(val screenState: ScreenState.Loading)
}