package no.nav.sokos.os.ekstern.api.api.models.liste

import kotlinx.serialization.Serializable

@Serializable
data class KravgrunnlagRequest(
    val kodeAksjon: String,
    val gjelderId: String,
    val typeGjelder: String,
    val utbetalesTilId: String,
    val typeUtbetalesTil: String,
    val enhetAnsvarlig: String,
    val kodeFaggruppe: String,
    val kodeFagomraade: String,
    val fagsystemId: String,
    val kravgrunnlagId: Int? = null,
    val saksbehandlerId: String,
)
