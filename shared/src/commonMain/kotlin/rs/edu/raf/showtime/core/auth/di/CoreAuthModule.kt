package rs.edu.raf.showtime.core.auth.di

import androidx.datastore.core.DataStore
import org.koin.dsl.module
import rs.edu.raf.showtime.core.auth.AuthStore
import rs.edu.raf.showtime.core.auth.UserSessionCleaner
import rs.edu.raf.showtime.core.auth.createAuthDataStore
import rs.edu.raf.showtime.core.auth.model.AuthData

val coreAuthModule = module {
    single<DataStore<AuthData>> { createAuthDataStore() }
    single<AuthStore> { AuthStore(persistence = get()) }
    single<UserSessionCleaner> { UserSessionCleaner() }
}
