package rs.edu.raf.showtime.di

import org.koin.dsl.module
import rs.edu.raf.showtime.auth.di.authModule
import rs.edu.raf.showtime.core.auth.di.coreAuthModule
import rs.edu.raf.showtime.movies.di.moviesModule
import rs.edu.raf.showtime.network.di.networkModule

val appModule = module {
    includes(coreAuthModule, authModule, networkModule, moviesModule)
}
