package rs.edu.raf.showtime.watchlist.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.showtime.watchlist.ui.screen.WatchlistScreen
import rs.edu.raf.showtime.watchlist.ui.screen.WatchlistViewModel
import rs.edu.raf.showtime.movies.ui.navigation.MoviesRoutes

fun NavGraphBuilder.watchlistGraph(
    navController: NavController
) {
    composable(WatchlistRoutes.Watchlist.route) {
        val viewModel: WatchlistViewModel = koinViewModel()

        WatchlistScreen(
            viewModel,
            onMovieClick = { movieId ->
                navController.navigate(MoviesRoutes.MovieDetails().createRoute(movieId))
            }
        )
    }
}

