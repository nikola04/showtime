package rs.edu.raf.showtime.favorites.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.showtime.favorites.ui.screen.FavoritesScreen
import rs.edu.raf.showtime.favorites.ui.screen.FavoritesViewModel
import rs.edu.raf.showtime.movies.ui.navigation.MoviesRoutes

fun NavGraphBuilder.favoritesGraph(
    navController: NavController
) {
    composable(FavoriteRoutes.Favorites.route) {
        val viewModel: FavoritesViewModel = koinViewModel()

        FavoritesScreen(
            viewModel,
            onMovieClick = { movieId ->
                navController.navigate(MoviesRoutes.MovieDetails().createRoute(movieId))
            }
        )
    }
}

