package rs.edu.raf.showtime.movies.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.showtime.movies.data.MovieRepository
import rs.edu.raf.showtime.movies.ui.screen.moviedetails.MovieDetailsViewModel
import rs.edu.raf.showtime.movies.ui.screen.movielist.MovieListViewModel
import rs.edu.raf.showtime.movies.ui.screen.movielistfilter.MovieListFiltersViewModel
import rs.edu.raf.showtime.movies.ui.state.FilterManager

val moviesModule = module {
    singleOf(::MovieRepository)
    singleOf(::FilterManager)
    viewModelOf(::MovieListViewModel)
    viewModelOf(::MovieListFiltersViewModel)
    viewModelOf(::MovieDetailsViewModel)
}
