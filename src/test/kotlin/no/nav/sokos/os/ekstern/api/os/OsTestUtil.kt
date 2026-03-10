package no.nav.sokos.os.ekstern.api.os

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache5.Apache5
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

import no.nav.sokos.os.ekstern.api.config.jsonConfig

internal val testHttpClient =
    HttpClient(Apache5) {
        install(ContentNegotiation) {
            json(jsonConfig)
        }
    }
