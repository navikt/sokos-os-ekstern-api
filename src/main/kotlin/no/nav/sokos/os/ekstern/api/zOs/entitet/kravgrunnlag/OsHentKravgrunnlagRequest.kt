package no.nav.sokos.os.ekstern.api.zOs.entitet.kravgrunnlag

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OsHentKravgrunnlagRequest(
    @SerialName("OsHentKravgrunnlagOperation")
    val operation: HentKravgrunnlagRequestOperation,
)

@Serializable
data class HentKravgrunnlagRequestOperation(
    @SerialName("Kravgrunnlag")
    val container: HentKravgrunnlagRequestContainer,
)

@Serializable
data class HentKravgrunnlagRequestContainer(
    @SerialName("RequestTilbakekrevingsgrunnlag")
    val request: HentKravgrunnlagRequestData,
)

@Serializable
data class HentKravgrunnlagRequestData(
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
