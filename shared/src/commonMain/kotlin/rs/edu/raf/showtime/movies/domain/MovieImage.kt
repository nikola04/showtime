package rs.edu.raf.showtime.movies.domain

data class MovieImage(
    val filePath: String,
    val width: Int? = null,
    val height: Int? = null,
    val voteAverage: Float? = null,
    val language: String? = null,
)