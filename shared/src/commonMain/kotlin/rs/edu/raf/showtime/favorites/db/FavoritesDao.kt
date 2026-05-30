package rs.edu.raf.showtime.favorites.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rs.edu.raf.showtime.movies.db.MovieEntity

@Dao
interface FavoritesDao {
    @Query("""
        SELECT movies.*
        FROM movies
        INNER JOIN favorites
            ON movies.imdbId = favorites.movieId
    """)
    fun observeFavoritesMovies(): Flow<List<MovieEntity>>

    @Query("""
        SELECT COUNT(favorites.movieId)
        FROM favorites
    """)
    fun observeFavoritesCount(): Flow<Long>

    @Query("""
        SELECT EXISTS(
            SELECT 1
            FROM favorites
            WHERE movieId = :id
            LIMIT 1
        )
    """)
    fun observeFavoritesMovieExists(id: String): Flow<Boolean>

    @Query("""
        SELECT EXISTS(
            SELECT 1
            FROM favorites
            WHERE movieId = :id
            LIMIT 1
        )
    """)
    suspend fun hasMovieInFavorites(id: String): Boolean

    @Query("""
        SELECT COUNT(favorites.movieId)
        FROM favorites
    """)
    suspend fun getFavoritesCount(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(entity: FavoritesEntity)

    @Query("DELETE FROM favorites WHERE movieId = :id")
    suspend fun removeFromFavorites(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoFavorites(movies: List<FavoritesEntity>)

    @Query("DELETE FROM favorites")
    suspend fun clearFavorites()
}