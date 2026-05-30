package rs.edu.raf.showtime.favorites.domain

import kotlinx.coroutines.flow.Flow
import rs.edu.raf.showtime.movies.domain.Movie

interface FavoritesRepository {
    fun observeFavoriteMovies(): Flow<List<Movie>>

    fun observeFavoriteCount(): Flow<Long>

    fun observeFavoriteMovieState(id: String): Flow<Boolean>

    suspend fun getFavoriteMoviesCount(): Long

    suspend fun getFavoriteMovies(): List<Movie>

    suspend fun refreshFavorite()

    suspend fun isMovieInFavorite(id: String): Boolean

    suspend fun toggleMovie(id: String)
}