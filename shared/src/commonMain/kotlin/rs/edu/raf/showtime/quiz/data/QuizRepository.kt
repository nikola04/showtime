package rs.edu.raf.showtime.quiz.data

import rs.edu.raf.showtime.core.db.AppDatabase
import rs.edu.raf.showtime.quiz.domain.QuizMovie
import rs.edu.raf.showtime.quiz.domain.QuizRepository

class QuizRepository(
    private val appDatabase: AppDatabase
): QuizRepository {
    override suspend fun getQuizMoviesPool(): List<QuizMovie> {
        val movies = appDatabase.movieDao().getAllMovies()

        return movies.map { movie ->

            val cast = try {
                appDatabase.movieDao()
                    .getCast(movie.imdbId)
                    .map { it.name }
            } catch (_: Exception) {
                emptyList()
            }

            val images = try {
                appDatabase.movieDao()
                    .getImages(movie.imdbId)
            } catch (_: Exception) {
                emptyList()
            }

            QuizMovie(
                imdbId = movie.imdbId,
                title = movie.title,
                year = movie.year,
                posterPath = images.firstOrNull { it.type == "poster" }?.filePath,
                backdropPath = images.firstOrNull { it.type == "backdrop" }?.filePath,
                cast = cast
            )
        }
    }
}