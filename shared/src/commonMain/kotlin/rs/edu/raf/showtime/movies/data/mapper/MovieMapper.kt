package rs.edu.raf.showtime.movies.data.mapper

import rs.edu.raf.showtime.movies.db.CollectionEntity
import rs.edu.raf.showtime.movies.db.MovieDetailed
import rs.edu.raf.showtime.movies.db.MovieEntity
import rs.edu.raf.showtime.movies.db.MovieGenreCrossRef
import rs.edu.raf.showtime.movies.domain.Collection
import rs.edu.raf.showtime.movies.domain.Movie
import rs.edu.raf.showtime.movies.domain.MovieDetails
import rs.edu.raf.showtime.network.model.movies.Collection as CollectionDTO
import rs.edu.raf.showtime.network.model.movies.MovieDTO
import rs.edu.raf.showtime.network.model.movies.MovieMinDTO

fun MovieMinDTO.toMovieEntity(): MovieEntity {
    return MovieEntity(
        imdbId = imdbId,
        title = title,
        year = year,
        runtime = runtime,
        imdbRating = imdbRating?.toFloat(),
        imdbVotes = imdbVotes,
        posterPath = posterPath,
        budget = budget,
        revenue = revenue,
        languageCode = language,
        popularity = popularity?.toFloat(),

        hasDetails = false
    )
}

fun MovieDTO.toMovieEntity(): MovieEntity {
    return MovieEntity(
        imdbId = imdbId,
        title = title,
        originalTitle = originalTitle,
        overview = overview,
        tagline = tagline,
        releaseDate = releaseDate,
        year = year,
        runtime = runtime,
        budget = budget,
        revenue = revenue,
        languageCode = languageCode,
        popularity = popularity,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        tmdbRating = tmdbRating,
        tmdbVotes = tmdbVotes,
        posterPath = posterPath,
        backdropPath = backdropPath,
        homepage = homepage,
        collectionId = collection?.id,

        hasDetails = true
    )
}

fun CollectionDTO.toCollectionEntity(): CollectionEntity {
    return CollectionEntity(
        id = id,
        name = name,
        posterPath = posterPath,
        backdropPath = backdropPath
    )
}

fun MovieMinDTO.toMovieGenreCrossRefs(): List<MovieGenreCrossRef> {
    return genres.map { genre ->
        MovieGenreCrossRef(
            imdbId = imdbId,
            genreId = genre.id
        )
    }
}

fun MovieDTO.toMovieGenreCrossRefs(): List<MovieGenreCrossRef> {
    return genres.map { genre ->
        MovieGenreCrossRef(
            imdbId = imdbId,
            genreId = genre.id
        )
    }
}

fun MovieDetailed.toDomain(): MovieDetails {
    return MovieDetails(
        imdbId = movie.imdbId,
        title = movie.title,
        originalTitle = movie.originalTitle,
        overview = movie.overview,
        tagline = movie.tagline,
        releaseDate = movie.releaseDate,
        year = movie.year,
        runtime = movie.runtime,
        budget = movie.budget,
        revenue = movie.revenue,
        languageCode = movie.languageCode,
        popularity = movie.popularity,
        imdbRating = movie.imdbRating,
        imdbVotes = movie.imdbVotes,
        tmdbRating = movie.tmdbRating,
        tmdbVotes = movie.tmdbVotes,
        genres = genres.map { it.toDomain() },
        posterPath = movie.posterPath,
        backdropPath = movie.backdropPath,
        homepage = movie.homepage,
        collection = collection?.toDomain()
    )
}

fun MovieDetailed.asDomainMovie(): Movie {
    return Movie(
        imdbId = movie.imdbId,
        title = movie.title,
        year = movie.year,
        runtime = movie.runtime,
        imdbRating = movie.imdbRating,
        imdbVotes = movie.imdbVotes,
        posterPath = movie.posterPath,
        genres = genres.map { it.toDomain() },
        budget = movie.budget,
        revenue = movie.revenue,
        language = movie.languageCode,
        popularity = movie.popularity
    )
}

fun CollectionEntity.toDomain(): Collection {
    return Collection(
        id = id,
        name = name,
        posterPath = posterPath,
        backdropPath = backdropPath
    )
}

fun MovieMinDTO.toDomain(): Movie {
    return Movie(
        imdbId = imdbId,
        title = title,
        year = year,
        runtime = runtime,
        imdbRating = imdbRating?.toFloat(),
        imdbVotes = imdbVotes,
        posterPath = posterPath,
        genres = genres.map { it.toDomain() },
        budget = budget,
        revenue = revenue,
        language = language,
        popularity = popularity?.toFloat()
    )
}

fun MovieDTO.toDomain(): MovieDetails {
    return MovieDetails(
        imdbId = imdbId,
        title = title,
        originalTitle = originalTitle,
        overview = overview,
        tagline = tagline,
        releaseDate = releaseDate,
        year = year,
        runtime = runtime,
        budget = budget,
        revenue = revenue,
        languageCode = languageCode,
        popularity = popularity,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        tmdbRating = tmdbRating,
        tmdbVotes = tmdbVotes,
        genres = genres.map { it.toDomain() },
        posterPath = posterPath,
        backdropPath = backdropPath,
        homepage = homepage,
        collection = collection?.let {
            Collection(it.id, it.name, it.posterPath, it.backdropPath)
        }
    )
}
