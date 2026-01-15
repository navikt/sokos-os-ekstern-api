package no.nav.sokos.os.ekstern.api.os.entitet.kravgrunnlag

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OsHentKravgrunnlagResponse(
    @SerialName("OsHentKravgrunnlagOperationResponse")
    val operation: HentKravgrunnlagResponseOperation,
)

@Serializable
data class HentKravgrunnlagResponseOperation(
    @SerialName("KravgrunnlagListe")
    val container: HentKravgrunnlagResponseContainer,
)

@Serializable
data class HentKravgrunnlagResponseContainer(
    @SerialName("ResponsKravgrunnlagListe")
    val response: HentKravgrunnlagResponseData,
)

@Serializable
data class HentKravgrunnlagResponseData(
    @SerialName("Status")
    val status: Int,
    @SerialName("StatusMelding")
    val statusMelding: String,
    @SerialName("Kravgrunnlag")
    val kravgrunnlag: List<Kravgrunnlag>,
)

@Serializable
data class Kravgrunnlag(
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
    val belopSumFeilutbetalt: Double,
)
