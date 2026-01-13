package no.nav.sokos.os.ekstern.api.dto.annuler

import kotlinx.serialization.Serializable

@Serializable
data class KravgrunnlagAnnulerResponse(
    val status: Int,
    val melding: String,
    val vedtakId: Long,
    val saksbehandlerId: String,
)
