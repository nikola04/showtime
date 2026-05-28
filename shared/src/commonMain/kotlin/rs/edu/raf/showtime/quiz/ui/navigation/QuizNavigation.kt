package rs.edu.raf.showtime.quiz.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

object QuizRoutes {
    data object QuizHome {
        const val route = "quiz"
    }
}

fun NavGraphBuilder.quizGraph(
    navController: NavController,
) {
    composable(QuizRoutes.QuizHome.route) {
        QuizPlaceholderScreen()
    }
}

@Composable
private fun QuizPlaceholderScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Quiz",
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}
