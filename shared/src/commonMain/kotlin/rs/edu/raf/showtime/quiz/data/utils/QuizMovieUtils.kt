package rs.edu.raf.showtime.quiz.data.utils

import rs.edu.raf.showtime.movies.domain.Movie
import rs.edu.raf.showtime.quiz.domain.QuizMovie

object QuizMovieUtils {
    fun Movie.toQuizMovie(
        cast: List<String>
    ): QuizMovie {
        return QuizMovie(
            imdbId = imdbId,
            title = title,
            year = year,
            posterPath = posterPath,
            backdropPath = null,
            cast = cast
        )
    }
}