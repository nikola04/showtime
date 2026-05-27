package rs.edu.raf.showtime.auth.data.repository

import rs.edu.raf.showtime.auth.data.api.AuthAPI
import rs.edu.raf.showtime.auth.data.model.AuthResponse
import rs.edu.raf.showtime.auth.data.model.LoginRequest
import rs.edu.raf.showtime.auth.data.model.SignupRequest

class AuthRepository(
    private val api: AuthAPI,
) {
    suspend fun login(
        username: String,
        password: String,
    ): AuthResponse {
        return api.login(
            LoginRequest(
                username = username,
                password = password,
            )
        )
    }

    suspend fun signup(
        fullName: String,
        username: String,
        password: String,
    ): AuthResponse {
        return api.signup(
            SignupRequest(
                fullName = fullName,
                username = username,
                password = password,
            )
        )
    }
}
