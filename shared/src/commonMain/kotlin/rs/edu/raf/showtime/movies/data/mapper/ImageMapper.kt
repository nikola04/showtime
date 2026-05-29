package rs.edu.raf.showtime.movies.data.mapper

import rs.edu.raf.showtime.movies.domain.MovieImage
import rs.edu.raf.showtime.network.model.movies.ImageDTO

fun ImageDTO.toDomain(): MovieImage {
    return MovieImage(
        filePath = filePath,
        width = width,
        height = height,
        voteAverage = voteAverage,
        language = language
    )
}
