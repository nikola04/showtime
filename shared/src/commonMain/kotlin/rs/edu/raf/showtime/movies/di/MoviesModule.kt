package rs.edu.raf.showtime.movies.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.showtime.movies.data.api.MovieAPI
import rs.edu.raf.showtime.movies.data.api.createMovieAPI
import rs.edu.raf.showtime.movies.data.repository.MovieRepository
import rs.edu.raf.showtime.movies.ui.screen.moviedetails.MovieDetailsViewModel
import rs.edu.raf.showtime.movies.ui.screen.movielist.MovieListViewModel
import rs.edu.raf.showtime.movies.ui.screen.movielistfilter.MovieListFiltersViewModel
import rs.edu.raf.showtime.movies.ui.state.FilterManager

val moviesModule = module {
    single {
        Ktorfit.Builder()
            .baseUrl("https://rma.finlab.rs/")
            .httpClient(get<HttpClient>())
            .build()
    }

    single<MovieAPI> { get<Ktorfit>().createMovieAPI() }

    singleOf(::MovieRepository)
    singleOf(::FilterManager)
    viewModelOf(::MovieListViewModel)
    viewModelOf(::MovieListFiltersViewModel)
    viewModelOf(::MovieDetailsViewModel)
}
