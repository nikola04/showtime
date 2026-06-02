package rs.edu.raf.showtime.core.util

import androidx.compose.runtime.Composable

@Composable
expect fun SystemBackHandler(enabled: Boolean = true, onBack: () -> Unit)
