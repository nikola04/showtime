package rs.edu.raf.showtime.network.model.movies

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfigDTO(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String
)