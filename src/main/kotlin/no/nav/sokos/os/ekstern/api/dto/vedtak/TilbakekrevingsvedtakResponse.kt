package no.nav.sokos.os.ekstern.api.dto.vedtak

import kotlinx.serialization.Serializable

@Serializable
data class TilbakekrevingsvedtakResponse(
    val status: Int,
    val melding: String,
    val vedtakId: Int,
    val datoVedtakFagsystem: String,
)
