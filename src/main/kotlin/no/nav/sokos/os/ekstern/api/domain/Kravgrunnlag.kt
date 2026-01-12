package no.nav.sokos.os.ekstern.api.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HentKravgrunnlagRequest(
    @SerialName("OsHentKravgrunnlagOperation")
    val operation: HentKravgrunnlagOperation,
)

@Serializable
data class HentKravgrunnlagOperation(
    @SerialName("Kravgrunnlag")
    val kravgrunnlag: KravgrunnlagRequestWrapper,
)

@Serializable
data class KravgrunnlagRequestWrapper(
    @SerialName("RequestTilbakekrevingsgrunnlag")
    val request: RequestTilbakekrevingsgrunnlag,
)

@Serializable
data class RequestTilbakekrevingsgrunnlag(
    @SerialName("KodeAksjon")
    val kodeAksjon: String,
    @SerialName("GjelderId")
    val gjelderId: String? = null,
    @SerialName("TypeGjelder")
    val typeGjelder: String? = null,
    @SerialName("UtbetalesTilId")
    val utbetalesTilId: String? = null,
    @SerialName("TypeUtbetalesTil")
    val typeUtbetalesTil: String? = null,
    @SerialName("EnhetAnsvarlig")
    val enhetAnsvarlig: String,
    @SerialName("KodeFaggruppe")
    val kodeFaggruppe: String? = null,
    @SerialName("KodeFagomraade")
    val kodeFagomraade: String? = null,
    @SerialName("FagsystemId")
    val fagsystemId: String? = null,
    @SerialName("KravgrunnlagId")
    val kravgrunnlagId: Int? = null,
    @SerialName("SaksbehandlerId")
    val saksbehandlerId: String,
)

@Serializable
data class HentKravgrunnlagResponse(
    @SerialName("OsHentKravgrunnlagOperationResponse")
    val operationResponse: HentKravgrunnlagOperationResponse,
)

@Serializable
data class HentKravgrunnlagOperationResponse(
    @SerialName("KravgrunnlagListe")
    val kravgrunnlagListe: KravgrunnlagListeWrapper,
)

@Serializable
data class KravgrunnlagListeWrapper(
    @SerialName("ResponsKravgrunnlagListe")
    val response: ResponsKravgrunnlagListe,
)

@Serializable
data class ResponsKravgrunnlagListe(
    @SerialName("Status")
    val status: Int,
    @SerialName("StatusMelding")
    val statusMelding: String,
    @SerialName("Kravgrunnlag")
    val kravgrunnlag: List<KravgrunnlagListeItem>,
)

@Serializable
data class KravgrunnlagListeItem(
    @SerialName("KravgrunnlagId")
    val kravgrunnlagId: Int,
    @SerialName("KodeStatusKrav")
    val kodeStatusKrav: String,
    @SerialName("GjelderId")
    val gjelderId: String,
    @SerialName("TypeGjelder")
    val typeGjelder: String,
    @SerialName("UtbetalesTilId")
    val utbetalesTilId: String,
    @SerialName("TypeUtbetalesTil")
    val typeUtbetalesTil: String,
    @SerialName("KodeFagomraade")
    val kodeFagomraade: String,
    @SerialName("FagsystemId")
    val fagsystemId: String,
    @SerialName("DatoVedtakFagsystem")
    val datoVedtakFagsystem: String,
    @SerialName("EnhetBosted")
    val enhetBosted: String,
    @SerialName("EnhetAnsvarlig")
    val enhetAnsvarlig: String,
    @SerialName("DatoKravDannet")
    val datoKravDannet: String,
    @SerialName("DatoPeriodeFom")
    val datoPeriodeFom: String,
    @SerialName("DatoPeriodeTom")
    val datoPeriodeTom: String,
    @SerialName("BelopSumFeilutbetalt")
    val belopSumFeilutbetalt: BigDecimalSerial,
)
