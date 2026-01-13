package no.nav.sokos.os.ekstern.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class HentKravgrunnlagRequest(
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

@Serializable
data class HentKravgrunnlagResponse(
    val status: Int,
    val melding: String,
    val kravgrunnlagListe: List<Kravgrunnlag>,
)

@Serializable
data class Kravgrunnlag(
    val kravgrunnlagId: Long,
    val kodeStatusKrav: String,
    val gjelderId: String,
    val typeGjelder: String,
    val utbetalesTilId: String,
    val typeUtbetalesTil: String,
    val kodeFagomraade: String,
    val fagsystemId: String,
    val datoVedtakFagsystem: String,
    val enhetBosted: String,
    val enhetAnsvarlig: String,
    val datoKravDannet: String,
    val datoPeriodeFom: String,
    val datoPeriodeTom: String,
    val belopSumFeilutbetalt: Double,
)
