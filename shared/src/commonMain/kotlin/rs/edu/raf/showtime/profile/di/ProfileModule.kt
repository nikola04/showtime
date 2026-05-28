package rs.edu.raf.showtime.profile.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.showtime.profile.ui.screen.ProfileViewModel

val profileModule = module {
    viewModelOf(::ProfileViewModel)
}
