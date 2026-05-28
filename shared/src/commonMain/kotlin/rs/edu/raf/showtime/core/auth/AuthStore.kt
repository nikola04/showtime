package rs.edu.raf.showtime.core.auth

import androidx.datastore.core.DataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import rs.edu.raf.showtime.core.auth.model.AuthData
import rs.edu.raf.showtime.core.auth.model.AuthState
import rs.edu.raf.showtime.core.auth.model.asAuthState
import rs.edu.raf.showtime.core.auth.model.currentEpochSeconds

class AuthStore(private val persistence: DataStore<AuthData>) {
    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun setAuthData(authData: AuthData) {
        persistence.updateData { authData }
    }

    suspend fun setAccessToken(token: String) {
        persistence.updateData {
            it.copy(accessToken = token)
        }
    }

    suspend fun setAuthSession(
        accessToken: String,
        expiresInSeconds: Long,
        userId: Long,
        username: String,
        fullName: String,
    ) {
        persistence.updateData {
            it.copy(
                accessToken = accessToken,
                expiresAtEpochSeconds = currentEpochSeconds() + expiresInSeconds,
                userId = userId,
                username = username,
                fullName = fullName,
            )
        }
    }

    suspend fun clearAuthData() {
        persistence.updateData {
            AuthData.empty()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val authState: StateFlow<AuthState> = persistence.data
        .distinctUntilChanged()
        .transformLatest { authData ->
            val authState = authData.asAuthState()
            emit(authState)

            if (authState is AuthState.Authenticated) {
                val expiresAt = authState.data.expiresAtEpochSeconds ?: return@transformLatest
                val secondsUntilExpiry = expiresAt - currentEpochSeconds()

                if (secondsUntilExpiry > 0) {
                    delay(secondsUntilExpiry * 1000L)
                }

                emit(AuthState.Unauthenticated)
            }
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = AuthState.Loading
        )
}
