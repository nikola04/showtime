package rs.edu.raf.showtime.quiz.domain

data class QuizMovie(
    val imdbId: String,
    val title: String,
    val year: Int?,
    val posterPath: String?,
    val backdropPath: String?,
    val cast: List<String>
)