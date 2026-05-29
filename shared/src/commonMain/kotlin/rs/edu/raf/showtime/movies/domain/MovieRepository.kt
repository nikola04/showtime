package rs.edu.raf.showtime.movies.domain

import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun observeMovies(
        sortBy: String = "imdb_rating",
        sortOrder: String = "desc",
        genreId: Int? = null,
        query: String? = null,
        minYear: Int? = null,
        maxYear: Int? = null,
        minRating: Float? = null,
    ): Flow<List<Movie>>

    fun observeMovieDetails(
        imdbId: String
    ): Flow<MovieDetails?>

    suspend fun hasMovieDetails(imdbId: String): Boolean

    fun observeGenres(): Flow<List<Genre>>

    suspend fun getGenres(): List<Genre>

    suspend fun getMovies(
        sortBy: String = "imdb_rating",
        sortOrder: String = "desc",
        genreId: Int? = null,
        query: String? = null,
        minYear: Int? = null,
        maxYear: Int? = null,
        minRating: Float? = null
    ): MovieList

    suspend fun getMovieDetails(imdbId: String): MovieDetails

    suspend fun getCast(imdbId: String): MovieCast

    suspend fun getImages(imdbId: String): MovieImages

    suspend fun getVideos(imdbId: String, type: String = "Trailer"): List<MovieVideo>

    suspend fun refreshMovies()

    suspend fun refreshMovieDetails(imdbId: String)

    suspend fun refreshGenres()
}