package no.nav.sokos.os.ekstern.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class HentKravgrunnlagDetaljerRequest(
    val kodeAksjon: String,
    val kravgrunnlagId: Long,
    val enhetAnsvarlig: String,
    val saksbehandlerId: String,
)

@Serializable
data class HentKravgrunnlagDetaljerResponse(
    val status: Int,
    val melding: String,
    val kravgrunnlag: KravgrunnlagDetaljer,
)

@Serializable
data class KravgrunnlagDetaljer(
    val kravgrunnlagId: Long,
    val vedtakId: Long,
    val kodeStatusKrav: String,
    val kodeFagomraade: String,
    val fagsystemId: String,
    val datoVedtakFagsystem: String,
    val vedtakIdImgjort: Long,
    val gjelderId: String,
    val typeGjelder: String,
    val utbetalesTilId: String,
    val typeUtbetalesTilId: String,
    val kodeHjemmel: String,
    val renterBeregnes: Boolean,
    val enhetAnsvarlig: String,
    val enhetBosted: String,
    val enhetBehandl: String,
    val kontrollfelt: String,
    val saksbehandlerId: String,
    val referanse: String,
    val datoTilleggsfrist: String? = null,
    val perioder: List<KravgrunnlagPeriode>,
)

@Serializable
data class KravgrunnlagPeriode(
    val periodeFom: String,
    val periodeTom: String,
    val belopSkattMnd: Double,
    val posteringer: List<KravgrunnlagPostering>,
)

@Serializable
data class KravgrunnlagPostering(
    val kodeKlasse: String,
    val typeKlasse: String,
    val belopOpprinneligUtbetalt: Double,
    val belopNy: Double,
    val belopTilbakekreves: Double,
    val belopUinnkrevd: Double,
    val skattProsent: Double,
    val kodeResultat: String,
    val kodeAarsak: String,
    val kodeSkyld: String,
)
