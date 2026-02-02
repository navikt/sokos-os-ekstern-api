package no.nav.sokos.os.ekstern.api.api.models.detaljer

import kotlinx.serialization.Serializable

import no.nav.sokos.os.ekstern.api.util.BigDecimal

@Serializable
data class KravdetaljerResponse(
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
    val perioder: List<DetaljerPeriode>,
)

@Serializable
data class DetaljerPeriode(
    val periodeFom: String,
    val periodeTom: String,
    val belopSkattMnd: BigDecimal,
    val posteringer: List<DetaljerPostering>,
)

@Serializable
data class DetaljerPostering(
    val kodeKlasse: String,
    val typeKlasse: String,
    val belopOpprinneligUtbetalt: BigDecimal,
    val belopNy: BigDecimal,
    val belopTilbakekreves: BigDecimal,
    val belopUinnkrevd: BigDecimal,
    val skattProsent: BigDecimal,
    val kodeResultat: String,
    val kodeAarsak: String,
    val kodeSkyld: String,
)
