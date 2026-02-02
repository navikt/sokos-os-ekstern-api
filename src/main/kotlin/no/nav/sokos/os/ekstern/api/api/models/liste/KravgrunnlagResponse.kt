package no.nav.sokos.os.ekstern.api.api.models.liste

import kotlinx.serialization.Serializable

import no.nav.sokos.os.ekstern.api.util.BigDecimal

@Serializable
data class KravgrunnlagResponse(
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
    val belopSumFeilutbetalt: BigDecimal,
)
