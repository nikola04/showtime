package rs.edu.raf.showtime.auth.ui.screen.register

object RegisterContract {
    private val usernameRegex = Regex("^[A-Za-z0-9_]+$")

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

                return when {
                    fullName.isBlank() -> "Full name is required"
                    else -> null
                }
            }

        val usernameError: String?
            get() {
                if (!hasSubmitted && username.isBlank()) return null

                return when {
                    username.isBlank() -> "Username is required"
                    username.length < 3 -> "Username must be at least 3 characters"
                    !usernameRegex.matches(username) -> "Use only letters, digits, and underscores"
                    else -> null
                }
            }

        val passwordError: String?
            get() {
                if (!hasSubmitted && password.isBlank()) return null

                return when {
                    password.isBlank() -> "Password is required"
                    password.length < 8 -> "Password must be at least 8 characters"
                    else -> null
                }
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
