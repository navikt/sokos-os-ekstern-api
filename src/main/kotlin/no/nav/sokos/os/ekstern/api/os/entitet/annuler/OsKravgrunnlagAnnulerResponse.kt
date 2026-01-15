package no.nav.sokos.os.ekstern.api.os.entitet.annuler

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OsKravgrunnlagAnnulerResponse(
    @SerialName("OsKravgrunnlagAnnulerOperationResponse")
    val operation: KravgrunnlagAnnulerResponseOperation,
)

@Serializable
data class KravgrunnlagAnnulerResponseOperation(
    @SerialName("KravgrunnlagAnnulert")
    val container: KravgrunnlagAnnulerResponseContainer,
)

@Serializable
data class KravgrunnlagAnnulerResponseContainer(
    @SerialName("ResponsKravgrunnlagAnnuler")
    val response: KravgrunnlagAnnulerResponseData,
)

@Serializable
data class KravgrunnlagAnnulerResponseData(
    @SerialName("Status")
    val status: Int,
    @SerialName("StatusMelding")
    val statusMelding: String,
    @SerialName("KodeAksjon")
    val kodeAksjon: String,
    @SerialName("VedtakId")
    val vedtakId: Int,
    @SerialName("SaksbehandlerId")
    val saksbehandlerId: String,
)
