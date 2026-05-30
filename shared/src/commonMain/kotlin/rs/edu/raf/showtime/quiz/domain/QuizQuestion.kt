package rs.edu.raf.showtime.quiz.domain

data class QuizQuestion(
    val id: String,
    val type: QuestionType,
    val imageUrl: String?,
    val movieTitle: String?,
    val answers: List<String>,
    val correctAnswer: String
)