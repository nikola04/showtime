package rs.edu.raf.showtime.network.model.movies

import kotlinx.serialization.Serializable
@Serializable
data class VideoDTO(
    val key: String,
    val site: String,
    val name: String? = null,
    val type: String? = null,
    val publishedAt: String? = null
)