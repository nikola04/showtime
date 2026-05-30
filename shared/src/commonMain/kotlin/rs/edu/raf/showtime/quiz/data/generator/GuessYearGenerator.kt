package rs.edu.raf.showtime.quiz.data.generator

import rs.edu.raf.showtime.quiz.data.utils.QuizAnswerUtils
import rs.edu.raf.showtime.quiz.domain.*

class GuessYearGenerator : QuestionGenerator {

    override fun generate(
        movies: List<QuizMovie>,
        limit: Int
    ): List<QuizQuestion> {

        val eligible = movies
            .filter { it.posterPath != null && it.year != null }
            .shuffled()

        return eligible.take(limit).map { movie ->

            val year = movie.year!!

            QuizQuestion(
                id = movie.imdbId,
                type = QuestionType.GUESS_YEAR,
                correctAnswer = year.toString(),
                answers = QuizAnswerUtils.generateYearAnswers(year),
                imageUrl = movie.posterPath,
                movieTitle = movie.title
            )
        }
    }
}