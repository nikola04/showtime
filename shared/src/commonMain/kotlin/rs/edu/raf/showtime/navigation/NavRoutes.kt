package rs.edu.raf.showtime.navigation

sealed class NavRoutes(val route: String) {
    data object MovieList : NavRoutes("movie_list")
    data object MovieListFilters : NavRoutes("movie_list_filters")
    data class MovieDetails(val id: String = "") : NavRoutes("movie_details/{id}") {
        fun createRoute(id: String) = "movie_details/$id"
    }
}