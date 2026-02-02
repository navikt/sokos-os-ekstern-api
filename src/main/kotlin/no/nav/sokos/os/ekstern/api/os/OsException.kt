package no.nav.sokos.os.ekstern.api.os

import no.nav.sokos.os.ekstern.api.config.ApiError

data class OsException(
    val apiError: ApiError,
) : Exception(apiError.error)
