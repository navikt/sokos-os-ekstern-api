package no.nav.sokos.os.ekstern.api.os.entitet.vedtak

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OsTilbakekrevingsvedtakResponse(
    @SerialName("OsTilbakekrevingsvedtakOperationResponse")
    val operation: TilbakekrevingsvedtakResponseOperation,
)

@Serializable
data class TilbakekrevingsvedtakResponseOperation(
    @SerialName("ZT1OCont")
    val container: TilbakekrevingsvedtakResponseContainer,
)

@Serializable
data class TilbakekrevingsvedtakResponseContainer(
    @SerialName("ResponsTilbakekrevingsvedtak")
    val response: TilbakekrevingsvedtakResponseData,
)

@Serializable
data class TilbakekrevingsvedtakResponseData(
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
