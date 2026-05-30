package rs.edu.raf.showtime.core.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import rs.edu.raf.showtime.favorites.db.FavoritesDao
import rs.edu.raf.showtime.favorites.db.FavoritesEntity
import rs.edu.raf.showtime.movies.db.CollectionEntity
import rs.edu.raf.showtime.movies.db.GenreEntity
import rs.edu.raf.showtime.movies.db.MovieDao
import rs.edu.raf.showtime.movies.db.MovieEntity
import rs.edu.raf.showtime.movies.db.MovieGenreCrossRef
import rs.edu.raf.showtime.movies.db.CastMemberEntity
import rs.edu.raf.showtime.movies.db.MovieImageEntity
import rs.edu.raf.showtime.movies.db.MovieVideoEntity
import rs.edu.raf.showtime.watchlist.db.WatchlistDao
import rs.edu.raf.showtime.watchlist.db.WatchlistEntity

@Database(
    entities = [
        MovieEntity::class,
        CollectionEntity::class,
        GenreEntity::class,
        MovieGenreCrossRef::class,
        CastMemberEntity::class,
        MovieImageEntity::class,
        MovieVideoEntity::class,
        WatchlistEntity::class,
        FavoritesEntity::class
               ],
    version = 2,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun watchlistDao(): WatchlistDao
    abstract fun favoritesDao(): FavoritesDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun buildAppDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}