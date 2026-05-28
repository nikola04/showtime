package rs.edu.raf.showtime.profile.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import rs.edu.raf.showtime.core.auth.AuthStore
import rs.edu.raf.showtime.core.auth.UserSessionCleaner

class ProfileViewModel(
    private val authStore: AuthStore,
    private val userSessionCleaner: UserSessionCleaner,
) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            userSessionCleaner.clearUserData()
            authStore.clearAuthData()
        }
    }
}
