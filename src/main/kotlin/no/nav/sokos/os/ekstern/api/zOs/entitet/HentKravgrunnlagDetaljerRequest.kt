package no.nav.sokos.os.ekstern.api.zOs.entitet

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HentKravgrunnlagDetaljerRequest(
    @SerialName("OsHentKravgrunnlagDetaljerOperation")
    val operation: HentKravgrunnlagDetaljerRequestOperation,
)

@Serializable
data class HentKravgrunnlagDetaljerRequestOperation(
    @SerialName("Kravgrunnlagsdetaljer")
    val container: HentKravgrunnlagDetaljerRequestContainer,
)

@Serializable
data class HentKravgrunnlagDetaljerRequestContainer(
    @SerialName("RequestDetaljer")
    val request: HentKravgrunnlagDetaljerRequestData,
)

@Serializable
data class HentKravgrunnlagDetaljerRequestData(
    @SerialName("KodeAksjon")
    val kodeAksjon: String,
    @SerialName("KravgrunnlagId")
    val kravgrunnlagId: Int,
    @SerialName("EnhetAnsvarlig")
    val enhetAnsvarlig: String,
    @SerialName("SaksbehandlerId")
    val saksbehandlerId: String,
)
