package rs.edu.raf.showtime.auth.ui.screen.login

import rs.edu.raf.showtime.auth.util.validatePassword
import rs.edu.raf.showtime.auth.util.validateUsername

object LoginContract {
    sealed class ScreenState {
        data object Idle : ScreenState()
        data object Loading: ScreenState()
        data object Success: ScreenState()
        data class Error(val message: String): ScreenState()
    }

    data class State(
        val username: String = "",
        val password: String = "",
        val hasSubmitted: Boolean = false,
        val screenState: ScreenState = ScreenState.Idle
    ) {
        val isLoading: Boolean
            get() = screenState is ScreenState.Loading

        val usernameError: String?
            get() {
                if (!hasSubmitted && username.isBlank()) return null

                return validateUsername(username)
            }

        val passwordError: String?
            get() {
                if (!hasSubmitted && password.isBlank()) return null

                return validatePassword(password)
            }

        val canSubmit: Boolean
            get() = usernameError == null &&
                passwordError == null &&
                username.isNotBlank() &&
                password.isNotBlank() &&
                !isLoading
    }

    sealed class Event {
        data class UsernameChanged(val value: String): Event()
        data class PasswordChanged(val value: String): Event()
        data object LoginClicked : Event()
    }
}
