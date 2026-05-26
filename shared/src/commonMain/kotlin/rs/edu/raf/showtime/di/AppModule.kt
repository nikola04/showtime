package rs.edu.raf.showtime.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.showtime.data.api.MovieAPI
import rs.edu.raf.showtime.data.repository.MovieRepository
import org.koin.core.module.dsl.singleOf
import rs.edu.raf.showtime.data.api.createMovieAPI
import rs.edu.raf.showtime.ui.screen.moviedetails.MovieDetailsViewModel
import rs.edu.raf.showtime.ui.screen.movielist.MovieListViewModel
import rs.edu.raf.showtime.ui.screen.movielistfilter.MovieListFiltersViewModel
import rs.edu.raf.showtime.ui.state.FilterManager

val appModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

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
