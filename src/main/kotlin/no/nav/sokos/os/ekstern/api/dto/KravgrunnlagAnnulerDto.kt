package no.nav.sokos.os.ekstern.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class KravgrunnlagAnnulerRequest(
    val kodeAksjon: String,
    val vedtakId: Long,
    val enhetAnsvarlig: String,
    val saksbehandlerId: String,
)

@Serializable
data class KravgrunnlagAnnulerResponse(
    val status: Int,
    val melding: String,
    val vedtakId: Long,
    val saksbehandlerId: String,
)
