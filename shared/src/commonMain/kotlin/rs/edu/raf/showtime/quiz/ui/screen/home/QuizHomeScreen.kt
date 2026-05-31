package rs.edu.raf.showtime.quiz.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QuizHomeScreen(
    viewModel: QuizHomeViewModel,
    navigateToQuiz: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when(effect) {
                is QuizHomeContract.Effect.NavigateToQuiz -> navigateToQuiz()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Quiz", fontWeight = FontWeight.Bold)
                }
            )
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (val screenState = state.screenState) {
                is QuizHomeContract.ScreenState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is QuizHomeContract.ScreenState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Error loading quiz home",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(screenState.message, style = MaterialTheme.typography.bodySmall)
                    }
                }

                is QuizHomeContract.ScreenState.NotEnoughMovies -> {
                    val syncing = screenState.syncing

                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Not enough movies",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Browse the catalog first to populate your quiz pool.",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.onEvent(QuizHomeContract.Event.SyncMovies) },
                                enabled = !syncing
                            ) {
                                Text(text = (if(syncing) "Syncing" else "Sync"))
                            }
                        }
                    }
                }

                is QuizHomeContract.ScreenState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp, vertical = padding.calculateTopPadding()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Do you want to test your knowledge?", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            shape = RoundedCornerShape(8.dp),
                            onClick = { viewModel.onEvent(QuizHomeContract.Event.StartQuizClicked) }) {
                            Text("Start Quiz")
                        }
                    }
                }
            }
        }
    }
}
