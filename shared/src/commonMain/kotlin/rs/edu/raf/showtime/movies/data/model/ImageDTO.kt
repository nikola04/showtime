package rs.edu.raf.showtime.movies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    val backdrops: List<ImageDTO> = emptyList(),
    val posters: List<ImageDTO> = emptyList(),
    val logos: List<ImageDTO> = emptyList(),
)

@Serializable
data class ImageDTO(
    val filePath: String,
    val width: Int? = null,
    val height: Int? = null,
    val voteAverage: Float? = null,
    val language: String? = null,
)