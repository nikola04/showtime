package rs.edu.raf.showtime.movies.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.edu.raf.showtime.movies.ui.screen.moviedetails.MovieDetailsScreen
import rs.edu.raf.showtime.movies.ui.screen.movielist.MovieListScreen
import rs.edu.raf.showtime.movies.ui.screen.movielistfilter.MovieListFiltersScreen

fun NavGraphBuilder.moviesGraph(
    navController: NavController
) {
    composable(MoviesRoutes.MovieList.route) {
        MovieListScreen(
            onMovieClick = { movieId ->
                navController.navigate(MoviesRoutes.MovieDetails().createRoute(movieId))
            },
            onFiltersClick = {
                navController.navigate(MoviesRoutes.MovieListFilters.route)
            }
        )
    }

    composable(MoviesRoutes.MovieListFilters.route) {
        MovieListFiltersScreen(
            onBackClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            },
            onApplyFilters = {
                navController.navigate(MoviesRoutes.MovieList.route)
            }
        )
    }

    composable(MoviesRoutes.MovieDetails().route) {
        MovieDetailsScreen(
            onBackClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            }
        )
    }
}
