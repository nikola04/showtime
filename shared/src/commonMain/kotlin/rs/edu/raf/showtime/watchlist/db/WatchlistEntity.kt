package rs.edu.raf.showtime.watchlist.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock

@Entity("watchlist")
data class WatchlistEntity(
    @PrimaryKey val movieId: String,
    val addedAt: Long = Clock.System.now().toEpochMilliseconds()
)