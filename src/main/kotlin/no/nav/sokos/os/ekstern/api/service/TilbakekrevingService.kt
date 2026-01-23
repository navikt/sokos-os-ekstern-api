package no.nav.sokos.os.ekstern.api.service

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import mu.KotlinLogging

import no.nav.sokos.os.ekstern.api.dto.annuler.KravgrunnlagAnnulerRequest
import no.nav.sokos.os.ekstern.api.dto.annuler.KravgrunnlagAnnulerResponse
import no.nav.sokos.os.ekstern.api.dto.detaljer.HentKravgrunnlagDetaljerRequest
import no.nav.sokos.os.ekstern.api.dto.detaljer.HentKravgrunnlagDetaljerResponse
import no.nav.sokos.os.ekstern.api.dto.kravgrunnlag.HentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.dto.kravgrunnlag.HentKravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.dto.toDto
import no.nav.sokos.os.ekstern.api.dto.toZosRequest
import no.nav.sokos.os.ekstern.api.dto.vedtak.TilbakekrevingsvedtakRequest
import no.nav.sokos.os.ekstern.api.dto.vedtak.TilbakekrevingsvedtakResponse
import no.nav.sokos.os.ekstern.api.os.OsHttpClient
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakResponse200

private val logger = KotlinLogging.logger {}

class TilbakekrevingService(
    private val osHttpClient: OsHttpClient,
) {
    suspend fun sendTilbakekrevingsvedtak(request: TilbakekrevingsvedtakRequest): TilbakekrevingsvedtakResponse =
        executeOsCall(
            operasjon = "sende tilbakekrevingsvedtak",
            call = { osHttpClient.postTilbakekrevingsvedtak(request.toZosRequest()) },
            transform = { it.body<PostOsTilbakekrevingsvedtakResponse200>().toDto() },
            logSuccess = { "Tilbakekrevingsvedtak sendt, status: ${it.status}, vedtakId: ${it.vedtakId}" },
        )

    suspend fun hentKravgrunnlagListe(request: HentKravgrunnlagRequest): HentKravgrunnlagResponse =
        executeOsCall(
            operasjon = "hente kravgrunnlag liste",
            call = { osHttpClient.postHentKravgrunnlag(request.toZosRequest()) },
            transform = { it.body<PostOsHentKravgrunnlagResponse200>().toDto() },
            logSuccess = { "Kravgrunnlag liste hentet, status: ${it.status}, antall: ${it.kravgrunnlagListe.size}" },
        )

    suspend fun hentKravgrunnlagDetaljer(request: HentKravgrunnlagDetaljerRequest): HentKravgrunnlagDetaljerResponse =
        executeOsCall(
            operasjon = "hente kravgrunnlag detaljer",
            call = { osHttpClient.postHentKravgrunnlagDetaljer(request.toZosRequest()) },
            transform = { it.body<PostOsHentKravgrunnlagDetaljerResponse200>().toDto() },
            logSuccess = { "Kravgrunnlag detaljer hentet, status: ${it.status}, kravgrunnlagId: ${it.kravgrunnlag.kravgrunnlagId}" },
        )

    suspend fun annulerKravgrunnlag(request: KravgrunnlagAnnulerRequest): KravgrunnlagAnnulerResponse =
        executeOsCall(
            operasjon = "annullere kravgrunnlag",
            call = { osHttpClient.postKravgrunnlagAnnuler(request.toZosRequest()) },
            transform = { it.body<PostOsKravgrunnlagAnnulerResponse200>().toDto() },
            logSuccess = { "Kravgrunnlag annullert, status: ${it.status}, vedtakId: ${it.vedtakId}" },
        )

    private suspend fun <T> executeOsCall(
        operasjon: String,
        call: suspend () -> HttpResponse,
        transform: suspend (HttpResponse) -> T,
        logSuccess: (T) -> String,
    ): T {
        logger.info { "Starter operasjon: $operasjon" }
        val response = call()

        return when (response.status) {
            HttpStatusCode.OK -> {
                val dto = transform(response)
                logger.info { logSuccess(dto) }
                dto
            }
            else -> {
                logger.error { "Feil ved $operasjon: ${response.status}" }
                throw TilbakekrevingException("Feil ved $operasjon: ${response.status}", response.status)
            }
        }
    }
}
