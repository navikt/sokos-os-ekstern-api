package no.nav.sokos.os.ekstern.api.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KravgrunnlagAnnulerRequest(
    @SerialName("OsKravgrunnlagAnnulerOperation")
    val operation: KravgrunnlagAnnulerOperation,
)

@Serializable
data class KravgrunnlagAnnulerOperation(
    @SerialName("KravgrunnlagAnnuler")
    val kravgrunnlagAnnuler: KravgrunnlagAnnulerRequestWrapper,
)

@Serializable
data class KravgrunnlagAnnulerRequestWrapper(
    @SerialName("RequestKravgrunnlagAnnuler")
    val request: RequestKravgrunnlagAnnuler,
)

@Serializable
data class RequestKravgrunnlagAnnuler(
    @SerialName("KodeAksjon")
    val kodeAksjon: String,
    @SerialName("VedtakId")
    val vedtakId: Int,
    @SerialName("EnhetAnsvarlig")
    val enhetAnsvarlig: String,
    @SerialName("SaksbehandlerId")
    val saksbehandlerId: String,
)

@Serializable
data class KravgrunnlagAnnulerResponse(
    @SerialName("OsKravgrunnlagAnnulerOperationResponse")
    val operationResponse: KravgrunnlagAnnulerOperationResponse,
)

@Serializable
data class KravgrunnlagAnnulerOperationResponse(
    @SerialName("KravgrunnlagAnnulert")
    val kravgrunnlagAnnulert: KravgrunnlagAnnulertWrapper,
)

@Serializable
data class KravgrunnlagAnnulertWrapper(
    @SerialName("ResponsKravgrunnlagAnnuler")
    val response: ResponsKravgrunnlagAnnuler,
)

@Serializable
data class ResponsKravgrunnlagAnnuler(
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
