package no.nav.sokos.os.ekstern.api.api.models.vedtak

import kotlinx.serialization.Serializable

@Serializable
data class VedtakRequest(
    val kodeAksjon: String,
    val vedtakId: Long,
    val vedtaksDato: String,
    val kodeHjemmel: String,
    val renterBeregnes: Boolean,
    val enhetAnsvarlig: String,
    val kontrollfelt: String,
    val saksbehandlerId: String,
    val datoTilleggsfrist: String? = null,
    val perioder: List<Periode>,
)

@Serializable
data class Periode(
    val periodeFom: String,
    val periodeTom: String,
    val renterPeriodeBeregnes: Boolean,
    val belopRenter: Double,
    val posteringer: List<Postering>,
)

@Serializable
data class Postering(
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
