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
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljer
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljerTilbakekrevingsperiodeInner
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljerTilbakekrevingsperiodeInnerTilbakekrevingsbelopInner
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperation
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperationKravgrunnlag
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperationKravgrunnlagRequestTilbakekrevingsgrunnlag
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponseKravgrunnlagListeResponsKravgrunnlagListe
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponseKravgrunnlagListeResponsKravgrunnlagListeKravgrunnlagInner
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerRequest
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerRequestOsKravgrunnlagAnnulerOperation
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerRequestOsKravgrunnlagAnnulerOperationKravgrunnlagAnnuler
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerRequestOsKravgrunnlagAnnulerOperationKravgrunnlagAnnulerRequestKravgrunnlagAnnuler
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerResponse200OsKravgrunnlagAnnulerOperationResponseKravgrunnlagAnnulertResponsKravgrunnlagAnnuler
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequest
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperation
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1ICont
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtak
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtakTilbakekrevingsperiodeInner
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtakTilbakekrevingsperiodeInnerTilbakerevingsbelopInner
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakResponse200OsTilbakekrevingsvedtakOperationResponseZT1OContResponsTilbakekrevingsvedtak
import no.nav.sokos.os.ekstern.api.service.TilbakekrevingException

private typealias OsTilbakekrevingsvedtakRespons =
    PostOsTilbakekrevingsvedtakResponse200OsTilbakekrevingsvedtakOperationResponseZT1OContResponsTilbakekrevingsvedtak

private typealias OsKravgrunnlagListeRespons =
    PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponseKravgrunnlagListeResponsKravgrunnlagListe

private typealias OsKravgrunnlagListeElement =
    PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponseKravgrunnlagListeResponsKravgrunnlagListeKravgrunnlagInner

private typealias OsKravgrunnlagDetaljerRespons =
    PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljer

private typealias OsTilbakekrevingsperiode =
    PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljerTilbakekrevingsperiodeInner

private typealias OsTilbakekrevingsbelop =
    PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljerTilbakekrevingsperiodeInnerTilbakekrevingsbelopInner

private typealias OsKravgrunnlagAnnulerRespons =
    PostOsKravgrunnlagAnnulerResponse200OsKravgrunnlagAnnulerOperationResponseKravgrunnlagAnnulertResponsKravgrunnlagAnnuler

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
        belopRenter = belopRenter,
        tilbakerevingsbelop =
            posteringer.map {
                PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtakTilbakekrevingsperiodeInnerTilbakerevingsbelopInner(
                    kodeKlasse = it.kodeKlasse,
                    belopOpprinneligUtbetalt = it.belopOpprinneligUtbetalt,
                    belopNy = it.belopNy,
                    belopTilbakekreves = it.belopTilbakekreves,
                    belopUinnkrevd = it.belopUinnkrevd,
                    belopSkatt = it.belopSkatt,
                    kodeResultat = it.kodeResultat,
                    kodeAarsak = it.kodeAarsak,
                    kodeSkyld = it.kodeSkyld,
                )
            },
    )

fun PostOsTilbakekrevingsvedtakResponse200.toDto(): TilbakekrevingsvedtakResponse {
    val resp = extractTilbakekrevingsvedtakResponse()
    return TilbakekrevingsvedtakResponse(
        status = resp.status ?: 0,
        melding = resp.statusMelding.orEmpty(),
        vedtakId =
            resp.vedtakId?.toLong()
                ?: throw TilbakekrevingException("OS response mangler vedtakId i tilbakekrevingsvedtak respons"),
        datoVedtakFagsystem = resp.datoVedtakFagsystem.orEmpty(),
    )
}

private fun PostOsTilbakekrevingsvedtakResponse200.extractTilbakekrevingsvedtakResponse(): OsTilbakekrevingsvedtakRespons =
    osTilbakekrevingsvedtakOperationResponse
        ?.zt1OCont
        ?.responsTilbakekrevingsvedtak
        ?: throw TilbakekrevingException("OS response mangler tilbakekrevingsvedtak wrapper struktur")

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
    val resp = extractKravgrunnlagListeResponse()
    return HentKravgrunnlagResponse(
        status = resp.status ?: 0,
        melding = resp.statusMelding.orEmpty(),
        kravgrunnlagListe = resp.kravgrunnlag?.map { it.toKravgrunnlag() } ?: emptyList(),
    )
}

private fun PostOsHentKravgrunnlagResponse200.extractKravgrunnlagListeResponse(): OsKravgrunnlagListeRespons =
    osHentKravgrunnlagOperationResponse
        ?.kravgrunnlagListe
        ?.responsKravgrunnlagListe
        ?: throw TilbakekrevingException("OS response mangler kravgrunnlagListe wrapper struktur")

