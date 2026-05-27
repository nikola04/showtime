package rs.edu.raf.showtime.auth.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import rs.edu.raf.showtime.auth.ui.screen.login.LoginScreen
import rs.edu.raf.showtime.auth.ui.screen.register.RegisterScreen

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthRoutes.Login.route
    ) {
        authGraph(navController)
    }
}

fun NavGraphBuilder.authGraph(
    navController: NavController,
) {
    composable(AuthRoutes.Login.route) {
        LoginScreen(
            onRegisterClick = {
                navController.navigate(AuthRoutes.Register.route)
            }
        )
    }
    composable(AuthRoutes.Register.route) {
        RegisterScreen(
            onLoginClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                } else {
                    navController.navigate(AuthRoutes.Login.route)
                }
            }
        )
    }
}
