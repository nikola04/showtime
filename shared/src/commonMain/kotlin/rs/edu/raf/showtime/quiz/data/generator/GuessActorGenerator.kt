package rs.edu.raf.showtime.quiz.data.generator

import rs.edu.raf.showtime.quiz.data.utils.QuizAnswerUtils
import rs.edu.raf.showtime.quiz.domain.QuestionType
import rs.edu.raf.showtime.quiz.domain.QuizMovie
import rs.edu.raf.showtime.quiz.domain.QuizQuestion

class GuessActorGenerator : QuestionGenerator {

    override fun generate(
        movies: List<QuizMovie>,
        limit: Int
    ): List<QuizQuestion> {

        val eligible = movies
            .filter { it.posterPath != null && it.cast.isNotEmpty() }
            .shuffled()

        val questions = mutableListOf<QuizQuestion>()

        for (movie in eligible) {
            if (questions.size >= limit) break

            val correct = movie.cast.firstOrNull() ?: continue

            val wrong = QuizAnswerUtils.pickWrongActors(
                movies = movies,
                excludeId = movie.imdbId,
                correct = correct,
                count = 3
            )

            if (wrong.size < 3) continue

            questions += QuizQuestion(
                id = movie.imdbId,
                type = QuestionType.GUESS_LEAD_ACTOR,
                correctAnswer = correct,
                answers = QuizAnswerUtils.buildAnswers(correct, wrong),
                imageUrl = movie.posterPath,
                movieTitle = movie.title
            )
        }

        return questions
    }
}