package no.nav.sokos.os.ekstern.api

import java.math.BigDecimal

import no.nav.sokos.os.ekstern.api.api.models.annuler.AnnulerRequest
import no.nav.sokos.os.ekstern.api.api.models.annuler.AnnulerResponse
import no.nav.sokos.os.ekstern.api.api.models.detaljer.DetaljerPeriode
import no.nav.sokos.os.ekstern.api.api.models.detaljer.DetaljerPostering
import no.nav.sokos.os.ekstern.api.api.models.detaljer.KravdetaljerRequest
import no.nav.sokos.os.ekstern.api.api.models.detaljer.KravdetaljerResponse
import no.nav.sokos.os.ekstern.api.api.models.detaljer.KravgrunnlagDetaljer
import no.nav.sokos.os.ekstern.api.api.models.liste.Kravgrunnlag
import no.nav.sokos.os.ekstern.api.api.models.liste.KravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.api.models.liste.KravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.api.models.vedtak.Periode
import no.nav.sokos.os.ekstern.api.api.models.vedtak.Postering
import no.nav.sokos.os.ekstern.api.api.models.vedtak.VedtakRequest
import no.nav.sokos.os.ekstern.api.api.models.vedtak.VedtakResponse

object Testdata {
    val vedtakRequest =
        VedtakRequest(
            kodeAksjon = "8",
            vedtakId = 892793,
            vedtaksDato = "2026-02-11",
            kodeHjemmel = "22-15",
            renterBeregnes = false,
            enhetAnsvarlig = "8020",
            kontrollfelt = "2025-11-27-00.19.14.863826",
            saksbehandlerId = "Z999999",
            datoTilleggsfrist = null,
            perioder =
                listOf(
                    Periode(
                        periodeFom = "2025-04-01",
                        periodeTom = "2025-04-25",
                        renterPeriodeBeregnes = false,
                        belopRenter = BigDecimal.ZERO,
                        posteringer =
                            listOf(
                                Postering(
                                    kodeKlasse = "SPATORD",
                                    belopOpprinneligUtbetalt = BigDecimal.valueOf(551),
                                    belopNy = BigDecimal.valueOf(532),
                                    belopTilbakekreves = BigDecimal.valueOf(19),
                                    belopUinnkrevd = BigDecimal.ZERO,
                                    belopSkatt = BigDecimal.ZERO,
                                    kodeResultat = "FULL_TILBAKEKREV",
                                    kodeAarsak = "ANNET",
                                    kodeSkyld = "NAV",
                                ),
                            ),
                    ),
                ),
        )

    val vedtakResponse =
        VedtakResponse(
            status = 0,
            melding = "",
            vedtakId = 892793,
            datoVedtakFagsystem = "2026-02-11",
        )

    val kravListeRequest =
        KravgrunnlagRequest(
            kodeAksjon = "3",
            gjelderId = "",
            typeGjelder = "",
            utbetalesTilId = "",
            typeUtbetalesTil = "",
            enhetAnsvarlig = "4819",
            kodeFaggruppe = "",
            kodeFagomraade = "UFOREUT",
            fagsystemId = "20153917",
            kravgrunnlagId = null,
            saksbehandlerId = "Test",
        )

    val kravgrunnlag =
        Kravgrunnlag(
            kravgrunnlagId = 123456L,
            kodeStatusKrav = "NY",
            gjelderId = "12345678901",
            typeGjelder = "PERSON",
            utbetalesTilId = "12345678901",
            typeUtbetalesTil = "PERSON",
            kodeFagomraade = "PENALD",
            fagsystemId = "FS123456",
            datoVedtakFagsystem = "2026-01-15",
            enhetBosted = "0219",
            enhetAnsvarlig = "8020",
            datoKravDannet = "2026-01-15",
            datoPeriodeFom = "2026-01-01",
            datoPeriodeTom = "2026-01-31",
            belopSumFeilutbetalt = BigDecimal.valueOf(10000.00),
        )

    val kravgrunnlagResponse =
        KravgrunnlagResponse(
            status = 0,
            melding = "",
            kravgrunnlagListe = listOf(kravgrunnlag),
        )

    val kravDetaljerRequest =
        KravdetaljerRequest(
            kodeAksjon = "5",
            kravgrunnlagId = 570598,
            enhetAnsvarlig = "4819",
            saksbehandlerId = "TEST",
        )

    val kravgrunnlagDetaljer =
        KravgrunnlagDetaljer(
            kravgrunnlagId = 570598L,
            vedtakId = 851138L,
            kodeStatusKrav = "BEHA",
            kodeFagomraade = "FP",
            fagsystemId = "152465243100",
            datoVedtakFagsystem = "",
            vedtakIdImgjort = 851138L,
            gjelderId = "98765432111",
            typeGjelder = "PERSON",
            utbetalesTilId = "98765432111",
            typeUtbetalesTilId = "PERSON",
            kodeHjemmel = "",
            renterBeregnes = false,
            enhetAnsvarlig = "8020",
            enhetBosted = "8020",
            enhetBehandl = "8020",
            kontrollfelt = "2025-10-28-22.57.21.466798",
            saksbehandlerId = "Z999999",
            referanse = "3650158",
            datoTilleggsfrist = null,
            perioder =
                listOf(
                    DetaljerPeriode(
                        periodeFom = "2025-08-11",
                        periodeTom = "2025-08-31",
                        belopSkattMnd = BigDecimal.valueOf(6783),
                        posteringer =
                            listOf(
                                DetaljerPostering(
                                    kodeKlasse = "FPATORD",
                                    typeKlasse = "YTEL",
                                    belopOpprinneligUtbetalt = BigDecimal.valueOf(31005),
                                    belopNy = BigDecimal.ZERO,
                                    belopTilbakekreves = BigDecimal.valueOf(31005),
                                    belopUinnkrevd = BigDecimal.ZERO,
                                    skattProsent = BigDecimal.valueOf(21.8771),
                                    kodeResultat = "",
                                    kodeAarsak = "",
                                    kodeSkyld = "",
                                ),
                            ),
                    ),
                ),
        )

    val kravDetaljerResponse =
        KravdetaljerResponse(
            status = 0,
            melding = "",
            kravgrunnlag = kravgrunnlagDetaljer,
        )

    val annulerRequest =
        AnnulerRequest(
            kodeAksjon = "4",
            vedtakId = 123456789,
            enhetAnsvarlig = "8020",
            saksbehandlerId = "Z999999",
        )

    val annulerResponse =
        AnnulerResponse(
            status = 0,
            melding = "",
            vedtakId = 123456789,
            saksbehandlerId = "Z999999",
        )
}
