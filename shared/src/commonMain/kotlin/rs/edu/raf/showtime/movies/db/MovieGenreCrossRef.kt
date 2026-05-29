package rs.edu.raf.showtime.movies.db

import androidx.room.Entity

@Entity(
    primaryKeys = ["imdbId", "genreId"]
)
data class MovieGenreCrossRef(
    val imdbId: String,
    val genreId: Int,
)