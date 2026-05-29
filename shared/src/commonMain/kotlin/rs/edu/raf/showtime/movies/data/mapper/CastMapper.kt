package rs.edu.raf.showtime.movies.data.mapper

import rs.edu.raf.showtime.movies.domain.CastMember
import rs.edu.raf.showtime.network.model.movies.CastMemberDTO

fun CastMemberDTO.toDomain(): CastMember {
    return CastMember(
        imdbId = imdbId,
        name = name,
        professions = professions,
        profilePath = profilePath,
        department = department
    )
}
