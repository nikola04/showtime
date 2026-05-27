package rs.edu.raf.showtime.movies.ui

import androidx.compose.runtime.Composable
import rs.edu.raf.showtime.movies.ui.navigation.MoviesNavigation
import rs.edu.raf.showtime.movies.ui.navigation.MoviesRoutes

@Composable
fun MoviesApp() {
    MoviesNavigation(
        startDestination = MoviesRoutes.MovieList.route
    )
}
