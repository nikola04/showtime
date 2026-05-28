package rs.edu.raf.showtime.profile.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.showtime.core.auth.AuthStore
import rs.edu.raf.showtime.core.auth.UserSessionCleaner
import rs.edu.raf.showtime.profile.data.ProfileRepository

class ProfileViewModel(
    private val authStore: AuthStore,
    private val userSessionCleaner: UserSessionCleaner,
    private val repository: ProfileRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileContract.State())
    val state = _state.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(screenState = ProfileContract.ScreenState.Loading) }
            try {
                val result = repository.getProfile()
                _state.update {
                    it.copy(screenState = ProfileContract.ScreenState.Success(result))
                }
            } catch (e: Exception) {
                Napier.e("Failed to load profile from API", e)
                _state.update {
                    it.copy(screenState = ProfileContract.ScreenState.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            userSessionCleaner.clearUserData()
            authStore.clearAuthData()
        }
    }
}
