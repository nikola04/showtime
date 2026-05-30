package rs.edu.raf.showtime.watchlist.domain

import kotlinx.coroutines.flow.Flow
import rs.edu.raf.showtime.movies.domain.Movie

interface WatchlistRepository {
    fun observeWatchlistMovies(): Flow<List<Movie>>

    fun observeWatchlistCount(): Flow<Long>

    fun observeWatchlistMovieState(id: String): Flow<Boolean>

    suspend fun getWatchlistMoviesCount(): Long

    suspend fun getWatchlistMovies(): List<Movie>

    suspend fun refreshWatchlist()

    suspend fun isMovieInWatchlist(id: String): Boolean

    suspend fun toggleMovie(id: String)

    suspend fun emptyWatchlist()
}