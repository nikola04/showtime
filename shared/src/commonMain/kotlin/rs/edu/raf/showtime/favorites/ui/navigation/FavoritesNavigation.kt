package rs.edu.raf.showtime.favorites.ui.navigation

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

object FavoriteRoutes {
    data object Favorites {
        const val route = "favorites"
    }
}

fun NavGraphBuilder.favoritesGraph(
    navController: NavController,
) {
    composable(FavoriteRoutes.Favorites.route) {
        FavoritesPlaceholderScreen()
    }
}

@Composable
private fun FavoritesPlaceholderScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Favorite",
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}
