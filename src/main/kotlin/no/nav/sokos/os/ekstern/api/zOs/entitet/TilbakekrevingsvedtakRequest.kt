package no.nav.sokos.os.ekstern.api.zOs.entitet

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TilbakekrevingsvedtakRequest(
    @SerialName("OsTilbakekrevingsvedtakOperation")
    val operation: TilbakekrevingsvedtakRequestOperation,
)

@Serializable
data class TilbakekrevingsvedtakRequestOperation(
    @SerialName("ZT1ICont")
    val container: TilbakekrevingsvedtakRequestContainer,
)

@Serializable
data class TilbakekrevingsvedtakRequestContainer(
    @SerialName("RequestTilbakekrevingsvedtak")
    val request: TilbakekrevingsvedtakRequestData,
)

@Serializable
data class TilbakekrevingsvedtakRequestData(
    @SerialName("KodeAksjon")
    val kodeAksjon: String,
    @SerialName("VedtakId")
    val vedtakId: Int,
    @SerialName("DatoVedtakFagsystem")
    val datoVedtakFagsystem: String,
    @SerialName("KodeHjemmel")
    val kodeHjemmel: String,
    @SerialName("RenterBeregnes")
    val renterBeregnes: Boolean,
    @SerialName("EnhetAnsvarlig")
    val enhetAnsvarlig: String,
    @SerialName("Kontrollfelt")
    val kontrollfelt: String,
    @SerialName("SaksbehandlerId")
    val saksbehandlerId: String,
    @SerialName("DatoTilleggsfrist")
    val datoTilleggsfrist: String? = null,
    @SerialName("Tilbakekrevingsperiode")
    val tilbakekrevingsperiode: List<Tilbakekrevingsperiode>,
)

@Serializable
data class Tilbakekrevingsperiode(
    @SerialName("DatoPeriodeFom")
    val datoPeriodeFom: String,
    @SerialName("DatoPeriodeTom")
    val datoPeriodeTom: String,
    @SerialName("RenterPeriodeBeregnes")
    val renterPeriodeBeregnes: Boolean,
    @SerialName("BelopRenter")
    val belopRenter: Double,
    @SerialName("Tilbakerevingsbelop")
    val tilbakerevingsbelop: List<Tilbakerevingsbelop>,
)

@Serializable
data class Tilbakerevingsbelop(
    @SerialName("KodeKlasse")
    val kodeKlasse: String,
    @SerialName("BelopOpprinneligUtbetalt")
    val belopOpprinneligUtbetalt: Double,
    @SerialName("BelopNy")
    val belopNy: Double,
    @SerialName("BelopTilbakekreves")
    val belopTilbakekreves: Double,
    @SerialName("BelopUinnkrevd")
    val belopUinnkrevd: Double,
    @SerialName("BelopSkatt")
    val belopSkatt: Double,
    @SerialName("KodeResultat")
    val kodeResultat: String,
    @SerialName("KodeAarsak")
    val kodeAarsak: String,
    @SerialName("KodeSkyld")
    val kodeSkyld: String,
)
