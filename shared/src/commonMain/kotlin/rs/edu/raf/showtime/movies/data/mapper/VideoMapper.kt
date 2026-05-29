package rs.edu.raf.showtime.movies.data.mapper

import rs.edu.raf.showtime.movies.domain.MovieVideo
import rs.edu.raf.showtime.network.model.movies.VideoDTO

fun VideoDTO.toDomain(): MovieVideo {
    return MovieVideo(
        key = key,
        site = site,
        name = name,
        type = type,
        publishedAt = publishedAt
    )
}
