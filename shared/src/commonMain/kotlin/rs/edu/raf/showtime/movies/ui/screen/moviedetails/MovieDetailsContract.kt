package rs.edu.raf.showtime.movies.ui.screen.moviedetails

import rs.edu.raf.showtime.network.model.movies.CastMemberDTO
import rs.edu.raf.showtime.network.model.movies.ImageDTO
import rs.edu.raf.showtime.network.model.movies.MovieDTO
import rs.edu.raf.showtime.network.model.movies.VideoDTO

object MovieDetailsContract {
    sealed class ScreenState {
        data object Loading : ScreenState()
        data class Success(
            val movie: MovieDTO,
            val cast: List<CastMemberDTO> = emptyList(),
            val images: List<ImageDTO> = emptyList(),
            val trailer: VideoDTO?
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