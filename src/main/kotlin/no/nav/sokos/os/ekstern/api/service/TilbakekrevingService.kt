package no.nav.sokos.os.ekstern.api.service

import io.ktor.client.call.body
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
    suspend fun sendTilbakekrevingsvedtak(request: TilbakekrevingsvedtakRequest): TilbakekrevingsvedtakResponse {
        logger.info { "Sender tilbakekrevingsvedtak request til OS" }

        val zosRequest = request.toZosRequest()
        val response = osHttpClient.postTilbakekrevingsvedtak(zosRequest)

        return when (response.status) {
            HttpStatusCode.OK -> {
                val body = response.body<PostOsTilbakekrevingsvedtakResponse200>()
                logger.info { "Tilbakekrevingsvedtak sendt, status: ${body.osTilbakekrevingsvedtakOperationResponse?.zt1OCont?.responsTilbakekrevingsvedtak?.status}" }
                body.toDto()
            }
            else -> {
                logger.error { "Feil ved sending av tilbakekrevingsvedtak: ${response.status}" }
                throw TilbakekrevingException("Feil ved sending av tilbakekrevingsvedtak: ${response.status}", response.status)
            }
        }
    }

    suspend fun hentKravgrunnlagListe(request: HentKravgrunnlagRequest): HentKravgrunnlagResponse {
        logger.info { "Henter kravgrunnlag liste fra OS" }

        val zosRequest = request.toZosRequest()
        val response = osHttpClient.postHentKravgrunnlag(zosRequest)

        return when (response.status) {
            HttpStatusCode.OK -> {
                val body = response.body<PostOsHentKravgrunnlagResponse200>()
                logger.info { "Kravgrunnlag liste hentet, status: ${body.osHentKravgrunnlagOperationResponse?.kravgrunnlagListe?.responsKravgrunnlagListe?.status}" }
                body.toDto()
            }
            else -> {
                logger.error { "Feil ved henting av kravgrunnlag liste: ${response.status}" }
                throw TilbakekrevingException("Feil ved henting av kravgrunnlag liste: ${response.status}", response.status)
            }
        }
    }

    suspend fun hentKravgrunnlagDetaljer(request: HentKravgrunnlagDetaljerRequest): HentKravgrunnlagDetaljerResponse {
        logger.info { "Henter kravgrunnlag detaljer fra OS" }

        val zosRequest = request.toZosRequest()
        val response = osHttpClient.postHentKravgrunnlagDetaljer(zosRequest)

        return when (response.status) {
            HttpStatusCode.OK -> {
                val body = response.body<PostOsHentKravgrunnlagDetaljerResponse200>()
                logger.info { "Kravgrunnlag detaljer hentet, status: ${body.osHentKravgrunnlagDetaljerOperationResponse?.kravgrunnlagsdetaljer?.responsDetaljer?.status}" }
                body.toDto()
            }
            else -> {
                logger.error { "Feil ved henting av kravgrunnlag detaljer: ${response.status}" }
                throw TilbakekrevingException("Feil ved henting av kravgrunnlag detaljer: ${response.status}", response.status)
            }
        }
    }

    suspend fun annulerKravgrunnlag(request: KravgrunnlagAnnulerRequest): KravgrunnlagAnnulerResponse {
        logger.info { "Annullerer kravgrunnlag i OS" }

        val zosRequest = request.toZosRequest()
        val response = osHttpClient.postKravgrunnlagAnnuler(zosRequest)

        return when (response.status) {
            HttpStatusCode.OK -> {
                val body = response.body<PostOsKravgrunnlagAnnulerResponse200>()
                logger.info { "Kravgrunnlag annullert, status: ${body.osKravgrunnlagAnnulerOperationResponse?.kravgrunnlagAnnulert?.responsKravgrunnlagAnnuler?.status}" }
                body.toDto()
            }
            else -> {
                logger.error { "Feil ved annullering av kravgrunnlag: ${response.status}" }
                throw TilbakekrevingException("Feil ved annullering av kravgrunnlag: ${response.status}", response.status)
            }
        }
    }
}

class TilbakekrevingException(
    message: String,
    val statusCode: HttpStatusCode? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
