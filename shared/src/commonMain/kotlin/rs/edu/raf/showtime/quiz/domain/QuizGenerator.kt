package rs.edu.raf.showtime.quiz.domain

interface QuizGenerator {
    suspend fun generateQuiz(): QuizSession
}