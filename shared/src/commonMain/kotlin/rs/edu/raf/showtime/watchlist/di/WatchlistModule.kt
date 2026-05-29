package rs.edu.raf.showtime.watchlist.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.showtime.watchlist.data.WatchlistRepository
import rs.edu.raf.showtime.watchlist.ui.screen.WatchlistViewModel

val watchlistModule = module {
    singleOf(::WatchlistRepository)

    viewModelOf(::WatchlistViewModel)
}