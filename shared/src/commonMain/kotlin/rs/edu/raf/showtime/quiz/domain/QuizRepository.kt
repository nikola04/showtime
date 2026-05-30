package rs.edu.raf.showtime.quiz.domain

interface QuizRepository {
    suspend fun getQuizMoviesPool(): List<QuizMovie>

//    suspend fun saveQuizAttempt(
//        attempt: QuizAttempt
//    )

//    suspend fun getQuizAttempts(): List<QuizAttempt>
}