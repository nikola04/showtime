package rs.edu.raf.showtime.quiz.domain

import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    suspend fun getQuizMoviesPool(): List<QuizMovie>

    suspend fun saveQuizResult(quizResult: QuizResult)

    fun observeBestQuizResult(): Flow<QuizResult?>

    fun observeTotalQuizResults(): Flow<Long>
}