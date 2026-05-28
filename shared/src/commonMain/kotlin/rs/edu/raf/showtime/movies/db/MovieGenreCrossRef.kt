package rs.edu.raf.showtime.movies.db

import androidx.room.Entity

@Entity(
    primaryKeys = ["movieId", "genreId"]
)
data class MovieGenreCrossRef(
    val movieId: String,
    val genreId: Int,
)