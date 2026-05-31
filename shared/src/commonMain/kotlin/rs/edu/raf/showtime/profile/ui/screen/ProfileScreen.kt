package rs.edu.raf.showtime.profile.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import rs.edu.raf.showtime.profile.ui.components.Heading
import rs.edu.raf.showtime.profile.ui.components.LibraryStats
import rs.edu.raf.showtime.profile.ui.components.ProfileDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Profile", fontWeight = FontWeight.Bold)
                }
            )
        }
    ) { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                val watchlistCount = state.watchlistCount
                val favoritesCount = state.favoriteCount
                val bestQuizScore = state.bestQuizScore
                val totalQuizGames = state.quizGamesPlayed

                when (val screenState = state.screenState) {
                    is ProfileContract.ScreenState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is ProfileContract.ScreenState.Error -> {
                        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally){
                            Text("Error loading profile", style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(8.dp))
                            Text(screenState.message, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = { viewModel.onEvent(ProfileContract.Event.RetryClicked)}) {
                                Text("Retry")
                            }
                        }
                    }
                is ProfileContract.ScreenState.Success -> {
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Heading("Details")
                            ProfileDetails(screenState.profile)
                            Heading("Library")
                            LibraryStats(
                                "Favorites:",
                                "In Watchlist:",
                                favoritesCount.toString(),
                                watchlistCount.toString()
                            )
                            Heading("Quiz")
                            LibraryStats(
                                "Best Score:",
                                "Total Games:",
                                bestQuizScore?.toString() ?: "N/A",
                                totalQuizGames.toString()
                            )
                        }

                        Button(
                            onClick = { viewModel.onEvent(ProfileContract.Event.Logout) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Logout")
                        }
                    }
                }
            }
        }
    }
}
