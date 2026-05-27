package rs.edu.raf.showtime.di

import org.koin.dsl.module
import rs.edu.raf.showtime.movies.di.moviesModule
import rs.edu.raf.showtime.network.di.networkModule

val appModule = module {
    includes(networkModule, moviesModule)
}
