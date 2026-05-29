package rs.edu.raf.showtime.movies.domain

data class CastMember(
    val imdbId: String,
    val name: String,
    val professions: String? = null,
    val profilePath: String? = null,
    val department: String? = null
)