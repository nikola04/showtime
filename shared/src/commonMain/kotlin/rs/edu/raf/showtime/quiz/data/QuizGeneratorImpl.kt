package rs.edu.raf.showtime.quiz.data

import rs.edu.raf.showtime.quiz.domain.*
import rs.edu.raf.showtime.quiz.data.generator.*

class QuizGeneratorImpl(
    private val repository: QuizRepository
) : QuizGenerator {

    private val generators: List<QuestionGenerator> = listOf(
        GuessMovieGenerator(),
        GuessYearGenerator(),
        GuessActorGenerator()
    )

    override suspend fun generateQuiz(): QuizSession {

        val movies: List<QuizMovie> = repository.getQuizMoviesPool()

        val used = mutableSetOf<String>()

        val questions = generators.flatMap { generator ->
            generator.generate(movies, limit = 4)
        }.filter {
            used.add(it.id) // prevent duplicates
        }

        if (questions.size < 10) {
            throw IllegalStateException("Not enough questions generated")
        }

        return QuizSession(
            questions = questions.shuffled().take(10)
        )
    }
}