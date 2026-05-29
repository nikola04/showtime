package rs.edu.raf.showtime.movies.data.mapper

import rs.edu.raf.showtime.movies.db.CastMemberEntity
import rs.edu.raf.showtime.movies.domain.CastMember
import rs.edu.raf.showtime.network.model.movies.CastMemberDTO

fun CastMemberDTO.toEntity(movieImdbId: String): CastMemberEntity {
    return CastMemberEntity(
        imdbId = movieImdbId,
        personImdbId = imdbId,
        name = name,
        professions = professions,
        profilePath = profilePath,
        department = department
    )
}

fun CastMemberEntity.toDomain(): CastMember {
    return CastMember(
        imdbId = personImdbId,
        name = name,
        professions = professions,
        profilePath = profilePath,
        department = department
    )
}

fun CastMemberDTO.toDomain(): CastMember {
    return CastMember(
        imdbId = imdbId,
        name = name,
        professions = professions,
        profilePath = profilePath,
        department = department
    )
}
