package no.nav.sokos.os.ekstern.api

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
            kodeAksjon = "NY",
            vedtakId = 123456789L,
            vedtaksDato = "2026-01-15",
            kodeHjemmel = "FVL-22",
            renterBeregnes = true,
            enhetAnsvarlig = "8020",
            kontrollfelt = "2026-01-15-01.12.34.123456",
            saksbehandlerId = "Z999999",
            datoTilleggsfrist = null,
            perioder =
                listOf(
                    Periode(
                        periodeFom = "2026-01-01",
                        periodeTom = "2026-01-31",
                        renterPeriodeBeregnes = true,
                        belopRenter = 150.50,
                        posteringer =
                            listOf(
                                Postering(
                                    kodeKlasse = "KL_KODE_CLASS1",
                                    belopOpprinneligUtbetalt = 10000.00,
                                    belopNy = 8000.00,
                                    belopTilbakekreves = 2000.00,
                                    belopUinnkrevd = 0.00,
                                    belopSkatt = 500.00,
                                    kodeResultat = "FULL_TILBAKEKREV",
                                    kodeAarsak = "FEIL_OPPLYSNINGER",
                                    kodeSkyld = "BRUKER",
                                ),
                            ),
                    ),
                ),
        )

    val vedtakResponse =
        VedtakResponse(
            status = 200,
            melding = "Vedtak sendt",
            vedtakId = 123456789,
            datoVedtakFagsystem = "2026-01-15",
        )

    val kravListeRequest =
        KravgrunnlagRequest(
            kodeAksjon = "HENT",
            gjelderId = "12345678901",
            typeGjelder = "PERSON",
            utbetalesTilId = "12345678901",
            typeUtbetalesTil = "PERSON",
            enhetAnsvarlig = "8020",
            kodeFaggruppe = "PEN",
            kodeFagomraade = "PENALD",
            fagsystemId = "FS123456",
            kravgrunnlagId = null,
            saksbehandlerId = "Z999999",
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
            belopSumFeilutbetalt = 10000.00,
        )

    val kravgrunnlagResponse =
        KravgrunnlagResponse(
            status = 200,
            melding = "Kravgrunnlag hentet",
            kravgrunnlagListe = listOf(kravgrunnlag),
        )

    val kravDetaljerRequest =
        KravdetaljerRequest(
            kodeAksjon = "HENT_DETALJER",
            kravgrunnlagId = 123456,
            enhetAnsvarlig = "8020",
            saksbehandlerId = "Z999999",
        )

    val kravgrunnlagDetaljer =
        KravgrunnlagDetaljer(
            kravgrunnlagId = 123456L,
            vedtakId = 123456789L,
            kodeStatusKrav = "NY",
            kodeFagomraade = "PENALD",
            fagsystemId = "FS123456",
            datoVedtakFagsystem = "2026-01-15",
            vedtakIdImgjort = 123456788L,
            gjelderId = "12345678901",
            typeGjelder = "PERSON",
            utbetalesTilId = "12345678901",
            typeUtbetalesTilId = "PERSON",
            kodeHjemmel = "FVL-22",
            renterBeregnes = true,
            enhetAnsvarlig = "8020",
            enhetBosted = "0219",
            enhetBehandl = "8020",
            kontrollfelt = "2026-01-15-01.12.34.123456",
            saksbehandlerId = "Z999999",
            referanse = "REF123456",
            datoTilleggsfrist = null,
            perioder =
                listOf(
                    DetaljerPeriode(
                        periodeFom = "2026-01-01",
                        periodeTom = "2026-01-31",
                        belopSkattMnd = 500.00,
                        posteringer =
                            listOf(
                                DetaljerPostering(
                                    kodeKlasse = "KL_KODE_CLASS1",
                                    typeKlasse = "YTEL",
                                    belopOpprinneligUtbetalt = 10000.00,
                                    belopNy = 8000.00,
                                    belopTilbakekreves = 2000.00,
                                    belopUinnkrevd = 0.00,
                                    skattProsent = 25.0,
                                    kodeResultat = "FULL_TILBAKEKREV",
                                    kodeAarsak = "FEIL_OPPLYSNINGER",
                                    kodeSkyld = "BRUKER",
                                ),
                            ),
                    ),
                ),
        )

    val kravDetaljerResponse =
        KravdetaljerResponse(
            status = 200,
            melding = "Kravgrunnlag detaljer hentet",
            kravgrunnlag = kravgrunnlagDetaljer,
        )

    val annulerRequest =
        AnnulerRequest(
            kodeAksjon = "ANNULER",
            vedtakId = 123456789L,
            enhetAnsvarlig = "8020",
            saksbehandlerId = "Z999999",
        )

    val annulerResponse =
        AnnulerResponse(
            status = 200,
            melding = "Kravgrunnlag annullert",
            vedtakId = 123456789,
            saksbehandlerId = "Z999999",
        )
}
