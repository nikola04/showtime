package rs.edu.raf.showtime.watchlist.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

object WatchlistRoutes {
    data object Watchlist {
        const val route = "watchlist"
    }
}

fun NavGraphBuilder.watchlistGraph(
    navController: NavController,
) {
    composable(WatchlistRoutes.Watchlist.route) {
        WatchlistPlaceholderScreen()
    }
}

@Composable
private fun WatchlistPlaceholderScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Watchlist",
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}
