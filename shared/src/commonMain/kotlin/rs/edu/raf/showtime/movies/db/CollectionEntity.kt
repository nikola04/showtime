package rs.edu.raf.showtime.movies.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("collections")
data class CollectionEntity (
    @PrimaryKey val id: Long,
    val name: String,
    val posterPath: String? = null,
    val backdropPath: String? = null,
)
