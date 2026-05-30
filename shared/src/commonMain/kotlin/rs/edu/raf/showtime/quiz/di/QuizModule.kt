package rs.edu.raf.showtime.quiz.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.showtime.quiz.data.QuizGeneratorImpl
import rs.edu.raf.showtime.quiz.data.QuizRepository
import rs.edu.raf.showtime.quiz.ui.screen.home.QuizHomeViewModel

val quizModule = module {
    singleOf(::QuizRepository)
    singleOf(::QuizGeneratorImpl)

    viewModelOf(::QuizHomeViewModel)
}