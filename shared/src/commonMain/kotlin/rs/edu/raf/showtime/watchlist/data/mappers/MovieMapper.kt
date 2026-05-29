package rs.edu.raf.showtime.watchlist.data.mappers

import rs.edu.raf.showtime.network.model.movies.MovieMinDTO
import rs.edu.raf.showtime.watchlist.db.WatchlistEntity

fun MovieMinDTO.toWatchlistEntity(): WatchlistEntity {
    return WatchlistEntity(imdbId)
}