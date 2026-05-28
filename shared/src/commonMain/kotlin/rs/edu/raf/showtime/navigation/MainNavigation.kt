package rs.edu.raf.showtime.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import rs.edu.raf.showtime.favorites.ui.navigation.favoritesGraph
import rs.edu.raf.showtime.movies.ui.navigation.MoviesRoutes
import rs.edu.raf.showtime.movies.ui.navigation.moviesGraph
import rs.edu.raf.showtime.profile.ui.navigation.profileGraph
import rs.edu.raf.showtime.quiz.ui.navigation.quizGraph
import rs.edu.raf.showtime.watchlist.ui.navigation.watchlistGraph

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val homeRoute = MoviesRoutes.MovieList.route

    fun navigateToTopLevel(route: String) {
        val isHomeRoute = route == homeRoute

        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = !isHomeRoute
            }
            launchSingleTop = true
            restoreState = !isHomeRoute
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            MainBottomBar(
                currentDestination = currentDestination,
                onRouteClick = ::navigateToTopLevel
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = MoviesRoutes.MovieList.route,
            modifier = Modifier.padding(padding),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            moviesGraph(navController)
            favoritesGraph(navController)
            watchlistGraph(navController)
            quizGraph(navController)
            profileGraph(navController)
        }
    }
}
