package rs.edu.raf.showtime.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GenreDTO(
    val id: Int,
    val name: String
)