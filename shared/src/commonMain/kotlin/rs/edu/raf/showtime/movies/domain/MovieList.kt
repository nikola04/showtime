package rs.edu.raf.showtime.movies.domain

data class MovieList(
    val items: List<Movie>,
    val totalItems: Int
)