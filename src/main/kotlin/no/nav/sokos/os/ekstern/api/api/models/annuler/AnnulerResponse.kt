package no.nav.sokos.os.ekstern.api.api.models.annuler

import kotlinx.serialization.Serializable

@Serializable
data class AnnulerResponse(
    val status: Int,
    val melding: String,
    val vedtakId: Int,
    val saksbehandlerId: String,
)