private fun OsKravgrunnlagListeElement.toKravgrunnlag(): Kravgrunnlag =
    Kravgrunnlag(
        kravgrunnlagId =
            kravgrunnlagId?.toLong()
                ?: throw TilbakekrevingException("OS response mangler kravgrunnlagId i kravgrunnlag liste element"),
        kodeStatusKrav = kodeStatusKrav.orEmpty(),
        gjelderId = gjelderId.orEmpty(),
        typeGjelder = typeGjelder.orEmpty(),
        utbetalesTilId = utbetalesTilId.orEmpty(),
        typeUtbetalesTil = typeUtbetalesTil.orEmpty(),
        kodeFagomraade = kodeFagomraade.orEmpty(),
        fagsystemId = fagsystemId.orEmpty(),
        datoVedtakFagsystem = datoVedtakFagsystem.orEmpty(),
        enhetBosted = enhetBosted.orEmpty(),
        enhetAnsvarlig = enhetAnsvarlig.orEmpty(),
        datoKravDannet = datoKravDannet.orEmpty(),
        datoPeriodeFom = datoPeriodeFom.orEmpty(),
        datoPeriodeTom = datoPeriodeTom.orEmpty(),
        belopSumFeilutbetalt = belopSumFeilutbetalt ?: 0.0,
    )

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
    val resp = extractKravgrunnlagDetaljerResponse()
    return HentKravgrunnlagDetaljerResponse(
        status = resp.status ?: 0,
        melding = resp.statusMelding.orEmpty(),
        kravgrunnlag = resp.toKravgrunnlagDetaljer(),
    )
}

private fun PostOsHentKravgrunnlagDetaljerResponse200.extractKravgrunnlagDetaljerResponse(): OsKravgrunnlagDetaljerRespons =
    osHentKravgrunnlagDetaljerOperationResponse
        ?.kravgrunnlagsdetaljer
        ?.responsDetaljer
        ?: throw TilbakekrevingException("OS response mangler kravgrunnlagDetaljer wrapper struktur")

private fun OsKravgrunnlagDetaljerRespons.toKravgrunnlagDetaljer(): KravgrunnlagDetaljer =
    KravgrunnlagDetaljer(
        kravgrunnlagId =
            kravgrunnlagId?.toLong()
                ?: throw TilbakekrevingException("OS response mangler kravgrunnlagId i detaljer"),
        vedtakId =
            vedtakId?.toLong()
                ?: throw TilbakekrevingException("OS response mangler vedtakId i detaljer"),
        kodeStatusKrav = kodeStatusKrav.orEmpty(),
        kodeFagomraade = kodeFagomraade.orEmpty(),
        fagsystemId = fagsystemId.orEmpty(),
        datoVedtakFagsystem = datoVedtakFagsystem.orEmpty(),
        vedtakIdImgjort = vedtakIdImgjort?.toLong() ?: 0L,
        gjelderId = gjelderId.orEmpty(),
        typeGjelder = typeGjelder.orEmpty(),
        utbetalesTilId = utbetalesTilId.orEmpty(),
        typeUtbetalesTilId = typeUtbetalesTilId.orEmpty(),
        kodeHjemmel = kodeHjemmel.orEmpty(),
        renterBeregnes = renterBeregnes ?: false,
        enhetAnsvarlig = enhetAnsvarlig.orEmpty(),
        enhetBosted = enhetBosted.orEmpty(),
        enhetBehandl = enhetBehandl.orEmpty(),
        kontrollfelt = kontrollfelt.orEmpty(),
        saksbehandlerId = saksbehandlerId.orEmpty(),
        referanse = referanse.orEmpty(),
        datoTilleggsfrist = datoTilleggsfrist,
        perioder = tilbakekrevingsperiode?.map { it.toDetaljerPeriode() } ?: emptyList(),
    )

private fun OsTilbakekrevingsperiode.toDetaljerPeriode(): DetaljerPeriode =
    DetaljerPeriode(
        periodeFom = datoPeriodeFom.orEmpty(),
        periodeTom = datoPeriodeTom.orEmpty(),
        belopSkattMnd = belopSkattMnd ?: 0.0,
        posteringer = tilbakekrevingsbelop?.map { it.toDetaljerPostering() } ?: emptyList(),
    )

private fun OsTilbakekrevingsbelop.toDetaljerPostering(): DetaljerPostering =
    DetaljerPostering(
        kodeKlasse = kodeKlasse.orEmpty(),
        typeKlasse = typeKlasse.orEmpty(),
        belopOpprinneligUtbetalt = belopOpprinneligUtbetalt ?: 0.0,
        belopNy = belopNy ?: 0.0,
        belopTilbakekreves = belopTilbakekreves ?: 0.0,
        belopUinnkrevd = belopUinnkrevd ?: 0.0,
        skattProsent = skattProsent ?: 0.0,
        kodeResultat = kodeResultat.orEmpty(),
        kodeAarsak = kodeAarsak.orEmpty(),
        kodeSkyld = kodeSkyld.orEmpty(),
    )

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
    val resp = extractKravgrunnlagAnnulerResponse()
    return KravgrunnlagAnnulerResponse(
        status = resp.status ?: 0,
        melding = resp.statusMelding.orEmpty(),
        vedtakId =
            resp.vedtakId?.toLong()
                ?: throw TilbakekrevingException("OS response mangler vedtakId i annuler respons"),
        saksbehandlerId = resp.saksbehandlerId.orEmpty(),
    )
}

private fun PostOsKravgrunnlagAnnulerResponse200.extractKravgrunnlagAnnulerResponse(): OsKravgrunnlagAnnulerRespons =
    osKravgrunnlagAnnulerOperationResponse
        ?.kravgrunnlagAnnulert
        ?.responsKravgrunnlagAnnuler
        ?: throw TilbakekrevingException("OS response mangler kravgrunnlagAnnuler wrapper struktur")
