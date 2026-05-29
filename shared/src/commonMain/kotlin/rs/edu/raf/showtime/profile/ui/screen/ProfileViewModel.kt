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
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileContract.State())
    val state = _state.asStateFlow()

    init {
        loadProfile()
        observeStats()
    }

    fun onEvent(action: ProfileContract.Event) {
        when (action) {
            is ProfileContract.Event.Logout -> logout()
        }
    }

    private fun observeStats() {
//        viewModelScope.launch {
//            movieRepository.observeFavoriteCount().collectLatest { count ->
//                _state.update { it.copy(favoriteCount = count) }
//            }
//        }
//        viewModelScope.launch {
//            movieRepository.observeWatchlistCount().collectLatest { count ->
//                _state.update { it.copy(watchlistCount = count) }
//            }
//        }
        // TODO: Observe quiz stats when implemented
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(screenState = ProfileContract.ScreenState.Loading) }
            try {
                val result = profileRepository.getProfile()
                _state.update {
                    it.copy(screenState = ProfileContract.ScreenState.Success(result))
                }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e
                Napier.e("Failed to load profile from API", e)
                _state.update {
                    it.copy(screenState = ProfileContract.ScreenState.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            userSessionCleaner.clearUserData()
            authStore.clearAuthData()
        }
    }
}
