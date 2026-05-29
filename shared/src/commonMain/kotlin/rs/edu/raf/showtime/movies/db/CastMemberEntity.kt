package rs.edu.raf.showtime.movies.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cast_members",
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
data class CastMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imdbId: String,
    val personImdbId: String,
    val name: String,
    val professions: String? = null,
    val profilePath: String? = null,
    val department: String? = null
)
