package rs.edu.raf.showtime.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import rs.edu.raf.showtime.core.db.AppDatabase
import rs.edu.raf.showtime.favorites.data.mappers.toFavoritesEntity
import rs.edu.raf.showtime.favorites.db.FavoritesEntity
import rs.edu.raf.showtime.favorites.domain.FavoritesRepository
import rs.edu.raf.showtime.movies.data.mapper.toDomain
import rs.edu.raf.showtime.movies.domain.Movie
import rs.edu.raf.showtime.network.FavoritesAPI
import kotlin.collections.map

class FavoritesRepository(
    val appDatabase: AppDatabase,
    val api: FavoritesAPI
) : FavoritesRepository {
    override fun observeFavoriteMovies(): Flow<List<Movie>> {
        return appDatabase.favoritesDao()
            .observeFavoritesMovies()
            .distinctUntilChanged()
            .map { rows ->
                rows.map { it.toDomain() }
            }
    }

    override fun observeFavoriteCount(): Flow<Long> {
        return appDatabase.favoritesDao()
            .observeFavoritesCount()
    }

    override fun observeFavoriteMovieState(id: String): Flow<Boolean> {
        return appDatabase.favoritesDao()
            .observeFavoritesMovieExists(id)
    }

    override suspend fun getFavoriteMoviesCount(): Long {
        return appDatabase.favoritesDao().getFavoritesCount()
    }

    override suspend fun getFavoriteMovies(): List<Movie> {
        return api.getFavorites().map { it.toDomain() }
    }

    override suspend fun refreshFavorite() {
        val remoteMovies = api.getFavorites()

        val entities = remoteMovies.map { it.toFavoritesEntity() }

        appDatabase.favoritesDao().apply {
            clearFavorites()
            insertIntoFavorites(entities)
        }
    }

    override suspend fun isMovieInFavorite(id: String): Boolean {
        return appDatabase.favoritesDao().hasMovieInFavorites(id)
    }

    override suspend fun toggleMovie(id: String) {
        val wasInFavorite = appDatabase.favoritesDao().hasMovieInFavorites(id)

        if (wasInFavorite) {
            appDatabase.favoritesDao().removeFromFavorites(id)
        } else appDatabase.favoritesDao().addToFavorites(FavoritesEntity(id))
        try {
            if (wasInFavorite) {
                api.removeFromFavorites(id)
            } else api.addToFavorites(id)
        } catch (e: Exception) {
            // Rollback if network fails
            if (wasInFavorite) {
                appDatabase.favoritesDao().addToFavorites(FavoritesEntity(id))
            } else appDatabase.favoritesDao().removeFromFavorites(id)

            throw e
        }
    }
}