package rs.edu.raf.showtime.quiz.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.showtime.quiz.ui.screen.home.QuizHomeScreen
import rs.edu.raf.showtime.quiz.ui.screen.home.QuizHomeViewModel

fun NavGraphBuilder.quizGraph(
    navController: NavController,
) {
    composable(QuizRoutes.QuizHome.route) {
        val viewModel: QuizHomeViewModel = koinViewModel()
        QuizHomeScreen(viewModel,
            navigateToQuiz = {
//                navController.navigate()
            })
    }
}
