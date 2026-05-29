package rs.edu.raf.showtime.network

import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import rs.edu.raf.showtime.network.model.movies.MovieMinDTO


interface WatchlistAPI {
    @GET("me/watchlist")
    suspend fun getWatchlist(): List<MovieMinDTO>

    @POST("me/watchlist/{id}")
    suspend fun addToWatchlist(@Path("id") id: String)

    @DELETE("me/watchlist/{id}")
    suspend fun removeFromWatchlist(@Path("id") id: String)
}