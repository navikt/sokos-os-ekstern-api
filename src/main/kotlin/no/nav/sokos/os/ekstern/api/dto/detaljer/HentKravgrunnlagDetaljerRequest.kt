package no.nav.sokos.os.ekstern.api.dto.detaljer

import kotlinx.serialization.Serializable

@Serializable
data class HentKravgrunnlagDetaljerRequest(
    val kodeAksjon: String,
    val kravgrunnlagId: Long,
    val enhetAnsvarlig: String,
    val saksbehandlerId: String,
)
