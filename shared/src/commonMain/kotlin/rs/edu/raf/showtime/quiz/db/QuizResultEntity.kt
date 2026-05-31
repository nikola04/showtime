package rs.edu.raf.showtime.quiz.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("quizResults")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val totalQuestions: Int,
    val score: Double,
    val durationSeconds: Int
)