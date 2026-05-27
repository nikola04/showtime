package rs.edu.raf.showtime

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration
import rs.edu.raf.showtime.di.appModule
import rs.edu.raf.showtime.navigation.AppNavigation

@Composable
fun App() {
    AppLogger.init()

    val darkTheme = isSystemInDarkTheme()

    val colorScheme = when {
        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    KoinApplication(
        configuration = koinConfiguration(declaration = { modules(appModule) }),
        content = {
            MaterialTheme(colorScheme) {
                AppNavigation()
            }
        })
}

private object AppLogger {
    private var initialized = false

    fun init() {
        if (initialized) return

        Napier.base(DebugAntilog())
        initialized = true
    }
}
