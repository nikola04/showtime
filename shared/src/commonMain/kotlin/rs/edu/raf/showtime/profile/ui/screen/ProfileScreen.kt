package rs.edu.raf.showtime.profile.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.showtime.network.model.profile.UserDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(){
    val viewModel: ProfileViewModel = koinViewModel()
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 8.dp),
        ) {
            when (val screenState = state.screenState) {
                is ProfileContract.ScreenState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            ProfileDetails(screenState.profile)
                        }

                        Button(
                            onClick = { viewModel.logout() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Logout")
                        }
                    }
                }
                is ProfileContract.ScreenState.Loading -> {
                    Text("Loading", color = MaterialTheme.colorScheme.onBackground)
                }
                is ProfileContract.ScreenState.Error -> {
                    Text("Error", color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(screenState.message, color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun ProfileDetails(profile: UserDto) {
    Column(
        modifier = Modifier.padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Full Name:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f))
            Text(profile.fullName, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Username:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f))
            Text(profile.username, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}