package rs.edu.raf.showtime.quiz.ui.screen.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import rs.edu.raf.showtime.quiz.data.QuizGeneratorImpl
import rs.edu.raf.showtime.quiz.domain.QuizResult

class QuizViewModel(
    private val quizGenerator: QuizGeneratorImpl
) : ViewModel() {

    private val _state = MutableStateFlow(QuizContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<QuizContract.Effect>()
    val effect = _effect.receiveAsFlow()

    private var timerJob: Job? = null

    init {
        startQuiz()
    }

    private fun startQuiz() {
        viewModelScope.launch {
            timerJob?.cancel()

            _state.value = QuizContract.State(
                screenState = QuizContract.ScreenState.Loading
            )

            try {
                val session = quizGenerator.generateQuiz()

                _state.update {
                    it.copy(
                        screenState = QuizContract.ScreenState.Success,
                        session = session,
                        currentQuestion = session.questions.firstOrNull(),
                        currentIndex = 0,
                        timeLeft = 60
                    )
                }

                startTimer()
            } catch (e: Exception) {
                _state.update { it.copy(screenState = QuizContract.ScreenState.Error(e.message.toString())) }
            }
        }
    }

    fun onEvent(event: QuizContract.Event) {
        when (event) {

            is QuizContract.Event.AnswerSelected -> handleAnswer(event.answer)

            QuizContract.Event.ExitClicked -> {
                _state.update { it.copy(showExitDialog = true) }
            }

            QuizContract.Event.ExitCancelled -> {
                _state.update { it.copy(showExitDialog = false) }
            }

            QuizContract.Event.ExitConfirmed -> {
                viewModelScope.launch {
                    timerJob?.cancel()
                    _effect.send(QuizContract.Effect.NavigateBack)
                }
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
                correctCount = it.correctCount + if (isCorrect) 1 else 0,
                wrongCount = it.wrongCount + if (!isCorrect) 1 else 0
            )
        }

        viewModelScope.launch {
            delay(1000)
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

        val correct = _state.value.correctCount
        val wrong = _state.value.wrongCount
        val total = _state.value.session?.questions?.size ?: (correct + wrong)

        val result = QuizResult(
            correctAnswers = correct,
            wrongAnswers = wrong,
            totalQuestions = total,
            scorePercent = if (total > 0) correct * 100 / total else 0,
            durationSeconds = 60 - _state.value.timeLeft
        )

        _state.update {
            it.copy(
                screenState = QuizContract.ScreenState.Finished(result),
            )
        }
    }

    private fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                tick()
            }
        }
    }
}