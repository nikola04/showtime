package rs.edu.raf.showtime.network.model.movies

import kotlinx.serialization.Serializable

@Serializable
data class GenreDTO(
    val id: Int,
    val name: String
)