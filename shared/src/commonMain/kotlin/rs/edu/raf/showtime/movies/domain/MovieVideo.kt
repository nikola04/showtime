package rs.edu.raf.showtime.movies.domain

data class MovieVideo(
    val key: String,
    val site: String,
    val name: String? = null,
    val type: String? = null,
    val publishedAt: String? = null
)