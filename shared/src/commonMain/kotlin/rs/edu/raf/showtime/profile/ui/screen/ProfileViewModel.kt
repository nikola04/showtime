package rs.edu.raf.showtime.profile.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.edu.raf.showtime.core.auth.AuthStore
import rs.edu.raf.showtime.core.auth.UserSessionCleaner
import rs.edu.raf.showtime.favorites.data.FavoritesRepository
import rs.edu.raf.showtime.profile.data.ProfileRepository
import rs.edu.raf.showtime.quiz.data.QuizRepository
import rs.edu.raf.showtime.watchlist.data.WatchlistRepository

class ProfileViewModel(
    private val authStore: AuthStore,
    private val userSessionCleaner: UserSessionCleaner,
    private val profileRepository: ProfileRepository,
    private val watchlistRepository: WatchlistRepository,
    private val favoritesRepository: FavoritesRepository,
    private val quizRepository: QuizRepository
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
            is ProfileContract.Event.RetryClicked -> loadProfile()
        }
    }

    private fun observeStats() {
        viewModelScope.launch {
            watchlistRepository.observeWatchlistCount().collectLatest { count ->
                _state.update { it.copy(watchlistCount = count) }
            }
        }
        viewModelScope.launch {
            favoritesRepository.observeFavoriteCount().collectLatest { count ->
                _state.update { it.copy(favoriteCount = count) }
            }
        }
        viewModelScope.launch {
            quizRepository.observeBestQuizResult().collectLatest { bestResult ->
                _state.update { it.copy(bestQuizScore = bestResult?.score )}
            }
        }
        viewModelScope.launch {
            quizRepository.observeTotalQuizResults().collectLatest { total ->
                _state.update { it.copy(quizGamesPlayed = total) }
            }
        }
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
                    it.copy(screenState = ProfileContract.ScreenState.Error("Network request failed"))
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
