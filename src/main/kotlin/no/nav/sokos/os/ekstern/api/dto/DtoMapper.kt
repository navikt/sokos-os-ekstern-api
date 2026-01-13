package no.nav.sokos.os.ekstern.api.dto

import no.nav.sokos.os.ekstern.api.zOs.entitet.HentKravgrunnlagDetaljerRequest as ZosHentKravgrunnlagDetaljerRequest
import no.nav.sokos.os.ekstern.api.zOs.entitet.HentKravgrunnlagDetaljerResponse as ZosHentKravgrunnlagDetaljerResponse
import no.nav.sokos.os.ekstern.api.zOs.entitet.HentKravgrunnlagRequest as ZosHentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.zOs.entitet.HentKravgrunnlagResponse as ZosHentKravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.zOs.entitet.KravgrunnlagAnnulerRequest as ZosKravgrunnlagAnnulerRequest
import no.nav.sokos.os.ekstern.api.zOs.entitet.KravgrunnlagAnnulerResponse as ZosKravgrunnlagAnnulerResponse
import no.nav.sokos.os.ekstern.api.zOs.entitet.TilbakekrevingsvedtakRequest as ZosTilbakekrevingsvedtakRequest
import no.nav.sokos.os.ekstern.api.zOs.entitet.TilbakekrevingsvedtakResponse as ZosTilbakekrevingsvedtakResponse
import no.nav.sokos.os.ekstern.api.zOs.entitet.HentKravgrunnlagDetaljerRequestContainer
import no.nav.sokos.os.ekstern.api.zOs.entitet.HentKravgrunnlagDetaljerRequestData
import no.nav.sokos.os.ekstern.api.zOs.entitet.HentKravgrunnlagDetaljerRequestOperation
import no.nav.sokos.os.ekstern.api.zOs.entitet.HentKravgrunnlagRequestContainer
import no.nav.sokos.os.ekstern.api.zOs.entitet.HentKravgrunnlagRequestData
import no.nav.sokos.os.ekstern.api.zOs.entitet.HentKravgrunnlagRequestOperation
import no.nav.sokos.os.ekstern.api.zOs.entitet.KravgrunnlagAnnulerRequestContainer
import no.nav.sokos.os.ekstern.api.zOs.entitet.KravgrunnlagAnnulerRequestData
import no.nav.sokos.os.ekstern.api.zOs.entitet.KravgrunnlagAnnulerRequestOperation
import no.nav.sokos.os.ekstern.api.zOs.entitet.Tilbakekrevingsperiode
import no.nav.sokos.os.ekstern.api.zOs.entitet.TilbakekrevingsvedtakRequestContainer
import no.nav.sokos.os.ekstern.api.zOs.entitet.TilbakekrevingsvedtakRequestData
import no.nav.sokos.os.ekstern.api.zOs.entitet.TilbakekrevingsvedtakRequestOperation
import no.nav.sokos.os.ekstern.api.zOs.entitet.Tilbakerevingsbelop

