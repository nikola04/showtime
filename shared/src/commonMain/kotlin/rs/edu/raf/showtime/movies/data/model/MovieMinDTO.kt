package rs.edu.raf.showtime.movies.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListResponse(
    val items: List<MovieMinDTO>,
    val totalItems: Int
)

@Serializable
data class MovieMinDTO(
    val imdbId: String,
    val title: String,
    val year: Int? = null,
    val runtime: Int? = null,
    @SerialName("imdbRating") val imdbRating: Double? = null,
    @SerialName("imdbVotes") val imdbVotes: Int? = null,
    @SerialName("posterPath") val posterPath: String? = null,
    val genres: List<GenreDTO> = emptyList(),
    val budget: Long? = null,
    val revenue: Long? = null,
    val language: String? = null,
    val popularity: Double? = null
)