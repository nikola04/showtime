package rs.edu.raf.showtime.auth.data

import rs.edu.raf.showtime.network.AuthAPI
import rs.edu.raf.showtime.network.model.auth.AuthResponse
import rs.edu.raf.showtime.network.model.auth.LoginRequest
import rs.edu.raf.showtime.network.model.auth.SignupRequest

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