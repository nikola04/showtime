package rs.edu.raf.showtime.auth.ui.screen.register

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

class RegisterViewModel(
    private val repository: AuthRepository,
    private val authStore: AuthStore,
)
    : ViewModel() {
    private val _state = MutableStateFlow(RegisterContract.State())
    val state = _state.asStateFlow()

    fun onEvent(event: RegisterContract.Event) {
        when (event) {
            is RegisterContract.Event.UsernameChanged -> {
                _state.update {
                    it.copy(
                        username = event.value,
                        screenState = RegisterContract.ScreenState.Idle
                    )
                }
            }

            is RegisterContract.Event.PasswordChanged -> {
                _state.update {
                    it.copy(
                        password = event.value,
                        screenState = RegisterContract.ScreenState.Idle
                    )
                }
            }

            is RegisterContract.Event.FullNameChanged -> {
                _state.update {
                    it.copy(
                        fullName = event.value,
                        screenState = RegisterContract.ScreenState.Idle
                    )
                }
            }

            RegisterContract.Event.RegisterClicked -> {
                register()
            }
        }
    }

    private fun register() {
        _state.update { it.copy(hasSubmitted = true) }
        if (!_state.value.canSubmit) return

        viewModelScope.launch {
            _state.update { it.copy(screenState = RegisterContract.ScreenState.Loading) }

            try {
                val currentState = _state.value
                val response = repository.signup(
                    fullName = currentState.fullName.trim(),
                    username = currentState.username.trim(),
                    password = currentState.password,
                )

                authStore.setAccessToken(response.accessToken)
                _state.update { it.copy(screenState = RegisterContract.ScreenState.Success) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Napier.e("Signup failed", e)
                _state.update {
                    it.copy(
                        screenState = RegisterContract.ScreenState.Error(
                            error = e.toRegisterMessage()
                        )
                    )
                }
            }
        }
    }

    private fun Exception.toRegisterMessage(): String {
        return when {
            this is ClientRequestException && response.status == HttpStatusCode.Conflict ->
                "Username is already taken"
            this is ClientRequestException && response.status == HttpStatusCode.BadRequest ->
                "Please check the form fields"
            this is ClientRequestException ->
                "Registration failed (${response.status.value})"
            else ->
                "Registration failed. Check your connection and try again."
        }
    }
}
