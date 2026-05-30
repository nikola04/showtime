package rs.edu.raf.showtime.quiz.data.generator

import rs.edu.raf.showtime.quiz.domain.QuizMovie
import rs.edu.raf.showtime.quiz.domain.QuizQuestion

interface QuestionGenerator {
    fun generate(
        movies: List<QuizMovie>,
        limit: Int
    ): List<QuizQuestion>
}