package rs.edu.raf.showtime.quiz.data.mappers

import rs.edu.raf.showtime.movies.db.MovieEntity
import rs.edu.raf.showtime.quiz.domain.QuizMovie

fun MovieEntity.toQuizMovie(cast: List<String>): QuizMovie {
    return QuizMovie(
        imdbId = imdbId,
        title = title,
        year = year,
        posterPath = posterPath,
        backdropPath = backdropPath,
        cast = cast
    )
}