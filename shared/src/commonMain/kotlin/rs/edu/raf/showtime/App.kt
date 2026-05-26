package rs.edu.raf.showtime

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.core.KoinApplication
import org.koin.dsl.koinConfiguration
import rs.edu.raf.showtime.di.appModule
import rs.edu.raf.showtime.navigation.AppNavigation

@Composable
fun App() {
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