package rs.edu.raf.showtime.movies.domain

data class MovieDetails(
    val imdbId: String,
    val title: String,

    val originalTitle: String? = null,

    val overview: String? = null,
    val tagline: String? = null,

    val releaseDate: String? = null,

    val year: Int? = null,
    val runtime: Int? = null,

    val budget: Long? = null,
    val revenue: Long? = null,

    val languageCode: String? = null,

    val popularity: Float? = null,

    val imdbRating: Float? = null,
    val imdbVotes: Int? = null,

    val tmdbRating: Float? = null,
    val tmdbVotes: Int? = null,

    val genres: List<Genre> = emptyList(),

    val posterPath: String? = null,
    val backdropPath: String? = null,

    val homepage: String? = null,

    val collection: Collection? = null,
)