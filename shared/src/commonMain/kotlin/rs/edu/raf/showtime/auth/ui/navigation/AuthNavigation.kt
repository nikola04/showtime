package rs.edu.raf.showtime.auth.ui.navigation

import androidx.compose.runtime.Composable
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
        authGraph()
    }
}

fun NavGraphBuilder.authGraph() {
    composable(AuthRoutes.Login.route) {
        LoginScreen()
    }
    composable(AuthRoutes.Register.route) {
        RegisterScreen()
    }
}

