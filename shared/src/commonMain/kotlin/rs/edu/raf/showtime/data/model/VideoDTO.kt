package rs.edu.raf.showtime.data.model

import kotlinx.serialization.Serializable
@Serializable
data class VideoDTO(
    val key: String,
    val site: String,
    val name: String? = null,
    val type: String? = null,
    val official: Boolean,
    val publishedAt: String? = null
)