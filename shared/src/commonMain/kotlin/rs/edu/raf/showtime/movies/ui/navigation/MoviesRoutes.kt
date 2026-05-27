package rs.edu.raf.showtime.movies.ui.navigation

sealed class MoviesRoutes(val route: String) {
    data object MovieList : MoviesRoutes("movie_list")
    data object MovieListFilters : MoviesRoutes("movie_list_filters")
    data class MovieDetails(val id: String = "") : MoviesRoutes("movie_details/{id}") {
        fun createRoute(id: String) = "movie_details/$id"
    }
}