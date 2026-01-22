package no.nav.sokos.os.ekstern.api.config

import kotlinx.serialization.json.Json

val jsonConfig =
    Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
    }
