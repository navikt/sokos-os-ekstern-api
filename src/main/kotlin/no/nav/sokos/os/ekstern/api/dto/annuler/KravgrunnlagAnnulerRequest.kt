package no.nav.sokos.os.ekstern.api.dto.annuler

import kotlinx.serialization.Serializable

@Serializable
data class KravgrunnlagAnnulerRequest(
    val kodeAksjon: String,
    val vedtakId: Long,
    val enhetAnsvarlig: String,
    val saksbehandlerId: String,
)
