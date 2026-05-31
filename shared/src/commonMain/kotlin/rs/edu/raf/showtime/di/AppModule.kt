package rs.edu.raf.showtime.di

import org.koin.dsl.module
import rs.edu.raf.showtime.auth.di.authModule
import rs.edu.raf.showtime.core.auth.di.coreAuthModule
import rs.edu.raf.showtime.core.db.di.databaseModule
import rs.edu.raf.showtime.favorites.di.favoritesModule
import rs.edu.raf.showtime.movies.di.moviesModule
import rs.edu.raf.showtime.network.di.networkModule
import rs.edu.raf.showtime.profile.di.profileModule
import rs.edu.raf.showtime.quiz.di.quizModule
import rs.edu.raf.showtime.watchlist.di.watchlistModule

val appModule = module {
    includes(databaseModule(), coreAuthModule, authModule, networkModule, moviesModule, profileModule,
        watchlistModule,
        favoritesModule,
        quizModule
    )
}
