package rs.edu.raf.showtime.quiz.data.utils

import rs.edu.raf.showtime.quiz.domain.QuizMovie

object QuizAnswerUtils {

    fun buildAnswers(
        correct: String,
        wrong: List<String>
    ): List<String> {
        return (wrong + correct).shuffled()
    }

    fun generateYearAnswers(year: Int): List<String> {
        val answers = mutableSetOf<Int>()
        answers += year

        while (answers.size < 4) {
            val offset = (-10..10).random()
            if (offset == 0) continue
            answers += year + offset
        }

        return answers.map { it.toString() }.shuffled()
    }

    fun pickWrongMovies(
        movies: List<QuizMovie>,
        excludeId: String,
        count: Int
    ): List<QuizMovie> {
        return movies
            .filter { it.imdbId != excludeId }
            .shuffled()
            .take(count)
    }

    fun pickWrongActors(
        movies: List<QuizMovie>,
        excludeId: String,
        correct: String,
        count: Int
    ): List<String> {
        return movies
            .filter { it.imdbId != excludeId }
            .flatMap { it.cast }
            .map(String::trim)
            .filter { it.isNotBlank() }
            .distinct()
            .filter { it != correct }
            .shuffled()
            .take(count)
    }
}