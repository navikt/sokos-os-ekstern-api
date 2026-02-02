package no.nav.sokos.os.ekstern.api.api.models.annuler

import kotlinx.serialization.Serializable

@Serializable
data class AnnulerRequest(
    val kodeAksjon: String,
    val vedtakId: Int,
    val enhetAnsvarlig: String,
    val saksbehandlerId: String,
)
