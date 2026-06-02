package rs.edu.raf.showtime.quiz.ui.screen.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import rs.edu.raf.showtime.core.util.SystemBackHandler
import rs.edu.raf.showtime.quiz.ui.screen.quiz.components.Question


@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is QuizContract.Effect.NavigateBack -> navigateBack()
            }
        }
    }

    SystemBackHandler(
        enabled = state.screenState is QuizContract.ScreenState.Success && !state.showExitDialog
    ) {
        viewModel.onEvent(QuizContract.Event.ExitClicked)
    }

    if (state.showExitDialog) {
        ExitAlertDialog(
            onConfirm = { viewModel.onEvent(QuizContract.Event.ExitConfirmed) },
            onCancel = { viewModel.onEvent(QuizContract.Event.ExitCancelled) }
        )
    }

    Scaffold { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (val screenState = state.screenState) {
                is QuizContract.ScreenState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is QuizContract.ScreenState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Error creating quiz",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(screenState.message, style = MaterialTheme.typography.bodySmall)
                    }
                }

                is QuizContract.ScreenState.Finished -> {
                    QuizResultScreen(
                        result = screenState.result,
                        onPlayAgain = {
                            viewModel.onEvent(QuizContract.Event.StartQuiz)
                        }
                    )
                }


                is QuizContract.ScreenState.Success -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp)
                                .padding(top = padding.calculateTopPadding())
                        ) {

                            Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Question ${state.currentIndex + 1} of 10",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "${state.timeLeft}s",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(Modifier.height(12.dp))

                            LinearProgressIndicator(
                                progress = { (state.currentIndex + 1) / 10f },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(Modifier.height(24.dp))

                            AnimatedContent(
                                targetState = state.currentQuestion,
                                transitionSpec = {
                                    fadeIn(tween(150)) + slideInHorizontally(
                                        tween(150),
                                        initialOffsetX = { it }
                                    ) togetherWith (
                                            fadeOut(tween(150)) + slideOutHorizontally(
                                                tween(150),
                                                targetOffsetX = { -it }
                                            )
                                            )
                                },
                                label = "question_transition"
                            ) { question ->
                                if (question == null) return@AnimatedContent
                                Question(
                                    question = question,
                                    selectedAnswer = state.selectedAnswer,
                                    isAnswered = state.isAnswered,
                                    selectAnswer = { answer ->
                                        viewModel.onEvent(
                                            QuizContract.Event.AnswerSelected(
                                                answer
                                            )
                                        )
                                    }
                                )
                            }
                        }
                }
            }
        }
    }
}

@Composable
private fun ExitAlertDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Exit")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text("Cancel")
            }
        },
        title = { Text("Abandon quiz?") },
        text = { Text("Your progress will be lost.") }
    )
}
