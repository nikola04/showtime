package rs.edu.raf.showtime.data.repository

import rs.edu.raf.showtime.data.api.MovieAPI
import rs.edu.raf.showtime.data.model.*

class MovieRepository(private val api: MovieAPI) {

    suspend fun getMovies(
        pageSize: Int = 30,
        sortBy: String = "imdb_rating",
        sortOrder: String = "desc",
        genreId: Int? = null,
        query: String? = null,
        minYear: Int? = null,
        maxYear: Int? = null,
        minRating: Float? = null
    ): MovieListResponse = api.getMovies(pageSize, sortBy, sortOrder, genreId, query, minYear, maxYear, minRating)

    suspend fun getMovieDetails(id: String): MovieDTO = api.getMovieDetails(id)

    suspend fun getCast(id: String): CastResponse = api.getCast(id)

    suspend fun getImages(id: String): ImageResponse = api.getImages(id)

    suspend fun getVideos(id: String, type: String = "Trailer"): List<VideoDTO> = api.getVideos(id, type)

    suspend fun getGenres(): List<GenreDTO> = api.getGenres()
}