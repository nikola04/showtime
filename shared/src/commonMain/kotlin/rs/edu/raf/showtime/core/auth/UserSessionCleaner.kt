package rs.edu.raf.showtime.core.auth

import rs.edu.raf.showtime.favorites.data.FavoritesRepository
import rs.edu.raf.showtime.watchlist.data.WatchlistRepository

class UserSessionCleaner(
    val watchlistRepository: WatchlistRepository,
    val favoritesRepository: FavoritesRepository
) {
    suspend fun clearUserData() {
        watchlistRepository.emptyWatchlist()
        favoritesRepository.emptyFavorites()
    }
}
