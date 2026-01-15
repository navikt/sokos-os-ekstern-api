package no.nav.sokos.os.ekstern.api.zOs

import java.net.ProxySelector

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
import org.apache.http.impl.conn.SystemDefaultRoutePlanner

import no.nav.sokos.os.ekstern.api.config.PropertiesConfig
import no.nav.sokos.os.ekstern.api.zOs.entitet.annuler.OsKravgrunnlagAnnulerRequest
import no.nav.sokos.os.ekstern.api.zOs.entitet.detaljer.OsHentKravgrunnlagDetaljerRequest
import no.nav.sokos.os.ekstern.api.zOs.entitet.kravgrunnlag.OsHentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.zOs.entitet.vedtak.OsTilbakekrevingsvedtakRequest

fun osHttpClient(osConfig: PropertiesConfig.OsConfiguration) =
    HttpClient(Apache) {
        engine {
            // sslContext = sslContext(urConfig)
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

// TODO

/*private fun sslContext(urConfig: PropertiesConfig.Configuration): SSLContext {
    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        .apply {
            val keyStoreFile = FileInputStream(osConfig.trustStore)
            val keyStorePassword = osConfig.trustStorePassword.toCharArray()
            load(keyStoreFile, keyStorePassword)
        }

    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        .apply { init(keyStore) }

    return SSLContext.getInstance("TLS")
        .apply { init(null, trustManagerFactory.trustManagers, null) }
}*/

class OsHttpClient(
    private val osConfig: PropertiesConfig.OsConfiguration,
    private val httpClient: HttpClient = osHttpClient(osConfig),
) {
    private val baseUrl = osConfig.url

    suspend fun postTilbakekrevingsvedtak(request: OsTilbakekrevingsvedtakRequest): HttpResponse =
        httpClient.post("$baseUrl/tilbakekrevingsvedtak") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

    suspend fun postHentKravgrunnlag(request: OsHentKravgrunnlagRequest): HttpResponse =
        httpClient.post("$baseUrl/kravgrunnlagHentListe") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

    suspend fun postHentKravgrunnlagDetaljer(request: OsHentKravgrunnlagDetaljerRequest): HttpResponse =
        httpClient.post("$baseUrl/kravgrunnlagHentDetalj") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

    suspend fun postKravgrunnlagAnnuler(request: OsKravgrunnlagAnnulerRequest): HttpResponse =
        httpClient.post("$baseUrl/kravgrunnlagAnnuler") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
}
