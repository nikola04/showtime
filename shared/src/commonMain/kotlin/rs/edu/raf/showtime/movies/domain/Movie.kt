package rs.edu.raf.showtime.movies.domain

data class Movie(
    val imdbId: String,
    val title: String,

    val year: Int? = null,
    val runtime: Int? = null,

    val imdbRating: Float? = null,
    val imdbVotes: Int? = null,

    val posterPath: String? = null,

    val genres: List<Genre> = emptyList(),

    val budget: Long? = null,
    val revenue: Long? = null,

    val language: String? = null,

    val popularity: Float? = null,
)