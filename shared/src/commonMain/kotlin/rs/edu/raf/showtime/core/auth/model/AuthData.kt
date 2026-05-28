package rs.edu.raf.showtime.core.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthData(
    val accessToken: String? = null,
    val expiresAtEpochSeconds: Long? = null,
    val userId: Long? = null,
    val username: String? = null,
    val fullName: String? = null,
) {
    fun isExpired(currentEpochSeconds: Long = currentEpochSeconds()): Boolean {
        return expiresAtEpochSeconds?.let { it <= currentEpochSeconds } == true
    }

    companion object {
        fun empty(): AuthData = AuthData(accessToken = null)
    }
}

@OptIn(kotlin.time.ExperimentalTime::class)
fun currentEpochSeconds(): Long {
    return kotlin.time.Clock.System.now().epochSeconds
}
