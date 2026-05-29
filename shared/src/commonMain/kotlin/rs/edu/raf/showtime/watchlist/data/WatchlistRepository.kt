package rs.edu.raf.showtime.watchlist.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import rs.edu.raf.showtime.core.db.AppDatabase
import rs.edu.raf.showtime.movies.data.mapper.toDomain
import rs.edu.raf.showtime.movies.domain.Movie
import rs.edu.raf.showtime.network.WatchlistAPI
import rs.edu.raf.showtime.watchlist.data.mappers.toWatchlistEntity
import rs.edu.raf.showtime.watchlist.db.WatchlistEntity
import rs.edu.raf.showtime.watchlist.domain.WatchlistRepository
import kotlin.collections.map

class WatchlistRepository(
    val appDatabase: AppDatabase,
    val api: WatchlistAPI
) : WatchlistRepository {
    override fun observeWatchlistMovies(): Flow<List<Movie>> {
        return appDatabase.watchlistDao()
            .observeWatchlistMovies()
            .distinctUntilChanged()
            .map { rows ->
                rows.map { it.toDomain() }
            }
    }

    override fun observeWatchlistCount(): Flow<Long> {
        return appDatabase.watchlistDao()
            .observeWatchlistCount()
    }

    override fun observeWatchlistMovieState(id: String): Flow<Boolean> {
        return appDatabase.watchlistDao()
            .observeWatchlistMovieExists(id)
    }

    override suspend fun getWatchlistMoviesCount(): Long {
        return appDatabase.watchlistDao().getWatchlistCount()
    }

    override suspend fun getWatchlistMovies(): List<Movie> {
        return api.getWatchlist().map { it.toDomain() }
    }

    override suspend fun refreshWatchlist() {
        val remoteMovies = api.getWatchlist()

        val entities = remoteMovies.map { it.toWatchlistEntity() }

        appDatabase.watchlistDao().apply {
            clearWatchlist()
            insertIntoWatchlist(entities)
        }
    }

    override suspend fun isMovieInWatchlist(id: String): Boolean {
        return appDatabase.watchlistDao().hasMovieInWatchlist(id)
    }

    override suspend fun toggleMovie(id: String) {
        val wasInWatchlist = appDatabase.watchlistDao().hasMovieInWatchlist(id)

        if (wasInWatchlist) {
            appDatabase.watchlistDao().removeFromWatchlist(id)
        } else appDatabase.watchlistDao().addToWatchlist(WatchlistEntity(id))
        try {
            if (wasInWatchlist) {
                api.removeFromWatchlist(id)
            } else api.addToWatchlist(id)
        } catch (_: Exception) {
            // Rollback if network fails
            if (wasInWatchlist) {
                appDatabase.watchlistDao().addToWatchlist(WatchlistEntity(id))
            } else appDatabase.watchlistDao().removeFromWatchlist(id)
        }
    }
}