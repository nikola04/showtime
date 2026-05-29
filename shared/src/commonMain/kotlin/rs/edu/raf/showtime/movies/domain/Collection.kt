package rs.edu.raf.showtime.movies.domain

data class Collection(
    val id: Long,
    val name: String,
    val posterPath: String? = null,
    val backdropPath: String? = null,
)