package rs.edu.raf.showtime.network

import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import rs.edu.raf.showtime.network.model.movies.MovieMinDTO

interface FavoritesAPI {
    @GET("me/favorites")
    suspend fun getFavorites(): List<MovieMinDTO>

    @POST("me/favorites/{id}")
    suspend fun addToFavorites(@Path("id") id: String)

    @DELETE("me/favorites/{id}")
    suspend fun removeFromFavorites(@Path("id") id: String)
}