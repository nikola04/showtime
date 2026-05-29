package rs.edu.raf.showtime.movies.domain

data class MovieImages(
    val backdrops: List<MovieImage>,
    val posters: List<MovieImage>,
    val logos: List<MovieImage>
)