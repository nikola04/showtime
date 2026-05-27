package rs.edu.raf.showtime.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import rs.edu.raf.showtime.movies.ui.navigation.MoviesRoutes
import rs.edu.raf.showtime.movies.ui.navigation.moviesGraph

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MoviesRoutes.MovieList.route
    ) {
        moviesGraph(navController)
    }
}
