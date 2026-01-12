package no.nav.sokos.os.ekstern.api.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TilbakekrevingsvedtakRequest(
    @SerialName("OsTilbakekrevingsvedtakOperation")
    val operation: TilbakekrevingsvedtakOperation,
)

@Serializable
data class TilbakekrevingsvedtakOperation(
    @SerialName("ZT1ICont")
    val container: TilbakekrevingsvedtakContainer,
)

@Serializable
data class TilbakekrevingsvedtakContainer(
    @SerialName("RequestTilbakekrevingsvedtak")
    val request: RequestTilbakekrevingsvedtak,
)

@Serializable
data class RequestTilbakekrevingsvedtak(
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
    val belopRenter: BigDecimalSerial,
    @SerialName("Tilbakerevingsbelop")
    val tilbakekrevingsbelop: List<Tilbakekrevingsbelop>,
)

@Serializable
data class Tilbakekrevingsbelop(
    @SerialName("KodeKlasse")
    val kodeKlasse: String,
    @SerialName("BelopOpprinneligUtbetalt")
    val belopOpprinneligUtbetalt: BigDecimalSerial,
    @SerialName("BelopNy")
    val belopNy: BigDecimalSerial,
    @SerialName("BelopTilbakekreves")
    val belopTilbakekreves: BigDecimalSerial,
    @SerialName("BelopUinnkrevd")
    val belopUinnkrevd: BigDecimalSerial,
    @SerialName("BelopSkatt")
    val belopSkatt: BigDecimalSerial,
    @SerialName("KodeResultat")
    val kodeResultat: String,
    @SerialName("KodeAarsak")
    val kodeAarsak: String,
    @SerialName("KodeSkyld")
    val kodeSkyld: String,
)

@Serializable
data class TilbakekrevingsvedtakResponse(
    @SerialName("OsTilbakekrevingsvedtakOperationResponse")
    val operationResponse: TilbakekrevingsvedtakOperationResponse,
)

@Serializable
data class TilbakekrevingsvedtakOperationResponse(
    @SerialName("ZT1OCont")
    val container: TilbakekrevingsvedtakResponseContainer,
)

@Serializable
data class TilbakekrevingsvedtakResponseContainer(
    @SerialName("ResponsTilbakekrevingsvedtak")
    val response: ResponsTilbakekrevingsvedtak,
)

@Serializable
data class ResponsTilbakekrevingsvedtak(
    @SerialName("Status")
    val status: Int,
    @SerialName("StatusMelding")
    val statusMelding: String,
    @SerialName("KodeAksjon")
    val kodeAksjon: String,
    @SerialName("VedtakId")
    val vedtakId: Int,
    @SerialName("DatoVedtakFagsystem")
    val datoVedtakFagsystem: String,
    @SerialName("KodeHjemmel")
    val kodeHjemmel: String,
    @SerialName("RenterBeregnes")
    val renterBeregnes: String,
    @SerialName("EnhetAnsvarlig")
    val enhetAnsvarlig: String,
    @SerialName("KontrollFelt")
    val kontrollfelt: String,
    @SerialName("SaksbehandlerId")
    val saksbehandlerId: String,
    @SerialName("DatoTilleggsfrist")
    val datoTilleggsfrist: String? = null,
)
