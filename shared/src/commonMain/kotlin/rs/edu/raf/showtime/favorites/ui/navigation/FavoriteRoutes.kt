package rs.edu.raf.showtime.favorites.ui.navigation

sealed class FavoriteRoutes(val route: String) {
    data object Favorites: FavoriteRoutes("favorites")
}