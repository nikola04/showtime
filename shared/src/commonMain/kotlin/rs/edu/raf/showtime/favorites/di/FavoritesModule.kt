package rs.edu.raf.showtime.favorites.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.showtime.favorites.data.FavoritesRepository
import rs.edu.raf.showtime.favorites.ui.screen.FavoritesViewModel

val favoritesModule = module {
    singleOf(::FavoritesRepository)

    viewModelOf(::FavoritesViewModel)
}