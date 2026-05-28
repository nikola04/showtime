package rs.edu.raf.showtime.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.ui.graphics.vector.ImageVector
import rs.edu.raf.showtime.favorites.ui.navigation.FavoriteRoutes
import rs.edu.raf.showtime.movies.ui.navigation.MoviesRoutes
import rs.edu.raf.showtime.profile.ui.navigation.ProfileRoutes
import rs.edu.raf.showtime.quiz.ui.navigation.QuizRoutes
import rs.edu.raf.showtime.watchlist.ui.navigation.WatchlistRoutes

data class MainBottomRoute(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

val mainBottomRoutes = listOf(
    MainBottomRoute(
        route = FavoriteRoutes.Favorites.route,
        label = "Favorite",
        icon = Icons.Default.Favorite,
    ),
    MainBottomRoute(
        route = WatchlistRoutes.Watchlist.route,
        label = "Watchlist",
        icon = Icons.Default.Bookmark,
    ),
    MainBottomRoute(
        route = MoviesRoutes.MovieList.route,
        label = "Home",
        icon = Icons.Default.Movie,
    ),
    MainBottomRoute(
        route = QuizRoutes.QuizHome.route,
        label = "Quiz",
        icon = Icons.Default.Quiz,
    ),
    MainBottomRoute(
        route = ProfileRoutes.Profile.route,
        label = "Profile",
        icon = Icons.Default.Person,
    ),
)
