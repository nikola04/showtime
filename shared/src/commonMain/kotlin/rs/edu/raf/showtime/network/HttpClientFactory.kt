package rs.edu.raf.showtime.network

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun createHttpClient(config: (HttpClientConfig<*>.() -> Unit)? = null): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }

            install(Logging) {
                logger = NapierKtorLogger
                level = LogLevel.INFO
            }

            config?.invoke(this)
        }
    }
}

private object NapierKtorLogger : Logger {
    override fun log(message: String) {
        Napier.d(message = message, tag = "HttpClient")
    }
}
