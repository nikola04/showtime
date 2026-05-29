package rs.edu.raf.showtime.movies.ui.screen.moviedetails

import rs.edu.raf.showtime.movies.domain.CastMember
import rs.edu.raf.showtime.movies.domain.MovieDetails
import rs.edu.raf.showtime.movies.domain.MovieImage
import rs.edu.raf.showtime.movies.domain.MovieVideo

object MovieDetailsContract {
    sealed class ScreenState {
        data object Loading : ScreenState()
        data class Success(
            val movie: MovieDetails,
            val cast: List<CastMember> = emptyList(),
            val images: List<MovieImage> = emptyList(),
            val trailer: MovieVideo?
        ): ScreenState()
        data class Error(val message: String): ScreenState()
    }

    data class State(val screenState: ScreenState = ScreenState.Loading)

    sealed class Event {
        data class LoadMovie(val movieId: String) : Event()
        data object BackClicked : Event()
        data object RetryClicked : Event()
        data class OpenYoutube(val id: String): Event()
    }

    sealed class Effect {
        data object NavigateBack : Effect()
        data class OpenYoutube(val id: String): Effect()
    }
}
