package rs.edu.raf.showtime.auth.di

import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.showtime.auth.data.api.AuthAPI
import rs.edu.raf.showtime.auth.data.api.createAuthAPI
import rs.edu.raf.showtime.auth.data.repository.AuthRepository
import rs.edu.raf.showtime.auth.ui.screen.login.LoginViewModel
import rs.edu.raf.showtime.auth.ui.screen.register.RegisterViewModel

val authModule = module {
    single<AuthAPI> { get<Ktorfit>().createAuthAPI() }
    singleOf(::AuthRepository)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}
