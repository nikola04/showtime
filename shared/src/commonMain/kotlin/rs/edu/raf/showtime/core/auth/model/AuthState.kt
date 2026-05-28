package rs.edu.raf.showtime.core.auth.model

sealed class AuthState {
    data object Loading: AuthState()
    data object Unauthenticated: AuthState()
    data class Authenticated(val data: AuthData): AuthState()
}

fun AuthData.asAuthState(): AuthState {
    return when {
        accessToken.isNullOrBlank() || isExpired() -> AuthState.Unauthenticated
        else -> AuthState.Authenticated(data = this.copy())
    }
}
