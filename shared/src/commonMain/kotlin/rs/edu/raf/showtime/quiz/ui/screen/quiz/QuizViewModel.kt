package rs.edu.raf.showtime.quiz.ui.screen.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.showtime.quiz.data.QuizGeneratorImpl

class QuizViewModel(
    private val quizGenerator: QuizGeneratorImpl
) : ViewModel() {

    private val _state = MutableStateFlow(QuizContract.State())
    val state = _state.asStateFlow()

    private var timerJob: Job? = null

    init {
        startQuiz()
    }

    private fun startQuiz() {
        viewModelScope.launch {
            val session = quizGenerator.generateQuiz()

            _state.update {
                it.copy(
                    session = session,
                    currentQuestion = session.questions.firstOrNull(),
                    currentIndex = 0,
                    timeLeft = 60
                )
            }

            startTimer()
        }
    }


    fun onEvent(event: QuizContract.Event) {
        when (event) {

            is QuizContract.Event.AnswerSelected -> handleAnswer(event.answer)

            QuizContract.Event.NextQuestion -> goNext()

            QuizContract.Event.Tick -> tick()

            QuizContract.Event.ExitClicked -> {
                _state.update { it.copy(showExitDialog = true) }
            }

            QuizContract.Event.ExitCancelled -> {
                _state.update { it.copy(showExitDialog = false) }
            }

            QuizContract.Event.ExitConfirmed -> {
                timerJob?.cancel()
                _state.update { it.copy(isFinished = true) }
            }

            QuizContract.Event.StartQuiz -> startQuiz()
        }
    }

    private fun handleAnswer(answer: String) {
        val current = _state.value.currentQuestion ?: return
        val alreadyAnswered = _state.value.isAnswered
        if (alreadyAnswered) return

        val isCorrect = answer == current.correctAnswer

        _state.update {
            it.copy(
                selectedAnswer = answer,
                isAnswered = true,
                correctCount = if (isCorrect) it.correctCount + 1 else it.correctCount,
                wrongCount = if (!isCorrect) it.wrongCount + 1 else it.wrongCount
            )
        }

        viewModelScope.launch {
            delay(800) // show correct/wrong highlight
            goNext()
        }
    }

    private fun goNext() {
        val state = _state.value
        val session = state.session ?: return

        val nextIndex = state.currentIndex + 1

        if (nextIndex >= session.questions.size || nextIndex >= 10) {
            finishQuiz()
            return
        }

        _state.update {
            it.copy(
                currentIndex = nextIndex,
                currentQuestion = session.questions[nextIndex],
                selectedAnswer = null,
                isAnswered = false
            )
        }
    }

    private fun tick() {
        val time = _state.value.timeLeft

        if (time <= 1) {
            finishQuiz()
            return
        }

        _state.update {
            it.copy(timeLeft = time - 1)
        }
    }

    private fun finishQuiz() {
        timerJob?.cancel()
        _state.update { it.copy(isFinished = true) }
    }

    // ---------------- TIMER ----------------

    private fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                onEvent(QuizContract.Event.Tick)
            }
        }
    }
}