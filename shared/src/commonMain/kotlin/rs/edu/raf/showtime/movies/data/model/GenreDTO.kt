package rs.edu.raf.showtime.movies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GenreDTO(
    val id: Int,
    val name: String
)