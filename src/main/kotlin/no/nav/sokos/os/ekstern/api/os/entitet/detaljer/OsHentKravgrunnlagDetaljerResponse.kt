package no.nav.sokos.os.ekstern.api.os.entitet.detaljer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OsHentKravgrunnlagDetaljerResponse(
    @SerialName("OsHentKravgrunnlagDetaljerOperationResponse")
    val operation: HentKravgrunnlagDetaljerResponseOperation,
)

@Serializable
data class HentKravgrunnlagDetaljerResponseOperation(
    @SerialName("Kravgrunnlagsdetaljer")
    val container: HentKravgrunnlagDetaljerResponseContainer,
)

@Serializable
data class HentKravgrunnlagDetaljerResponseContainer(
    @SerialName("ResponsDetaljer")
    val response: HentKravgrunnlagDetaljerResponseData,
)

@Serializable
data class HentKravgrunnlagDetaljerResponseData(
    @SerialName("Status")
    val status: Int,
    @SerialName("StatusMelding")
    val statusMelding: String,
    @SerialName("KravgrunnlagId")
    val kravgrunnlagId: Int,
    @SerialName("VedtakId")
    val vedtakId: Int,
    @SerialName("KodeStatusKrav")
    val kodeStatusKrav: String,
    @SerialName("KodeFagomraade")
    val kodeFagomraade: String,
    @SerialName("FagsystemId")
    val fagsystemId: String,
    @SerialName("DatoVedtakFagsystem")
    val datoVedtakFagsystem: String,
    @SerialName("VedtakIdImgjort")
    val vedtakIdImgjort: Int,
    @SerialName("GjelderId")
    val gjelderId: String,
    @SerialName("TypeGjelder")
    val typeGjelder: String,
    @SerialName("UtbetalesTilId")
    val utbetalesTilId: String,
    @SerialName("TypeUtbetalesTilId")
    val typeUtbetalesTilId: String,
    @SerialName("KodeHjemmel")
    val kodeHjemmel: String,
    @SerialName("RenterBeregnes")
    val renterBeregnes: Boolean,
    @SerialName("EnhetAnsvarlig")
    val enhetAnsvarlig: String,
    @SerialName("EnhetBosted")
    val enhetBosted: String,
    @SerialName("EnhetBehandl")
    val enhetBehandl: String,
    @SerialName("Kontrollfelt")
    val kontrollfelt: String,
    @SerialName("SaksbehandlerId")
    val saksbehandlerId: String,
    @SerialName("Referanse")
    val referanse: String,
    @SerialName("DatoTilleggsfrist")
    val datoTilleggsfrist: String? = null,
    @SerialName("Tilbakekrevingsperiode")
    val tilbakekrevingsperiode: List<TilbakekrevingsperiodeDetalj>,
)

@Serializable
data class TilbakekrevingsperiodeDetalj(
    @SerialName("DatoPeriodeFom")
    val datoPeriodeFom: String,
    @SerialName("DatoPeriodeTom")
    val datoPeriodeTom: String,
    @SerialName("BelopSkattMnd")
    val belopSkattMnd: Double,
    @SerialName("Tilbakekrevingsbelop")
    val tilbakekrevingsbelop: List<TilbakekrevingsbelopDetalj>,
)

@Serializable
data class TilbakekrevingsbelopDetalj(
    @SerialName("KodeKlasse")
    val kodeKlasse: String,
    @SerialName("TypeKlasse")
    val typeKlasse: String,
    @SerialName("BelopOpprinneligUtbetalt")
    val belopOpprinneligUtbetalt: Double,
    @SerialName("BelopNy")
    val belopNy: Double,
    @SerialName("BelopTilbakekreves")
    val belopTilbakekreves: Double,
    @SerialName("BelopUinnkrevd")
    val belopUinnkrevd: Double,
    @SerialName("SkattProsent")
    val skattProsent: Double,
    @SerialName("KodeResultat")
    val kodeResultat: String,
    @SerialName("KodeAarsak")
    val kodeAarsak: String,
    @SerialName("KodeSkyld")
    val kodeSkyld: String,
)
