package no.nav.sokos.os.ekstern.api.os.entitet.annuler

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OsKravgrunnlagAnnulerRequest(
    @SerialName("OsKravgrunnlagAnnulerOperation")
    val operation: KravgrunnlagAnnulerRequestOperation,
)

@Serializable
data class KravgrunnlagAnnulerRequestOperation(
    @SerialName("KravgrunnlagAnnuler")
    val container: KravgrunnlagAnnulerRequestContainer,
)

@Serializable
data class KravgrunnlagAnnulerRequestContainer(
    @SerialName("RequestKravgrunnlagAnnuler")
    val request: KravgrunnlagAnnulerRequestData,
)

@Serializable
data class KravgrunnlagAnnulerRequestData(
    @SerialName("KodeAksjon")
    val kodeAksjon: String,
    @SerialName("VedtakId")
    val vedtakId: Int,
    @SerialName("EnhetAnsvarlig")
    val enhetAnsvarlig: String,
    @SerialName("SaksbehandlerId")
    val saksbehandlerId: String,
)
