package rs.edu.raf.showtime.quiz.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.edu.raf.showtime.core.db.AppDatabase
import rs.edu.raf.showtime.quiz.data.mappers.toDomain
import rs.edu.raf.showtime.quiz.data.mappers.toEntity
import rs.edu.raf.showtime.quiz.data.mappers.toQuizMovie
import rs.edu.raf.showtime.quiz.domain.QuizMovie
import rs.edu.raf.showtime.quiz.domain.QuizRepository
import rs.edu.raf.showtime.quiz.domain.QuizResult

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

            movie.toQuizMovie(cast)
        }
    }

    override suspend fun saveQuizResult(quizResult: QuizResult) {
        appDatabase.quizDao().insertQuizResult(quizResult.toEntity())
    }

    override fun observeBestQuizResult(): Flow<QuizResult?> {
        return appDatabase.quizDao().getBestQuizResult().map { it?.toDomain() }
    }

    override fun observeTotalQuizResults(): Flow<Long> {
        return appDatabase.quizDao().getTotalQuizResults()
    }
}