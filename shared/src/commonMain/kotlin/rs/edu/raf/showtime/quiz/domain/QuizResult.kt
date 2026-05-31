package rs.edu.raf.showtime.quiz.domain

data class QuizResult(
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val totalQuestions: Int,
    val scorePercent: Int,
    val durationSeconds: Int
)