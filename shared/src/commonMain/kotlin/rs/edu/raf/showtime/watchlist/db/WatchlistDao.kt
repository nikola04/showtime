package rs.edu.raf.showtime.watchlist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rs.edu.raf.showtime.movies.db.MovieEntity

@Dao
interface WatchlistDao {
    @Query("""
        SELECT movies.*
        FROM movies
        INNER JOIN watchlist
            ON movies.imdbId = watchlist.movieId
    """)
    fun observeWatchlistMovies(): Flow<List<MovieEntity>>

    @Query("""
        SELECT COUNT(watchlist.movieId)
        FROM watchlist
    """)
    fun observeWatchlistCount(): Flow<Long>

    @Query("""
        SELECT EXISTS(
            SELECT 1
            FROM watchlist
            WHERE movieId = :id
            LIMIT 1
        )
    """)
    fun observeWatchlistMovieExists(id: String): Flow<Boolean>

    @Query("""
        SELECT EXISTS(
            SELECT 1
            FROM watchlist
            WHERE movieId = :id
            LIMIT 1
        )
    """)
    suspend fun hasMovieInWatchlist(id: String): Boolean

    @Query("""
        SELECT COUNT(watchlist.movieId)
        FROM watchlist
    """)
    suspend fun getWatchlistCount(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(entity: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE movieId = :id")
    suspend fun removeFromWatchlist(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoWatchlist(movies: List<WatchlistEntity>)

    @Query("DELETE FROM watchlist")
    suspend fun clearWatchlist()
}