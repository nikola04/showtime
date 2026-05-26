package rs.edu.raf.showtime.core.auth

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import rs.edu.raf.showtime.core.auth.model.AuthData
import rs.edu.raf.showtime.core.auth.model.AuthState
import rs.edu.raf.showtime.core.auth.model.asAuthState

class AuthStore(private val persistence: DataStore<AuthData>) {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val authData = persistence.data.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = runBlocking { persistence.data.first() }
    )

    suspend fun setAuthData(authData: AuthData) {
        persistence.updateData { authData }
    }

    suspend fun setAccessToken(token: String) {
        persistence.updateData {
            it.copy(accessToken = token)
        }
    }

    suspend fun clearAuthData() {
        persistence.updateData {
            AuthData.empty()
        }
    }

    val authState: StateFlow<AuthState> = persistence.data
        .map { it.asAuthState() }
        .distinctUntilChanged()
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = AuthState.Unauthenticated
        )
}