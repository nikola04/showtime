package rs.edu.raf.showtime.auth.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.showtime.auth.ui.screen.login.LoginViewModel
import rs.edu.raf.showtime.auth.ui.screen.register.RegisterViewModel

val authModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}