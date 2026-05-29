package rs.edu.raf.showtime.movies.data.mapper

import rs.edu.raf.showtime.movies.db.MovieVideoEntity
import rs.edu.raf.showtime.movies.domain.MovieVideo
import rs.edu.raf.showtime.network.model.movies.VideoDTO

fun VideoDTO.toEntity(movieImdbId: String): MovieVideoEntity {
    return MovieVideoEntity(
        key = key,
        imdbId = movieImdbId,
        site = site,
        name = name,
        type = type,
        publishedAt = publishedAt
    )
}

fun MovieVideoEntity.toDomain(): MovieVideo {
    return MovieVideo(
        key = key,
        site = site,
        name = name,
        type = type,
        publishedAt = publishedAt
    )
}

fun VideoDTO.toDomain(): MovieVideo {
    return MovieVideo(
        key = key,
        site = site,
        name = name,
        type = type,
        publishedAt = publishedAt
    )
}
