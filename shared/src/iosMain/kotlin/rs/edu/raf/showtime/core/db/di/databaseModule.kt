package rs.edu.raf.showtime.core.db.di

import rs.edu.raf.showtime.core.db.AppDatabase
import rs.edu.raf.showtime.core.db.buildAppDatabase
import rs.edu.raf.showtime.core.db.getDatabaseBuilder
import org.koin.dsl.module

actual fun databaseModule() = module {
    single<AppDatabase> {
        buildAppDatabase(builder = getDatabaseBuilder())
    }
}
