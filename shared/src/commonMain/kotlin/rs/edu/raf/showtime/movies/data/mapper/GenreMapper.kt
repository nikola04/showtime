package rs.edu.raf.showtime.movies.data.mapper

import rs.edu.raf.showtime.movies.db.GenreEntity
import rs.edu.raf.showtime.movies.domain.Genre
import rs.edu.raf.showtime.network.model.movies.GenreDTO

fun GenreDTO.toGenreEntity(): GenreEntity {
    return GenreEntity(
        id = id,
        name = name
    )
}

fun GenreEntity.toDomain(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

fun GenreDTO.toDomain(): Genre {
    return Genre(
        id = id,
        name = name
    )
}
