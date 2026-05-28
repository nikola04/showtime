package rs.edu.raf.showtime.profile.ui.navigation

sealed class ProfileRoutes(val route: String) {
    data object Profile : ProfileRoutes("profile")
}