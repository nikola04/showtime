package rs.edu.raf.showtime.profile.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.showtime.profile.ui.screen.ProfileScreen
import rs.edu.raf.showtime.profile.ui.screen.ProfileViewModel

fun NavGraphBuilder.profileGraph() {
    composable(ProfileRoutes.Profile.route) {
        val viewModel: ProfileViewModel = koinViewModel()
        ProfileScreen(viewModel)
    }
}
