package rs.edu.raf.showtime.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
)

@Serializable
data class SignupRequest(
    @SerialName("full_name") val fullName: String,
    val username: String,
    val password: String,
)

@Serializable
data class AuthResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Long,
    val user: AuthUserDTO,
)

@Serializable
data class AuthUserDTO(
    val id: Long,
    val username: String,
    @SerialName("full_name") val fullName: String,
)
