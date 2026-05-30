package rs.edu.raf.showtime.quiz.ui.screen.home

object QuizHomeContract {
    sealed class ScreenState {
        data object Loading : ScreenState()
        data object Success : ScreenState()
        data object NotEnoughMovies : ScreenState()
        data class Error(val message: String) : ScreenState()
    }

    data class State(
        val screenState: ScreenState = ScreenState.Loading,
        val availableMovies: Int = 0
    )

    sealed class Event {
        data object StartQuizClicked : Event()
    }

    sealed class Effect {
        data object NavigateToQuiz : Effect()
    }
}