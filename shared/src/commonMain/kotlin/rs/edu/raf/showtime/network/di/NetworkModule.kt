package rs.edu.raf.showtime.network.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.SetupRequest
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import org.koin.dsl.module
import rs.edu.raf.showtime.network.AuthAPI
import rs.edu.raf.showtime.core.auth.AuthStore
import rs.edu.raf.showtime.core.auth.UserSessionCleaner
import rs.edu.raf.showtime.core.auth.model.AuthState
import rs.edu.raf.showtime.network.HttpClientFactory
import rs.edu.raf.showtime.network.MovieAPI
import rs.edu.raf.showtime.network.ProfileAPI
import rs.edu.raf.showtime.network.createAuthAPI
import rs.edu.raf.showtime.network.createMovieAPI
import rs.edu.raf.showtime.network.createProfileAPI

val networkModule = module {
    single<MovieAPI> { get<Ktorfit>().createMovieAPI() }
    single<AuthAPI> { get<Ktorfit>().createAuthAPI() }
    single<ProfileAPI> { get<Ktorfit>(Qualifiers.Authenticated).createProfileAPI() }

    single<HttpClient> {
        HttpClientFactory.createHttpClient()
    }

    single {
        get<Ktorfit>(Qualifiers.Unauthenticated)
    }

    single<Ktorfit>(Qualifiers.Unauthenticated) {
        Ktorfit.Builder()
            .baseUrl("https://rma.finlab.rs/")
            .httpClient(get<HttpClient>())
            .build()
    }

    single<Ktorfit>(Qualifiers.Authenticated) {
        Ktorfit.Builder()
            .baseUrl("https://rma.finlab.rs/")
            .httpClient(get<HttpClient>(Qualifiers.Authenticated))
            .build()
    }

    single<HttpClient>(Qualifiers.Unauthenticated) {
        get<HttpClient>()
    }

    single<HttpClient>(Qualifiers.Authenticated) {
        val authStoreLazy: Lazy<AuthStore> = inject()
        val userSessionCleanerLazy: Lazy<UserSessionCleaner> = inject()
        HttpClientFactory.createHttpClient{
            installAuthPlugin(authStoreLazy, userSessionCleanerLazy)
        }
    }
}

private fun HttpClientConfig<*>.installAuthPlugin(
    authStoreLazy: Lazy<AuthStore>,
    userSessionCleanerLazy: Lazy<UserSessionCleaner>,
) = install(
    createClientPlugin("AuthPlugin") {
        on(SetupRequest) { request ->
            val authStore = authStoreLazy.value

            when (val authState = authStore.authState.value) {
                is AuthState.Authenticated -> {
                    request.header("Authorization", "Bearer ${authState.data.accessToken}")
                }
                AuthState.Unauthenticated -> Unit
                AuthState.Loading -> Unit
            }
        }

        on(Send) { request ->
            val originalCall = proceed(request)
            originalCall.response.run {
                if (status != HttpStatusCode.Unauthorized) {
                    return@run originalCall
                }

                userSessionCleanerLazy.value.clearUserData()
                authStoreLazy.value.clearAuthData()
                originalCall
            }
        }
    }
)
