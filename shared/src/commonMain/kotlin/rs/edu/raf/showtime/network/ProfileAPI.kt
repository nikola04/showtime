package rs.edu.raf.showtime.network

import de.jensklingenberg.ktorfit.http.GET
import rs.edu.raf.showtime.network.model.profile.UserDto

interface ProfileAPI {
    @GET("/me")
    suspend fun profileMe(): UserDto
}