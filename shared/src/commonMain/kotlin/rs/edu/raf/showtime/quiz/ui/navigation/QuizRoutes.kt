package rs.edu.raf.showtime.quiz.ui.navigation

sealed class QuizRoutes(val route: String) {
    data object QuizHome : QuizRoutes("quiz")
    data object QuizPlay : QuizRoutes("quiz/play")
}
