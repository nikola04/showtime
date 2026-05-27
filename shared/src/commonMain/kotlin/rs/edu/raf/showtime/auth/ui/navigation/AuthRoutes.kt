package rs.edu.raf.showtime.auth.ui.navigation

sealed class AuthRoutes(val route: String) {
    data object Login: AuthRoutes("auth/login")
    data object Register: AuthRoutes("auth/register")
}