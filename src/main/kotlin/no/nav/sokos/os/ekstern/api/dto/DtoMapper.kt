package no.nav.sokos.os.ekstern.api.dto

import no.nav.sokos.os.ekstern.api.dto.annuler.KravgrunnlagAnnulerRequest
import no.nav.sokos.os.ekstern.api.dto.annuler.KravgrunnlagAnnulerResponse
import no.nav.sokos.os.ekstern.api.dto.detaljer.DetaljerPeriode
import no.nav.sokos.os.ekstern.api.dto.detaljer.DetaljerPostering
import no.nav.sokos.os.ekstern.api.dto.detaljer.HentKravgrunnlagDetaljerRequest
import no.nav.sokos.os.ekstern.api.dto.detaljer.HentKravgrunnlagDetaljerResponse
import no.nav.sokos.os.ekstern.api.dto.detaljer.KravgrunnlagDetaljer
import no.nav.sokos.os.ekstern.api.dto.kravgrunnlag.HentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.dto.kravgrunnlag.HentKravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.dto.kravgrunnlag.Kravgrunnlag
import no.nav.sokos.os.ekstern.api.dto.vedtak.Periode
import no.nav.sokos.os.ekstern.api.dto.vedtak.TilbakekrevingsvedtakRequest
import no.nav.sokos.os.ekstern.api.dto.vedtak.TilbakekrevingsvedtakResponse
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerRequest
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerRequestOsHentKravgrunnlagDetaljerOperation
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerRequestOsHentKravgrunnlagDetaljerOperationKravgrunnlagsdetaljer
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerRequestOsHentKravgrunnlagDetaljerOperationKravgrunnlagsdetaljerRequestDetaljer
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperation
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperationKravgrunnlag
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperationKravgrunnlagRequestTilbakekrevingsgrunnlag
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerRequest
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerRequestOsKravgrunnlagAnnulerOperation
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerRequestOsKravgrunnlagAnnulerOperationKravgrunnlagAnnuler
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerRequestOsKravgrunnlagAnnulerOperationKravgrunnlagAnnulerRequestKravgrunnlagAnnuler
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequest
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperation
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1ICont
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtak
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtakTilbakekrevingsperiodeInner
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtakTilbakekrevingsperiodeInnerTilbakerevingsbelopInner
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakResponse200

fun TilbakekrevingsvedtakRequest.toZosRequest(): PostOsTilbakekrevingsvedtakRequest =
    PostOsTilbakekrevingsvedtakRequest(
        osTilbakekrevingsvedtakOperation =
            PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperation(
                zt1ICont =
                    PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1ICont(
                        requestTilbakekrevingsvedtak =
                            PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtak(
                                kodeAksjon = kodeAksjon,
                                vedtakId = vedtakId.toInt(),
                                datoVedtakFagsystem = vedtaksDato,
                                kodeHjemmel = kodeHjemmel,
                                renterBeregnes = renterBeregnes,
                                enhetAnsvarlig = enhetAnsvarlig,
                                kontrollfelt = kontrollfelt,
                                saksbehandlerId = saksbehandlerId,
                                datoTilleggsfrist = datoTilleggsfrist,
                                tilbakekrevingsperiode = perioder.map { it.toZosPeriode() },
                            ),
                    ),
            ),
    )

fun Periode.toZosPeriode(): PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtakTilbakekrevingsperiodeInner =
    PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtakTilbakekrevingsperiodeInner(
        datoPeriodeFom = periodeFom,
        datoPeriodeTom = periodeTom,
        renterPeriodeBeregnes = renterPeriodeBeregnes,
        belopRenter = belopRenter.toBigDecimal(),
        tilbakerevingsbelop =
            posteringer.map {
                PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtakTilbakekrevingsperiodeInnerTilbakerevingsbelopInner(
                    kodeKlasse = it.kodeKlasse,
                    belopOpprinneligUtbetalt = it.belopOpprinneligUtbetalt.toBigDecimal(),
                    belopNy = it.belopNy.toBigDecimal(),
                    belopTilbakekreves = it.belopTilbakekreves.toBigDecimal(),
                    belopUinnkrevd = it.belopUinnkrevd.toBigDecimal(),
                    belopSkatt = it.belopSkatt.toBigDecimal(),
                    kodeResultat = it.kodeResultat,
                    kodeAarsak = it.kodeAarsak,
                    kodeSkyld = it.kodeSkyld,
                )
            },
    )

