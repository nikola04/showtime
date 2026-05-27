package rs.edu.raf.showtime.splash.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SplashScreen() {
    val backgroundColor = Color(0xFF576DB4)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Loading...",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
