package rs.edu.raf.showtime.quiz.data

import rs.edu.raf.showtime.quiz.domain.*
import rs.edu.raf.showtime.quiz.data.generator.*

class QuizGeneratorImpl(
    private val repository: QuizRepository
) : QuizGenerator {

    private val movieKnowledgeGenerators: List<QuestionGenerator> = listOf(
        GuessMovieGenerator(),
        GuessYearGenerator(),
        GuessActorGenerator()
    )

    private fun getGenerators(category: QuizCategory): List<QuestionGenerator> {
        return when(category) {
            QuizCategory.MovieKnowledge -> movieKnowledgeGenerators
        }
    }

    override suspend fun generateQuiz(category: QuizCategory): QuizSession {
        val movies: List<QuizMovie> = repository.getQuizMoviesPool()
        val generators: List<QuestionGenerator> = getGenerators(category)

        val questions = generators.flatMap { generator ->
            generator.generate(movies, limit = 4)
        }

        if (questions.size < 10) {
            throw IllegalStateException("Not enough questions generated")
        }

        return QuizSession(
            questions = questions.shuffled().take(10)
        )
    }
}