package rs.edu.raf.showtime.quiz.ui.screen.quiz

import rs.edu.raf.showtime.quiz.domain.QuizQuestion
import rs.edu.raf.showtime.quiz.domain.QuizResult
import rs.edu.raf.showtime.quiz.domain.QuizSession

object QuizContract {

    sealed class ScreenState {
        data object Loading : ScreenState()
        data object Success : ScreenState()
        data class Error(val message: String) : ScreenState()
        data class Finished(val result: QuizResult) : ScreenState()
    }

    data class State(
        val screenState: ScreenState = ScreenState.Loading,
        val session: QuizSession? = null,

        val correctCount: Int = 0,
        val wrongCount: Int = 0,

        val currentIndex: Int = 0,
        val currentQuestion: QuizQuestion? = null,

        val selectedAnswer: String? = null,
        val isAnswered: Boolean = false,

        val timeLeft: Int = 60,

        val showExitDialog: Boolean = false
    )

    sealed class Event {
        data class AnswerSelected(val answer: String) : Event()
        data object ExitClicked : Event()
        data object ExitConfirmed : Event()
        data object ExitCancelled : Event()
        data object StartQuiz : Event()
    }

    sealed class Effect {
        data object NavigateBack : Effect()
    }
}