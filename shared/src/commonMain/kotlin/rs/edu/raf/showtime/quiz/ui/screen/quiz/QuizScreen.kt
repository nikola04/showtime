package rs.edu.raf.showtime.quiz.ui.screen.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import rs.edu.raf.showtime.quiz.domain.QuestionType
import rs.edu.raf.showtime.quiz.domain.QuizQuestion
import rs.edu.raf.showtime.quiz.ui.screen.quiz.components.AnswerButton


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
                    val question = state.currentQuestion ?: return@Scaffold

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp)
                            .padding(top = padding.calculateTopPadding())
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    viewModel.onEvent(QuizContract.Event.ExitClicked)
                                }
                            ) {
                                Text("Back")
                            }
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
                        }

                        Spacer(Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { (state.currentIndex + 1) / 10f },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(24.dp))

                        Text(
                            text = questionTitle(question),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(16.dp))

                        question.movieTitle?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(Modifier.height(12.dp))
                        }

                        question.imageUrl?.let { url ->

                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/w780${url}",
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(Modifier.height(24.dp))
                        }

                        question.answers.forEach { answer ->
                            AnswerButton(
                                answer = answer,
                                correctAnswer = question.correctAnswer,
                                selectedAnswer = state.selectedAnswer,
                                isAnswered = state.isAnswered,
                                onClick = {
                                    viewModel.onEvent(
                                        QuizContract.Event.AnswerSelected(answer)
                                    )
                                }
                            )

                            Spacer(Modifier.height(12.dp))
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
        title = { Text("Exit quiz?") },
        text = { Text("Your progress will be lost.") }
    )
}

@Composable
private fun questionTitle(
    question: QuizQuestion
): String {
    return when (question.type) {
        QuestionType.GUESS_MOVIE ->
            "Which movie is shown?"

        QuestionType.GUESS_YEAR ->
            "In which year was this movie released?"

        QuestionType.GUESS_LEAD_ACTOR ->
            "Who is the lead actor?"
    }
}