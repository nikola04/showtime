package rs.edu.raf.showtime.favorites.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock

@Entity("favorites")
data class FavoritesEntity(
    @PrimaryKey val movieId: String,
    val addedAt: Long = Clock.System.now().toEpochMilliseconds()
)