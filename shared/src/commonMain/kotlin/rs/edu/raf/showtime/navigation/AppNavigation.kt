package rs.edu.raf.showtime.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject
import rs.edu.raf.showtime.auth.ui.navigation.AuthNavigation
import rs.edu.raf.showtime.core.auth.AuthStore
import rs.edu.raf.showtime.core.auth.model.AuthState
import rs.edu.raf.showtime.splash.ui.SplashScreen

@Composable
fun AppNavigation(
    authStore: AuthStore = koinInject()
) {
    val authState by authStore.authState.collectAsState()

    when (authState) {
        AuthState.Loading -> {
            SplashScreen()
        }

        AuthState.Unauthenticated -> {
            AuthNavigation()
        }

        is AuthState.Authenticated -> {
            MainNavigation()
        }
    }
}
