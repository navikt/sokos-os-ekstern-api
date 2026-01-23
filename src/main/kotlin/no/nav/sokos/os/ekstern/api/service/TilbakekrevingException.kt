package no.nav.sokos.os.ekstern.api.service

import io.ktor.http.HttpStatusCode

class TilbakekrevingException(
    message: String,
    val statusCode: HttpStatusCode? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
