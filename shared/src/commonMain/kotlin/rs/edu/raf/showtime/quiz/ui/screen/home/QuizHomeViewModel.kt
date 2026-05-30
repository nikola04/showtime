package rs.edu.raf.showtime.quiz.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.showtime.quiz.data.QuizRepository
import rs.edu.raf.showtime.quiz.domain.QuizMovie

class QuizHomeViewModel(
    val repository: QuizRepository
) : ViewModel() {
    private val _state = MutableStateFlow(QuizHomeContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<QuizHomeContract.Effect>()
    val effect = _effect.receiveAsFlow()

    init {
        validateQuizPool()
    }

    fun onEvent(event: QuizHomeContract.Event) {
        when(event) {
            is QuizHomeContract.Event.StartQuizClicked -> {
                viewModelScope.launch {
                    _effect.send(QuizHomeContract.Effect.NavigateToQuiz)
                }
            }
        }
    }

    fun validateQuizPool() {
        viewModelScope.launch {
            val pool = repository.getQuizMoviesPool()
            _state.update {
                it.copy(
                    screenState = if (isValidQuizPool(pool)) {
                        QuizHomeContract.ScreenState.Success
                    } else QuizHomeContract.ScreenState.NotEnoughMovies
                )
            }
        }
    }

    private fun isValidQuizPool(pool: List<QuizMovie>): Boolean {
        return pool.count { it.posterPath != null } >= 10
    }
}