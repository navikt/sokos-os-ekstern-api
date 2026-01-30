package no.nav.sokos.os.ekstern.api.api.models.vedtak

import kotlinx.serialization.Serializable

@Serializable
data class VedtakResponse(
    val status: Int,
    val melding: String,
    val vedtakId: Int,
    val datoVedtakFagsystem: String,
)
