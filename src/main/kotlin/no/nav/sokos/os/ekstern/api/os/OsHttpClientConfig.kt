package no.nav.sokos.os.ekstern.api.os

import java.io.FileInputStream
import java.net.ProxySelector
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.apache5.Apache5
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import mu.KotlinLogging
import org.apache.hc.client5.http.impl.routing.SystemDefaultRoutePlanner

import no.nav.sokos.os.ekstern.api.config.PropertiesConfig
import no.nav.sokos.os.ekstern.api.config.jsonConfig

private val logger = KotlinLogging.logger {}

fun osHttpClient(osConfig: PropertiesConfig.OsConfiguration) =
    HttpClient(Apache5) {
        engine {
            sslContext = sslContext(osConfig)
            customizeClient {
                setRoutePlanner(SystemDefaultRoutePlanner(ProxySelector.getDefault()))
            }
        }
        install(ContentNegotiation) {
            json(jsonConfig)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 300000
            connectTimeoutMillis = 10000
            socketTimeoutMillis = 300000
        }
    }

private fun sslContext(osConfig: PropertiesConfig.OsConfiguration): SSLContext {
    val keyStore =
        KeyStore
            .getInstance(KeyStore.getDefaultType())
            .apply {
                val keyStoreFile = FileInputStream(osConfig.trustStore)
                val keyStorePassword = osConfig.trustStorePassword.toCharArray()
                load(keyStoreFile, keyStorePassword)
            }

    val trustManagerFactory =
        TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm())
            .apply { init(keyStore) }

    return SSLContext
        .getInstance("TLS")
        .apply { init(null, trustManagerFactory.trustManagers, null) }
}

suspend fun HttpResponse.errorMessage() = body<JsonElement>().jsonObject["errorMessage"]?.jsonPrimitive?.content

suspend fun HttpResponse.errorDetails() = body<JsonElement>().jsonObject["errorDetails"]?.jsonPrimitive?.content
