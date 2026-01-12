package no.nav.sokos.os.ekstern.api.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HentKravgrunnlagDetaljerRequest(
    @SerialName("OsHentKravgrunnlagDetaljerOperation")
    val operation: HentKravgrunnlagDetaljerOperation,
)

@Serializable
data class HentKravgrunnlagDetaljerOperation(
    @SerialName("Kravgrunnlagsdetaljer")
    val kravgrunnlagsdetaljer: KravgrunnlagDetaljerRequestWrapper,
)

@Serializable
data class KravgrunnlagDetaljerRequestWrapper(
    @SerialName("RequestDetaljer")
    val request: RequestDetaljer,
)

@Serializable
data class RequestDetaljer(
    @SerialName("KodeAksjon")
    val kodeAksjon: String,
    @SerialName("KravgrunnlagId")
    val kravgrunnlagId: Int,
    @SerialName("EnhetAnsvarlig")
    val enhetAnsvarlig: String,
    @SerialName("SaksbehandlerId")
    val saksbehandlerId: String,
)

@Serializable
data class HentKravgrunnlagDetaljerResponse(
    @SerialName("OsHentKravgrunnlagDetaljerOperationResponse")
    val operationResponse: HentKravgrunnlagDetaljerOperationResponse,
)

@Serializable
data class HentKravgrunnlagDetaljerOperationResponse(
    @SerialName("Kravgrunnlagsdetaljer")
    val kravgrunnlagsdetaljer: KravgrunnlagDetaljerResponseWrapper,
)

@Serializable
data class KravgrunnlagDetaljerResponseWrapper(
    @SerialName("ResponsDetaljer")
    val response: ResponsDetaljer,
)

@Serializable
data class ResponsDetaljer(
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
    val belopSkattMnd: BigDecimalSerial,
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
    val belopOpprinneligUtbetalt: BigDecimalSerial,
    @SerialName("BelopNy")
    val belopNy: BigDecimalSerial,
    @SerialName("BelopTilbakekreves")
    val belopTilbakekreves: BigDecimalSerial,
    @SerialName("BelopUinnkrevd")
    val belopUinnkrevd: BigDecimalSerial,
    @SerialName("SkattProsent")
    val skattProsent: BigDecimalSerial,
    @SerialName("KodeResultat")
    val kodeResultat: String,
    @SerialName("KodeAarsak")
    val kodeAarsak: String,
    @SerialName("KodeSkyld")
    val kodeSkyld: String,
)
