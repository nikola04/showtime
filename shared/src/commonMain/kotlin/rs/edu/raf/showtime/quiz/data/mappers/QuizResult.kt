package rs.edu.raf.showtime.quiz.data.mappers

import rs.edu.raf.showtime.quiz.db.QuizResultEntity
import rs.edu.raf.showtime.quiz.domain.QuizResult

fun QuizResult.toEntity() : QuizResultEntity {
    return QuizResultEntity(
        0L,
        correctAnswers,
        wrongAnswers,
        totalQuestions,
        score,
        durationSeconds
    )
}

fun QuizResultEntity.toDomain() : QuizResult {
    return QuizResult(
        correctAnswers,
        wrongAnswers,
        totalQuestions,
        score,
        durationSeconds
    )
}