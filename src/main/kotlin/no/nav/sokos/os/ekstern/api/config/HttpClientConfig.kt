package no.nav.sokos.os.ekstern.api.config

import java.net.ProxySelector

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache5.Apache5
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.apache.hc.client5.http.impl.routing.SystemDefaultRoutePlanner

val httpClient =
    HttpClient(Apache5) {
        expectSuccess = false

        install(ContentNegotiation) {
            json(jsonConfig)
        }

        engine {
            customizeClient {
                setRoutePlanner(SystemDefaultRoutePlanner(ProxySelector.getDefault()))
            }
        }
    }
