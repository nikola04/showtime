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
import rs.edu.raf.showtime.core.auth.AuthStore
import rs.edu.raf.showtime.core.auth.model.AuthState
import rs.edu.raf.showtime.network.HttpClientFactory

val networkModule = module {
    single<HttpClient> {
        HttpClientFactory.createHttpClient()
    }

    single {
        Ktorfit.Builder()
            .baseUrl("https://rma.finlab.rs/")
            .httpClient(get<HttpClient>())
            .build()
    }

    single<HttpClient>(Qualifiers.Unauthenticated) {
        get<HttpClient>()
    }

    single<HttpClient>(Qualifiers.Authenticated) {
        val authStoreLazy: Lazy<AuthStore> = inject()
        HttpClientFactory.createHttpClient{
            installAuthPlugin(authStoreLazy)
        }
    }
}

private fun HttpClientConfig<*>.installAuthPlugin(authStoreLazy: Lazy<AuthStore>) = install(
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

                // ignore even if 401 for now...
                originalCall
            }
        }
    }
)
