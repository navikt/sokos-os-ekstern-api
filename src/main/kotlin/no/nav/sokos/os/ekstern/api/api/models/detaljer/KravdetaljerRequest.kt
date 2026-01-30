package no.nav.sokos.os.ekstern.api.api.models.detaljer

import kotlinx.serialization.Serializable

@Serializable
data class KravdetaljerRequest(
    val kodeAksjon: String,
    val kravgrunnlagId: Int,
    val enhetAnsvarlig: String,
    val saksbehandlerId: String,
)
