package rs.edu.raf.showtime.auth.data.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import rs.edu.raf.showtime.auth.data.model.AuthResponse
import rs.edu.raf.showtime.auth.data.model.LoginRequest
import rs.edu.raf.showtime.auth.data.model.SignupRequest

interface AuthAPI {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): AuthResponse
}
