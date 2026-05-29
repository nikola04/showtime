package rs.edu.raf.showtime.watchlist.ui.navigation

sealed class WatchlistRoutes(val route: String) {
    data object Watchlist: WatchlistRoutes("watchlist")
}