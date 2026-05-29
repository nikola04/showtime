package rs.edu.raf.showtime.movies.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_images",
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
data class MovieImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imdbId: String,
    val filePath: String,
    val type: String, // "backdrop", "poster", "logo"
    val width: Int? = null,
    val height: Int? = null,
    val voteAverage: Float? = null,
    val language: String? = null
)