fun PostOsTilbakekrevingsvedtakResponse200.toDto(): TilbakekrevingsvedtakResponse {
    val resp = osTilbakekrevingsvedtakOperationResponse?.zt1OCont?.responsTilbakekrevingsvedtak
    return TilbakekrevingsvedtakResponse(
        status = resp?.status ?: 0,
        melding = resp?.statusMelding ?: "",
        vedtakId = resp?.vedtakId?.toLong() ?: 0L,
        datoVedtakFagsystem = resp?.datoVedtakFagsystem ?: "",
    )
}

fun HentKravgrunnlagRequest.toZosRequest(): PostOsHentKravgrunnlagRequest =
    PostOsHentKravgrunnlagRequest(
        osHentKravgrunnlagOperation =
            PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperation(
                kravgrunnlag =
                    PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperationKravgrunnlag(
                        requestTilbakekrevingsgrunnlag =
                            PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperationKravgrunnlagRequestTilbakekrevingsgrunnlag(
                                kodeAksjon = kodeAksjon,
                                gjelderId = gjelderId,
                                typeGjelder = typeGjelder,
                                utbetalesTilId = utbetalesTilId,
                                typeUtbetalesTil = typeUtbetalesTil,
                                enhetAnsvarlig = enhetAnsvarlig,
                                kodeFaggruppe = kodeFaggruppe,
                                kodeFagomraade = kodeFagomraade,
                                fagsystemId = fagsystemId,
                                kravgrunnlagId = kravgrunnlagId,
                                saksbehandlerId = saksbehandlerId,
                            ),
                    ),
            ),
    )

fun PostOsHentKravgrunnlagResponse200.toDto(): HentKravgrunnlagResponse {
    val resp = osHentKravgrunnlagOperationResponse?.kravgrunnlagListe?.responsKravgrunnlagListe
    return HentKravgrunnlagResponse(
        status = resp?.status ?: 0,
        melding = resp?.statusMelding ?: "",
        kravgrunnlagListe =
            resp?.kravgrunnlag?.map {
                Kravgrunnlag(
                    kravgrunnlagId = it.kravgrunnlagId?.toLong() ?: 0L,
                    kodeStatusKrav = it.kodeStatusKrav ?: "",
                    gjelderId = it.gjelderId ?: "",
                    typeGjelder = it.typeGjelder ?: "",
                    utbetalesTilId = it.utbetalesTilId ?: "",
                    typeUtbetalesTil = it.typeUtbetalesTil ?: "",
                    kodeFagomraade = it.kodeFagomraade ?: "",
                    fagsystemId = it.fagsystemId ?: "",
                    datoVedtakFagsystem = it.datoVedtakFagsystem ?: "",
                    enhetBosted = it.enhetBosted ?: "",
                    enhetAnsvarlig = it.enhetAnsvarlig ?: "",
                    datoKravDannet = it.datoKravDannet ?: "",
                    datoPeriodeFom = it.datoPeriodeFom ?: "",
                    datoPeriodeTom = it.datoPeriodeTom ?: "",
                    belopSumFeilutbetalt = it.belopSumFeilutbetalt?.toDouble() ?: 0.0,
                )
            } ?: emptyList(),
    )
}

fun HentKravgrunnlagDetaljerRequest.toZosRequest(): PostOsHentKravgrunnlagDetaljerRequest =
    PostOsHentKravgrunnlagDetaljerRequest(
        osHentKravgrunnlagDetaljerOperation =
            PostOsHentKravgrunnlagDetaljerRequestOsHentKravgrunnlagDetaljerOperation(
                kravgrunnlagsdetaljer =
                    PostOsHentKravgrunnlagDetaljerRequestOsHentKravgrunnlagDetaljerOperationKravgrunnlagsdetaljer(
                        requestDetaljer =
                            PostOsHentKravgrunnlagDetaljerRequestOsHentKravgrunnlagDetaljerOperationKravgrunnlagsdetaljerRequestDetaljer(
                                kodeAksjon = kodeAksjon,
                                kravgrunnlagId = kravgrunnlagId.toInt(),
                                enhetAnsvarlig = enhetAnsvarlig,
                                saksbehandlerId = saksbehandlerId,
                            ),
                    ),
            ),
    )

