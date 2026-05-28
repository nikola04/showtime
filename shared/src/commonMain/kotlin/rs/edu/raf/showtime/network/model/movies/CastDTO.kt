package rs.edu.raf.showtime.network.model.movies

import kotlinx.serialization.Serializable

@Serializable
data class CastResponse(
    val items: List<CastMemberDTO>,
    val totalItems: Int,
    val page: Int,
    val totalPages: Int,
)

@Serializable
data class CastMemberDTO(
    val imdbId: String,
    val name: String,
    val professions: String? = null,
    val profilePath: String? = null,
    val department: String? = null
)