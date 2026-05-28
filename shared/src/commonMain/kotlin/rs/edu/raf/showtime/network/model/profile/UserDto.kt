package rs.edu.raf.showtime.network.model.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Long,
    val username: String,
    @SerialName("full_name") val fullName: String
) {
}
