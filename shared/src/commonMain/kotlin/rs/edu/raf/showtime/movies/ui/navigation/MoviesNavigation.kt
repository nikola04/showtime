package rs.edu.raf.showtime.movies.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.showtime.movies.ui.screen.moviedetails.MovieDetailsScreen
import rs.edu.raf.showtime.movies.ui.screen.moviedetails.MovieDetailsViewModel
import rs.edu.raf.showtime.movies.ui.screen.movielist.MovieListScreen
import rs.edu.raf.showtime.movies.ui.screen.movielist.MovieListViewModel
import rs.edu.raf.showtime.movies.ui.screen.movielistfilter.MovieListFiltersScreen
import rs.edu.raf.showtime.movies.ui.screen.movielistfilter.MovieListFiltersViewModel

fun NavGraphBuilder.moviesGraph(
    navController: NavController
) {
    composable(MoviesRoutes.MovieList.route) {
        val viewModel: MovieListViewModel = koinViewModel()
        MovieListScreen(
            viewModel,
            onMovieClick = { movieId ->
                navController.navigate(MoviesRoutes.MovieDetails().createRoute(movieId))
            },
            onFiltersClick = {
                navController.navigate(MoviesRoutes.MovieListFilters.route)
            }
        )
    }

    composable(MoviesRoutes.MovieListFilters.route) {
        val viewModel: MovieListFiltersViewModel = koinViewModel()
        MovieListFiltersScreen(
            viewModel,
            onBackClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            },
            onApplyFilters = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                } else {
                    navController.navigate(MoviesRoutes.MovieList.route)
                }
            }
        )
    }

    composable(MoviesRoutes.MovieDetails().route) {
        val viewModel: MovieDetailsViewModel = koinViewModel()
        MovieDetailsScreen(
            viewModel,
            onBackClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            }
        )
    }
}
