package rs.edu.raf.showtime.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import rs.edu.raf.showtime.ui.screen.moviedetails.MovieDetailsScreen
import rs.edu.raf.showtime.ui.screen.movielist.MovieListScreen
import rs.edu.raf.showtime.ui.screen.movielistfilter.MovieListFiltersScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.MovieList.route
    ) {
        composable(NavRoutes.MovieList.route) {
            MovieListScreen(navController)
        }

        composable(NavRoutes.MovieListFilters.route) {
            MovieListFiltersScreen(navController)
        }

        composable(route = NavRoutes.MovieDetails().route) {
            MovieDetailsScreen(navController)
        }
    }
}