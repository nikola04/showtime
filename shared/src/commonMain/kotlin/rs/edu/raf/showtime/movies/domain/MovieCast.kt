package rs.edu.raf.showtime.movies.domain

data class MovieCast(
    val items: List<CastMember>,
    val totalItems: Int
)