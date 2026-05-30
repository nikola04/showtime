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

            val cast = appDatabase.movieDao()
                .getCast(movie.imdbId)
                .map { it.name.trim() }
                .filter { it.isNotBlank() }
                .distinct()

            QuizMovie(
                imdbId = movie.imdbId,
                title = movie.title,
                year = movie.year,
                posterPath = movie.posterPath,
                backdropPath = movie.backdropPath,
                cast = cast
            )
        }
    }
}