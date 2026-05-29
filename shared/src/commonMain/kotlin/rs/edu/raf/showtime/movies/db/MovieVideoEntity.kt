package rs.edu.raf.showtime.movies.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_videos",
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["imdbId"],
            childColumns = ["imdbId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["imdbId"])]
)
data class MovieVideoEntity(
    @PrimaryKey val key: String,
    val imdbId: String,
    val site: String,
    val name: String? = null,
    val type: String? = null,
    val publishedAt: String? = null
)
