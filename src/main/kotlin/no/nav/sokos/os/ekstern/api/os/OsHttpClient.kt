package no.nav.sokos.os.ekstern.api.os

import java.io.FileInputStream
import java.net.ProxySelector
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

import kotlinx.serialization.json.Json

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import mu.KotlinLogging
import org.apache.http.impl.conn.SystemDefaultRoutePlanner

import no.nav.sokos.os.ekstern.api.config.PropertiesConfig

private val logger = KotlinLogging.logger {}

fun osHttpClient(osConfig: PropertiesConfig.OsConfiguration) =
    HttpClient(Apache) {
        engine {
            sslContext = sslContext(osConfig)
            customizeClient {
                setRoutePlanner(SystemDefaultRoutePlanner(ProxySelector.getDefault()))
            }
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    explicitNulls = false
                },
            )
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

class OsHttpClient(
    private val osConfig: PropertiesConfig.OsConfiguration,
    private val httpClient: HttpClient = osHttpClient(osConfig),
) {
    private val baseUrl = osConfig.endpointUrl

    init {
        logger.info { "OsHttpClient initialized with baseUrl: $baseUrl" }
    }

    suspend fun postTilbakekrevingsvedtak(request: PostOsTilbakekrevingsvedtakRequest): HttpResponse = post("tilbakekrevingsvedtak", request)

    suspend fun postHentKravgrunnlag(request: PostOsHentKravgrunnlagRequest): HttpResponse = post("kravgrunnlagHentListe", request)

    suspend fun postHentKravgrunnlagDetaljer(request: PostOsHentKravgrunnlagDetaljerRequest): HttpResponse = post("kravgrunnlagHentDetalj", request)

    suspend fun postKravgrunnlagAnnuler(request: PostOsKravgrunnlagAnnulerRequest): HttpResponse = post("kravgrunnlagAnnuler", request)

    private suspend fun post(
        path: String,
        request: Any,
    ): HttpResponse {
        val fullUrl = "$baseUrl/$path"
        logger.info { "Calling OS API: $fullUrl" }
        logger.info { "Request: $request" }
        return httpClient
            .post(fullUrl) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.also { response ->
                logger.info { "OS API response status: ${response.status}" }
            }
    }
}