fun TilbakekrevingsvedtakRequest.toZosRequest(): ZosTilbakekrevingsvedtakRequest =
    ZosTilbakekrevingsvedtakRequest(
        operation =
            TilbakekrevingsvedtakRequestOperation(
                container =
                    TilbakekrevingsvedtakRequestContainer(
                        request =
                            TilbakekrevingsvedtakRequestData(
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

fun VedtaksPeriode.toZosPeriode(): Tilbakekrevingsperiode =
    Tilbakekrevingsperiode(
        datoPeriodeFom = periodeFom,
        datoPeriodeTom = periodeTom,
        renterPeriodeBeregnes = renterPeriodeBeregnes,
        belopRenter = belopRenter,
        tilbakerevingsbelop =
            posteringer.map {
                Tilbakerevingsbelop(
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

fun ZosTilbakekrevingsvedtakResponse.toDto(): TilbakekrevingsvedtakResponse {
    val resp = operation.container.response
    return TilbakekrevingsvedtakResponse(
        status = resp.status,
        melding = resp.statusMelding,
        vedtakId = resp.vedtakId.toLong(),
        datoVedtakFagsystem = resp.datoVedtakFagsystem,
    )
}

fun HentKravgrunnlagRequest.toZosRequest(): ZosHentKravgrunnlagRequest =
    ZosHentKravgrunnlagRequest(
        operation =
            HentKravgrunnlagRequestOperation(
                container =
                    HentKravgrunnlagRequestContainer(
                        request =
                            HentKravgrunnlagRequestData(
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

fun ZosHentKravgrunnlagResponse.toDto(): HentKravgrunnlagResponse {
    val resp = operation.container.response
    return HentKravgrunnlagResponse(
        status = resp.status,
        melding = resp.statusMelding,
        kravgrunnlagListe =
            resp.kravgrunnlag.map {
                no.nav.sokos.os.ekstern.api.dto.Kravgrunnlag(
                    kravgrunnlagId = it.kravgrunnlagId.toLong(),
                    kodeStatusKrav = it.kodeStatusKrav,
                    gjelderId = it.gjelderId,
                    typeGjelder = it.typeGjelder,
                    utbetalesTilId = it.utbetalesTilId,
                    typeUtbetalesTil = it.typeUtbetalesTil,
                    kodeFagomraade = it.kodeFagomraade,
                    fagsystemId = it.fagsystemId,
                    datoVedtakFagsystem = it.datoVedtakFagsystem,
                    enhetBosted = it.enhetBosted,
                    enhetAnsvarlig = it.enhetAnsvarlig,
                    datoKravDannet = it.datoKravDannet,
                    datoPeriodeFom = it.datoPeriodeFom,
                    datoPeriodeTom = it.datoPeriodeTom,
                    belopSumFeilutbetalt = it.belopSumFeilutbetalt,
                )
            },
    )
}

fun HentKravgrunnlagDetaljerRequest.toZosRequest(): ZosHentKravgrunnlagDetaljerRequest =
    ZosHentKravgrunnlagDetaljerRequest(
        operation =
            HentKravgrunnlagDetaljerRequestOperation(
                container =
                    HentKravgrunnlagDetaljerRequestContainer(
                        request =
                            HentKravgrunnlagDetaljerRequestData(
                                kodeAksjon = kodeAksjon,
                                kravgrunnlagId = kravgrunnlagId.toInt(),
                                enhetAnsvarlig = enhetAnsvarlig,
                                saksbehandlerId = saksbehandlerId,
                            ),
                    ),
            ),
    )

fun ZosHentKravgrunnlagDetaljerResponse.toDto(): HentKravgrunnlagDetaljerResponse {
    val resp = operation.container.response
    return HentKravgrunnlagDetaljerResponse(
        status = resp.status,
        melding = resp.statusMelding,
        kravgrunnlag =
            KravgrunnlagDetaljer(
                kravgrunnlagId = resp.kravgrunnlagId.toLong(),
                vedtakId = resp.vedtakId.toLong(),
                kodeStatusKrav = resp.kodeStatusKrav,
                kodeFagomraade = resp.kodeFagomraade,
                fagsystemId = resp.fagsystemId,
                datoVedtakFagsystem = resp.datoVedtakFagsystem,
                vedtakIdImgjort = resp.vedtakIdImgjort.toLong(),
                gjelderId = resp.gjelderId,
                typeGjelder = resp.typeGjelder,
                utbetalesTilId = resp.utbetalesTilId,
                typeUtbetalesTilId = resp.typeUtbetalesTilId,
                kodeHjemmel = resp.kodeHjemmel,
                renterBeregnes = resp.renterBeregnes,
                enhetAnsvarlig = resp.enhetAnsvarlig,
                enhetBosted = resp.enhetBosted,
                enhetBehandl = resp.enhetBehandl,
                kontrollfelt = resp.kontrollfelt,
                saksbehandlerId = resp.saksbehandlerId,
                referanse = resp.referanse,
                datoTilleggsfrist = resp.datoTilleggsfrist,
                perioder =
                    resp.tilbakekrevingsperiode.map { periode ->
                        KravgrunnlagPeriode(
                            periodeFom = periode.datoPeriodeFom,
                            periodeTom = periode.datoPeriodeTom,
                            belopSkattMnd = periode.belopSkattMnd,
                            posteringer =
                                periode.tilbakekrevingsbelop.map { belop ->
                                    KravgrunnlagPostering(
                                        kodeKlasse = belop.kodeKlasse,
                                        typeKlasse = belop.typeKlasse,
                                        belopOpprinneligUtbetalt = belop.belopOpprinneligUtbetalt,
                                        belopNy = belop.belopNy,
                                        belopTilbakekreves = belop.belopTilbakekreves,
                                        belopUinnkrevd = belop.belopUinnkrevd,
                                        skattProsent = belop.skattProsent,
                                        kodeResultat = belop.kodeResultat,
                                        kodeAarsak = belop.kodeAarsak,
                                        kodeSkyld = belop.kodeSkyld,
                                    )
                                },
                        )
                    },
            ),
    )
}

fun KravgrunnlagAnnulerRequest.toZosRequest(): ZosKravgrunnlagAnnulerRequest =
    ZosKravgrunnlagAnnulerRequest(
        operation =
            KravgrunnlagAnnulerRequestOperation(
                container =
                    KravgrunnlagAnnulerRequestContainer(
                        request =
                            KravgrunnlagAnnulerRequestData(
                                kodeAksjon = kodeAksjon,
                                vedtakId = vedtakId.toInt(),
                                enhetAnsvarlig = enhetAnsvarlig,
                                saksbehandlerId = saksbehandlerId,
                            ),
                    ),
            ),
    )

fun ZosKravgrunnlagAnnulerResponse.toDto(): KravgrunnlagAnnulerResponse {
    val resp = operation.container.response
    return KravgrunnlagAnnulerResponse(
        status = resp.status,
        melding = resp.statusMelding,
        vedtakId = resp.vedtakId.toLong(),
        saksbehandlerId = resp.saksbehandlerId,
    )
}
