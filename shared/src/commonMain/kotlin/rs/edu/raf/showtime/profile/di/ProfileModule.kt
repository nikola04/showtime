package rs.edu.raf.showtime.profile.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.showtime.profile.data.ProfileRepository
import rs.edu.raf.showtime.profile.ui.screen.ProfileViewModel

val profileModule = module {
    singleOf(::ProfileRepository)

    viewModelOf(::ProfileViewModel)
}
