package rs.edu.raf.showtime.profile.ui.screen

import rs.edu.raf.showtime.network.model.profile.UserDto

object ProfileContract {
    sealed class ScreenState {
        data object Loading : ScreenState()
        data class Success(val profile: UserDto) : ScreenState()
        data class Error(val message: String) : ScreenState()
    }

    data class State(val screenState: ScreenState = ScreenState.Loading)
}