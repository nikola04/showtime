package rs.edu.raf.showtime.auth.ui.screen.login

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.showtime.auth.ui.screen.components.FormButton
import rs.edu.raf.showtime.auth.ui.screen.components.FormInput

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
) {
    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }
        ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            // Header
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Welcome back to the app",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(48.dp))

            // Form Fields
            FormInput(
                label = "Username",
                value = state.username,
                onValueChanged = { value ->
                    viewModel.onEvent(LoginContract.Event.UsernameChanged(value))
                },
                placeholder = "john_doe",
                error = state.usernameError,
                enabled = !state.isLoading
            )
            Spacer(Modifier.height(16.dp))
            FormInput(
                label = "Password",
                value = state.password,
                onValueChanged = { value ->
                    viewModel.onEvent(LoginContract.Event.PasswordChanged(value))
                },
                placeholder = "********",
                error = state.passwordError,
                isPassword = true,
                enabled = !state.isLoading
            )

            Spacer(Modifier.height(32.dp))

            val loginError = state.screenState as? LoginContract.ScreenState.Error
            if (loginError != null) {
                Text(
                    text = loginError.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(12.dp))
            }

            // Login Button
            FormButton(
                text = if (state.isLoading) "Logging in..." else "Login",
                enabled = !state.isLoading,
                onClick = {
                    viewModel.onEvent(LoginContract.Event.LoginClicked)
                }
            )
            Spacer(Modifier.height(24.dp))
            // Create Account Button
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = onRegisterClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        "Create an account",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
