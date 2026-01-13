package no.nav.sokos.os.ekstern.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class TilbakekrevingsvedtakRequest(
    val kodeAksjon: String,
    val vedtakId: Long,
    val vedtaksDato: String,
    val kodeHjemmel: String,
    val renterBeregnes: Boolean,
    val enhetAnsvarlig: String,
    val kontrollfelt: String,
    val saksbehandlerId: String,
    val datoTilleggsfrist: String? = null,
    val perioder: List<VedtaksPeriode>,
)

@Serializable
data class VedtaksPeriode(
    val periodeFom: String,
    val periodeTom: String,
    val renterPeriodeBeregnes: Boolean,
    val belopRenter: Double,
    val posteringer: List<VedtaksPostering>,
)

@Serializable
data class VedtaksPostering(
    val kodeKlasse: String,
    val belopOpprinneligUtbetalt: Double,
    val belopNy: Double,
    val belopTilbakekreves: Double,
    val belopUinnkrevd: Double,
    val belopSkatt: Double,
    val kodeResultat: String,
    val kodeAarsak: String,
    val kodeSkyld: String,
)

@Serializable
data class TilbakekrevingsvedtakResponse(
    val status: Int,
    val melding: String,
    val vedtakId: Long,
    val datoVedtakFagsystem: String,
)
