package rs.edu.raf.showtime.movies.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    // TRANSACTIONS

    @Transaction
    suspend fun refreshMoviesTransaction(
        movies: List<MovieEntity>,
        genres: List<GenreEntity>,
        refs: List<MovieGenreCrossRef>,
    ) {
        insertMovies(movies)
        insertGenres(genres)
        insertMovieGenreCrossRefs(refs)
    }

    @Transaction
    suspend fun refreshMovieDetailsTransaction(
        movie: MovieEntity,
        collection: CollectionEntity?,
        genres: List<GenreEntity>,
        refs: List<MovieGenreCrossRef>,
    ) {
        insertMovie(movie)

        collection?.let {
            insertCollections(listOf(it))
        }

        insertGenres(genres)
        insertMovieGenreCrossRefs(refs)
    }

    // INSERTS

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollections(collections: List<CollectionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenreCrossRefs(
        refs: List<MovieGenreCrossRef>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCastMembers(cast: List<CastMemberEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieImages(images: List<MovieImageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieVideos(videos: List<MovieVideoEntity>)

    // GET SINGLE MOVIE

    @Transaction
    @Query("""
        SELECT * FROM movies
        WHERE imdbId = :id
    """)
    suspend fun getMovieDetails(id: String): MovieDetailed?

    @Transaction
    @Query("""
        SELECT * FROM movies
        WHERE imdbId = :id
    """)
    fun observeMovieDetails(id: String): Flow<MovieDetailed?>

    // GET MOVIES

    @Transaction
    @Query("""
    SELECT * FROM movies
    WHERE
        (:query IS NULL OR title LIKE '%' || :query || '%')

        AND (:minYear IS NULL OR year >= :minYear)

        AND (:maxYear IS NULL OR year <= :maxYear)

        AND (:minRating IS NULL OR imdbRating >= :minRating)

        AND (
            :genreId IS NULL
            OR EXISTS (
                SELECT 1
                FROM MovieGenreCrossRef
                WHERE imdbId = movies.imdbId
                AND genreId = :genreId
            )
        )

    ORDER BY

        CASE
            WHEN :sortBy = 'title'
            AND :sortOrder = 'asc'
            THEN title
        END ASC,

        CASE
            WHEN :sortBy = 'title'
            AND :sortOrder = 'desc'
            THEN title
        END DESC,

        CASE
            WHEN :sortBy = 'year'
            AND :sortOrder = 'asc'
            THEN year
        END ASC,

        CASE
            WHEN :sortBy = 'year'
            AND :sortOrder = 'desc'
            THEN year
        END DESC,

        CASE
            WHEN :sortBy = 'imdb_rating'
            AND :sortOrder = 'asc'
            THEN imdbRating
        END ASC,

        CASE
            WHEN :sortBy = 'imdb_rating'
            AND :sortOrder = 'desc'
            THEN imdbRating
        END DESC
""")
    suspend fun getMovies(
        sortBy: String = "imdb_rating",
        sortOrder: String = "desc",
        genreId: Int? = null,
        query: String? = null,
        minYear: Int? = null,
        maxYear: Int? = null,
        minRating: Float? = null,
    ): List<MovieDetailed>

    @Transaction
    @Query("""
    SELECT * FROM movies
    WHERE
        (:query IS NULL OR title LIKE '%' || :query || '%')

        AND (:minYear IS NULL OR year >= :minYear)

        AND (:maxYear IS NULL OR year <= :maxYear)

        AND (:minRating IS NULL OR imdbRating >= :minRating)

        AND (
            :genreId IS NULL
            OR EXISTS (
                SELECT 1
                FROM MovieGenreCrossRef
                WHERE imdbId = movies.imdbId
                AND genreId = :genreId
            )
        )

    ORDER BY

        CASE
            WHEN :sortBy = 'title'
            AND :sortOrder = 'asc'
            THEN title
        END ASC,

        CASE
            WHEN :sortBy = 'title'
            AND :sortOrder = 'desc'
            THEN title
        END DESC,

        CASE
            WHEN :sortBy = 'year'
            AND :sortOrder = 'asc'
            THEN year
        END ASC,

        CASE
            WHEN :sortBy = 'year'
            AND :sortOrder = 'desc'
            THEN year
        END DESC,

        CASE
            WHEN :sortBy = 'imdb_rating'
            AND :sortOrder = 'asc'
            THEN imdbRating
        END ASC,

        CASE
            WHEN :sortBy = 'imdb_rating'
            AND :sortOrder = 'desc'
            THEN imdbRating
        END DESC
""")
    fun observeMovies(
        sortBy: String = "imdb_rating",
        sortOrder: String = "desc",
        genreId: Int? = null,
        query: String? = null,
        minYear: Int? = null,
        maxYear: Int? = null,
        minRating: Float? = null,
    ): Flow<List<MovieDetailed>>

    // GENRES

    @Query("SELECT * FROM cast_members WHERE imdbId = :imdbId")
    fun observeCast(imdbId: String): Flow<List<CastMemberEntity>>

    @Query("SELECT * FROM cast_members WHERE imdbId = :imdbId")
    suspend fun getCast(imdbId: String): List<CastMemberEntity>

    @Query("SELECT * FROM movie_images WHERE imdbId = :imdbId")
    fun observeImages(imdbId: String): Flow<List<MovieImageEntity>>

    @Query("SELECT * FROM movie_images WHERE imdbId = :imdbId")
    suspend fun getImages(imdbId: String): List<MovieImageEntity>

    @Query("SELECT * FROM movie_videos WHERE imdbId = :imdbId")
    fun observeVideos(imdbId: String): Flow<List<MovieVideoEntity>>

    @Query("SELECT * FROM movie_videos WHERE imdbId = :imdbId")
    suspend fun getVideos(imdbId: String): List<MovieVideoEntity>

    @Query("DELETE FROM cast_members WHERE imdbId = :imdbId")
    suspend fun deleteCast(imdbId: String)

    @Query("DELETE FROM movie_images WHERE imdbId = :imdbId")
    suspend fun deleteImages(imdbId: String)

    @Query("DELETE FROM movie_videos WHERE imdbId = :imdbId")
    suspend fun deleteVideos(imdbId: String)

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getMoviesCount(): Int

    @Query("""
        SELECT * FROM GenreEntity
        ORDER BY name ASC
    """)
    suspend fun getGenres(): List<GenreEntity>

    @Query("""
        SELECT * FROM GenreEntity
        ORDER BY name ASC
    """)
    fun observeGenres(): Flow<List<GenreEntity>>

    // DELETE

    @Query("DELETE FROM movies")
    suspend fun clearMovies()

    @Query("DELETE FROM MovieGenreCrossRef")
    suspend fun clearMovieGenreCrossRefs()
}