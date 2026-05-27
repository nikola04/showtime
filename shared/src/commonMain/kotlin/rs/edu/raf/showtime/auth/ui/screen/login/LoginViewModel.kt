package rs.edu.raf.showtime.auth.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.showtime.auth.data.repository.AuthRepository
import rs.edu.raf.showtime.core.auth.AuthStore

class LoginViewModel(
    private val repository: AuthRepository,
    private val authStore: AuthStore,
)
    : ViewModel() {
    private val _state = MutableStateFlow(LoginContract.State())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginContract.Event) {
        when (event) {
            is LoginContract.Event.UsernameChanged -> {
                _state.update {
                    it.copy(
                        username = event.value,
                        screenState = LoginContract.ScreenState.Idle
                    )
                }
            }

            is LoginContract.Event.PasswordChanged -> {
                _state.update {
                    it.copy(
                        password = event.value,
                        screenState = LoginContract.ScreenState.Idle
                    )
                }
            }

            LoginContract.Event.LoginClicked -> {
                login()
            }
        }
    }

    private fun login() {
        _state.update { it.copy(hasSubmitted = true) }
        if (!_state.value.canSubmit) return

        viewModelScope.launch {
            _state.update { it.copy(screenState = LoginContract.ScreenState.Loading) }

            try {
                val currentState = _state.value
                val response = repository.login(
                    username = currentState.username.trim(),
                    password = currentState.password,
                )

                authStore.setAccessToken(response.accessToken)
                _state.update { it.copy(screenState = LoginContract.ScreenState.Success) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Napier.e("Login failed", e)
                _state.update {
                    it.copy(
                        screenState = LoginContract.ScreenState.Error(
                            message = e.toLoginMessage()
                        )
                    )
                }
            }
        }
    }

    private fun Exception.toLoginMessage(): String {
        return when {
            this is ClientRequestException && response.status == HttpStatusCode.Unauthorized ->
                "Invalid username or password"
            this is ClientRequestException ->
                "Login failed (${response.status.value})"
            else ->
                "Login failed. Check your connection and try again."
        }
    }
}