fun PostOsHentKravgrunnlagDetaljerResponse200.toDto(): HentKravgrunnlagDetaljerResponse {
    val resp = osHentKravgrunnlagDetaljerOperationResponse?.kravgrunnlagsdetaljer?.responsDetaljer
    return HentKravgrunnlagDetaljerResponse(
        status = resp?.status ?: 0,
        melding = resp?.statusMelding ?: "",
        kravgrunnlag =
            KravgrunnlagDetaljer(
                kravgrunnlagId = resp?.kravgrunnlagId?.toLong() ?: 0L,
                vedtakId = resp?.vedtakId?.toLong() ?: 0L,
                kodeStatusKrav = resp?.kodeStatusKrav ?: "",
                kodeFagomraade = resp?.kodeFagomraade ?: "",
                fagsystemId = resp?.fagsystemId ?: "",
                datoVedtakFagsystem = resp?.datoVedtakFagsystem ?: "",
                vedtakIdImgjort = resp?.vedtakIdImgjort?.toLong() ?: 0L,
                gjelderId = resp?.gjelderId ?: "",
                typeGjelder = resp?.typeGjelder ?: "",
                utbetalesTilId = resp?.utbetalesTilId ?: "",
                typeUtbetalesTilId = resp?.typeUtbetalesTilId ?: "",
                kodeHjemmel = resp?.kodeHjemmel ?: "",
                renterBeregnes = resp?.renterBeregnes ?: false,
                enhetAnsvarlig = resp?.enhetAnsvarlig ?: "",
                enhetBosted = resp?.enhetBosted ?: "",
                enhetBehandl = resp?.enhetBehandl ?: "",
                kontrollfelt = resp?.kontrollfelt ?: "",
                saksbehandlerId = resp?.saksbehandlerId ?: "",
                referanse = resp?.referanse ?: "",
                datoTilleggsfrist = resp?.datoTilleggsfrist ?: "",
                perioder =
                    resp?.tilbakekrevingsperiode?.map { periode ->
                        DetaljerPeriode(
                            periodeFom = periode.datoPeriodeFom ?: "",
                            periodeTom = periode.datoPeriodeTom ?: "",
                            belopSkattMnd = periode.belopSkattMnd?.toDouble() ?: 0.0,
                            posteringer =
                                periode.tilbakekrevingsbelop?.map { belop ->
                                    DetaljerPostering(
                                        kodeKlasse = belop.kodeKlasse ?: "",
                                        typeKlasse = belop.typeKlasse ?: "",
                                        belopOpprinneligUtbetalt = belop.belopOpprinneligUtbetalt?.toDouble() ?: 0.0,
                                        belopNy = belop.belopNy?.toDouble() ?: 0.0,
                                        belopTilbakekreves = belop.belopTilbakekreves?.toDouble() ?: 0.0,
                                        belopUinnkrevd = belop.belopUinnkrevd?.toDouble() ?: 0.0,
                                        skattProsent = belop.skattProsent?.toDouble() ?: 0.0,
                                        kodeResultat = belop.kodeResultat ?: "",
                                        kodeAarsak = belop.kodeAarsak ?: "",
                                        kodeSkyld = belop.kodeSkyld ?: "",
                                    )
                                } ?: emptyList(),
                        )
                    } ?: emptyList(),
            ),
    )
}

fun KravgrunnlagAnnulerRequest.toZosRequest(): PostOsKravgrunnlagAnnulerRequest =
    PostOsKravgrunnlagAnnulerRequest(
        osKravgrunnlagAnnulerOperation =
            PostOsKravgrunnlagAnnulerRequestOsKravgrunnlagAnnulerOperation(
                kravgrunnlagAnnuler =
                    PostOsKravgrunnlagAnnulerRequestOsKravgrunnlagAnnulerOperationKravgrunnlagAnnuler(
                        requestKravgrunnlagAnnuler =
                            PostOsKravgrunnlagAnnulerRequestOsKravgrunnlagAnnulerOperationKravgrunnlagAnnulerRequestKravgrunnlagAnnuler(
                                kodeAksjon = kodeAksjon,
                                vedtakId = vedtakId.toInt(),
                                enhetAnsvarlig = enhetAnsvarlig,
                                saksbehandlerId = saksbehandlerId,
                            ),
                    ),
            ),
    )

fun PostOsKravgrunnlagAnnulerResponse200.toDto(): KravgrunnlagAnnulerResponse {
    val resp = osKravgrunnlagAnnulerOperationResponse?.kravgrunnlagAnnulert?.responsKravgrunnlagAnnuler
    return KravgrunnlagAnnulerResponse(
        status = resp?.status ?: 0,
        melding = resp?.statusMelding ?: "",
        vedtakId = resp?.vedtakId?.toLong() ?: 0L,
        saksbehandlerId = resp?.saksbehandlerId ?: "",
    )
}
