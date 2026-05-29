package rs.edu.raf.showtime.movies.data.mapper

import rs.edu.raf.showtime.movies.db.MovieImageEntity
import rs.edu.raf.showtime.movies.domain.MovieImage
import rs.edu.raf.showtime.network.model.movies.ImageDTO

fun ImageDTO.toEntity(movieImdbId: String, type: String): MovieImageEntity {
    return MovieImageEntity(
        imdbId = movieImdbId,
        filePath = filePath,
        type = type,
        width = width,
        height = height,
        voteAverage = voteAverage,
        language = language
    )
}

fun MovieImageEntity.toDomain(): MovieImage {
    return MovieImage(
        filePath = filePath,
        width = width,
        height = height,
        voteAverage = voteAverage,
        language = language
    )
}

fun ImageDTO.toDomain(): MovieImage {
    return MovieImage(
        filePath = filePath,
        width = width,
        height = height,
        voteAverage = voteAverage,
        language = language
    )
}
