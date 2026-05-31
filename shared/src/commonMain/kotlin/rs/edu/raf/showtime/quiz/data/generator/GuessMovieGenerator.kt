package rs.edu.raf.showtime.quiz.data.generator

import rs.edu.raf.showtime.quiz.domain.*
import rs.edu.raf.showtime.quiz.data.utils.QuizAnswerUtils

class GuessMovieGenerator : QuestionGenerator {

    override fun generate(
        movies: List<QuizMovie>,
        limit: Int
    ): List<QuizQuestion> {

        val eligible = movies
            .filter { it.posterPath != null }
            .shuffled()

        return eligible.take(limit).map { movie ->

            val wrong = QuizAnswerUtils.pickWrongMovies(
                movies = movies,
                excludeId = movie.imdbId,
                count = 3
            ).map { it.title }

            QuizQuestion(
                id = movie.imdbId,
                type = QuestionType.GUESS_MOVIE,
                correctAnswer = movie.title,
                answers = QuizAnswerUtils.buildAnswers(movie.title, wrong),
                imageUrl = movie.posterPath,
                movieTitle = null
            )
        }
    }
}