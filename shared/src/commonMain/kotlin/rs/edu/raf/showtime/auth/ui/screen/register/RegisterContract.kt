package rs.edu.raf.showtime.auth.ui.screen.register

import rs.edu.raf.showtime.auth.util.validateFullName
import rs.edu.raf.showtime.auth.util.validatePassword
import rs.edu.raf.showtime.auth.util.validateUsername

object RegisterContract {
    sealed class ScreenState {
        data object Idle: ScreenState()
        data object Loading: ScreenState()
        data object Success: ScreenState()
        data class Error(val error: String): ScreenState()
    }

    data class State(
        val fullName: String = "",
        val username: String = "",
        val password: String = "",
        val hasSubmitted: Boolean = false,
        val screenState: ScreenState = ScreenState.Idle
    ) {
        val isLoading: Boolean
            get() = screenState is ScreenState.Loading

        val fullNameError: String?
            get() {
                if (!hasSubmitted && fullName.isBlank()) return null

                return validateFullName(fullName)
            }

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
            get() = fullNameError == null &&
                usernameError == null &&
                passwordError == null &&
                fullName.isNotBlank() &&
                username.isNotBlank() &&
                password.isNotBlank() &&
                !isLoading
    }

    sealed class Event {
        data class FullNameChanged(val value: String): Event()
        data class UsernameChanged(val value: String): Event()
        data class PasswordChanged(val value: String): Event()
        data object RegisterClicked: Event()
    }
}
