package rs.edu.raf.showtime.network

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import rs.edu.raf.showtime.network.model.movies.CastResponse
import rs.edu.raf.showtime.network.model.movies.ConfigDTO
import rs.edu.raf.showtime.network.model.movies.GenreDTO
import rs.edu.raf.showtime.network.model.movies.ImageResponse
import rs.edu.raf.showtime.network.model.movies.MovieDTO
import rs.edu.raf.showtime.network.model.movies.MovieListResponse
import rs.edu.raf.showtime.network.model.movies.VideoDTO

interface MovieAPI {

    @GET("movies")
    suspend fun getMovies(
        @Query("page_size") pageSize: Int = 30,
        @Query("sort_by") sortBy: String = "imdb_rating",
        @Query("sort_order") sortOrder: String = "desc",
        @Query("genre_id") genreId: Int? = null,
        @Query("query") query: String? = null,
        @Query("min_year") minYear: Int? = null,
        @Query("max_year") maxYear: Int? = null,
        @Query("min_rating") minRating: Float? = null
    ): MovieListResponse

    @GET("movies/{id}")
    suspend fun getMovieDetails(@Path("id") id: String): MovieDTO

    @GET("movies/{id}/cast")
    suspend fun getCast(
        @Path("id") id: String,
        @Query("page_size") pageSize: Int = 10
    ): CastResponse

    @GET("movies/{id}/images")
    suspend fun getImages(
        @Path("id") id: String,
        @Query("type") type: String = "backdrop",
        ): ImageResponse

    @GET("movies/{id}/videos")
    suspend fun getVideos(
        @Path("id") id: String,
        @Query("type") type: String = "Trailer"
    ): List<VideoDTO>

    @GET("genres")
    suspend fun getGenres(): List<GenreDTO>

    @GET("config")
    suspend fun getConfig(): ConfigDTO
}