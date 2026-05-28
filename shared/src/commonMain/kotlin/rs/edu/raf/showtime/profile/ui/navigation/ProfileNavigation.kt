package rs.edu.raf.showtime.profile.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.edu.raf.showtime.profile.ui.screen.ProfileScreen

fun NavGraphBuilder.profileGraph(
    navController: NavController,
) {
    composable(ProfileRoutes.Profile.route) {
        ProfileScreen(
            onBackClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            }
        )
    }
}
