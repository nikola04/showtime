package rs.edu.raf.showtime.movies.data

import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import rs.edu.raf.showtime.core.db.AppDatabase
import rs.edu.raf.showtime.movies.data.mapper.asDomainMovie
import rs.edu.raf.showtime.movies.data.mapper.toCollectionEntity
import rs.edu.raf.showtime.movies.data.mapper.toDomain
import rs.edu.raf.showtime.movies.data.mapper.toGenreEntity
import rs.edu.raf.showtime.movies.data.mapper.toMovieEntity
import rs.edu.raf.showtime.movies.data.mapper.toMovieGenreCrossRefs
import rs.edu.raf.showtime.movies.domain.Genre
import rs.edu.raf.showtime.movies.domain.Movie
import rs.edu.raf.showtime.movies.domain.MovieCast
import rs.edu.raf.showtime.movies.domain.MovieDetails
import rs.edu.raf.showtime.movies.domain.MovieImages
import rs.edu.raf.showtime.movies.domain.MovieList
import rs.edu.raf.showtime.movies.domain.MovieRepository
import rs.edu.raf.showtime.movies.domain.MovieVideo
import rs.edu.raf.showtime.network.MovieAPI
import rs.edu.raf.showtime.network.model.movies.MovieMinDTO

class MovieRepository(
    private val appDatabase: AppDatabase,
    private val api: MovieAPI,
) : MovieRepository {

    override fun observeMovies(
        sortBy: String,
        sortOrder: String,
        genreId: Int?,
        query: String?,
        minYear: Int?,
        maxYear: Int?,
        minRating: Float?,
    ): Flow<List<Movie>> =
        appDatabase.movieDao()
            .observeMovies(
                sortBy = sortBy,
                sortOrder = sortOrder,
                genreId = genreId,
                query = query,
                minYear = minYear,
                maxYear = maxYear,
                minRating = minRating,
            )
            .distinctUntilChanged()
            .map { rows ->
                rows.map { it.asDomainMovie() }
            }

    override fun observeMovieDetails(
        imdbId: String
    ): Flow<MovieDetails?> =
        appDatabase.movieDao()
            .observeMovieDetails(imdbId)
            .map { row ->
                row?.toDomain()
            }

    override fun observeGenres(): Flow<List<Genre>> =
        appDatabase.movieDao()
            .observeGenres()
            .distinctUntilChanged()
            .map { rows ->
                rows.map { it.toDomain() }
            }

    override suspend fun getGenres(): List<Genre> {
        val localGenres = appDatabase.movieDao().getGenres()
        return if (localGenres.isEmpty()) {
            refreshGenres()
            appDatabase.movieDao().getGenres().map { it.toDomain() }
        } else {
            localGenres.map { it.toDomain() }
        }
    }

    override suspend fun getMovies(
        sortBy: String,
        sortOrder: String,
        genreId: Int?,
        query: String?,
        minYear: Int?,
        maxYear: Int?,
        minRating: Float?
    ): MovieList {
        val response = api.getMovies(
            page = 1,
            pageSize = 30,
            sortBy = sortBy,
            sortOrder = sortOrder,
            genreId = genreId,
            query = query,
            minYear = minYear,
            maxYear = maxYear,
            minRating = minRating
        )
        return MovieList(
            items = response.items.map { it.toDomain() },
            totalItems = response.totalItems
        )
    }

    override suspend fun getMovieDetails(imdbId: String): MovieDetails {
        return api.getMovieDetails(imdbId).toDomain()
    }

    override suspend fun getCast(imdbId: String): MovieCast {
        val response = api.getCast(imdbId)
        return MovieCast(
            items = response.items.map { it.toDomain() },
            totalItems = response.totalItems
        )
    }

    override suspend fun getImages(imdbId: String): MovieImages {
        val response = api.getImages(imdbId)
        return MovieImages(
            backdrops = response.backdrops.map { it.toDomain() },
            posters = response.posters.map { it.toDomain() },
            logos = response.logos.map { it.toDomain() }
        )
    }

    override suspend fun getVideos(imdbId: String, type: String): List<MovieVideo> {
        return api.getVideos(imdbId, type).map { it.toDomain() }
    }

    override suspend fun refreshMovies() {
        val initialResponse = api.getMovies(page = 1, pageSize = 30)
        val totalItems = initialResponse.totalItems
        val localCount = appDatabase.movieDao().getMoviesCount()

        Napier.e("Total: ${totalItems}; Local: $localCount")
        if (totalItems != localCount) {
            val allMovies = mutableListOf<MovieMinDTO>()
            val pageSize = 100
            val totalPages = (totalItems + pageSize - 1) / pageSize

            for (page in 1..totalPages) {
                val response = api.getMovies(page = page, pageSize = pageSize)
                allMovies.addAll(response.items)
            }

            val movies = allMovies.map {
                it.toMovieEntity()
            }

            val genres = allMovies
                .flatMap { it.genres }
                .distinctBy { it.id }
                .map { it.toGenreEntity() }

            val refs = allMovies.flatMap {
                it.toMovieGenreCrossRefs()
            }

            appDatabase.movieDao().refreshMoviesTransaction(
                movies = movies,
                genres = genres,
                refs = refs,
            )
        }
    }

    override suspend fun refreshMovieDetails(
        imdbId: String
    ) {
        val dto = api.getMovieDetails(imdbId)

        val movie = dto.toMovieEntity()

        val collection = dto.collection?.toCollectionEntity()

        val genres = dto.genres.map {
            it.toGenreEntity()
        }

        val refs = dto.toMovieGenreCrossRefs()

        appDatabase.movieDao().refreshMovieDetailsTransaction(
            movie = movie,
            collection = collection,
            genres = genres,
            refs = refs,
        )
    }

    override suspend fun refreshGenres() {
        val genres = api.getGenres()
            .map { it.toGenreEntity() }

        appDatabase.movieDao().insertGenres(genres)
    }
}