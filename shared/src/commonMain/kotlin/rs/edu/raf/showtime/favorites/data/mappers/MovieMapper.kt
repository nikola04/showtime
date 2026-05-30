package rs.edu.raf.showtime.favorites.data.mappers

import rs.edu.raf.showtime.favorites.db.FavoritesEntity
import rs.edu.raf.showtime.network.model.movies.MovieMinDTO

fun MovieMinDTO.toFavoritesEntity(): FavoritesEntity {
    return FavoritesEntity(imdbId)
}