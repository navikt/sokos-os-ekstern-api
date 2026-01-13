package no.nav.sokos.os.ekstern.api.service

import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import mu.KotlinLogging

import no.nav.sokos.os.ekstern.api.zOs.entitet.HentKravgrunnlagDetaljerResponse as ZosHentKravgrunnlagDetaljerResponse
import no.nav.sokos.os.ekstern.api.zOs.entitet.HentKravgrunnlagResponse as ZosHentKravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.zOs.entitet.KravgrunnlagAnnulerResponse as ZosKravgrunnlagAnnulerResponse
import no.nav.sokos.os.ekstern.api.zOs.entitet.TilbakekrevingsvedtakResponse as ZosTilbakekrevingsvedtakResponse
import no.nav.sokos.os.ekstern.api.dto.HentKravgrunnlagDetaljerRequest
import no.nav.sokos.os.ekstern.api.dto.HentKravgrunnlagDetaljerResponse
import no.nav.sokos.os.ekstern.api.dto.HentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.dto.HentKravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.dto.KravgrunnlagAnnulerRequest
import no.nav.sokos.os.ekstern.api.dto.KravgrunnlagAnnulerResponse
import no.nav.sokos.os.ekstern.api.dto.TilbakekrevingsvedtakRequest
import no.nav.sokos.os.ekstern.api.dto.TilbakekrevingsvedtakResponse
import no.nav.sokos.os.ekstern.api.dto.toDto
import no.nav.sokos.os.ekstern.api.dto.toZosRequest
import no.nav.sokos.os.ekstern.api.zOs.OsHttpClient

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
                val body = response.body<ZosTilbakekrevingsvedtakResponse>()
                logger.info { "Tilbakekrevingsvedtak sendt, status: ${body.operation.container.response.status}" }
                body.toDto()
            }
            else -> {
                logger.error { "Feil ved sending av tilbakekrevingsvedtak: ${response.status}" }
                throw TilbakekrevingException("Feil ved sending av tilbakekrevingsvedtak: ${response.status}")
            }
        }
    }

    suspend fun hentKravgrunnlagListe(request: HentKravgrunnlagRequest): HentKravgrunnlagResponse {
        logger.info { "Henter kravgrunnlag liste fra OS" }

        val zosRequest = request.toZosRequest()
        val response = osHttpClient.postHentKravgrunnlag(zosRequest)

        return when (response.status) {
            HttpStatusCode.OK -> {
                val body = response.body<ZosHentKravgrunnlagResponse>()
                logger.info { "Kravgrunnlag liste hentet, status: ${body.operation.container.response.status}" }
                body.toDto()
            }
            else -> {
                logger.error { "Feil ved henting av kravgrunnlag liste: ${response.status}" }
                throw TilbakekrevingException("Feil ved henting av kravgrunnlag liste: ${response.status}")
            }
        }
    }

    suspend fun hentKravgrunnlagDetaljer(request: HentKravgrunnlagDetaljerRequest): HentKravgrunnlagDetaljerResponse {
        logger.info { "Henter kravgrunnlag detaljer fra OS" }

        val zosRequest = request.toZosRequest()
        val response = osHttpClient.postHentKravgrunnlagDetaljer(zosRequest)

        return when (response.status) {
            HttpStatusCode.OK -> {
                val body = response.body<ZosHentKravgrunnlagDetaljerResponse>()
                logger.info { "Kravgrunnlag detaljer hentet, status: ${body.operation.container.response.status}" }
                body.toDto()
            }
            else -> {
                logger.error { "Feil ved henting av kravgrunnlag detaljer: ${response.status}" }
                throw TilbakekrevingException("Feil ved henting av kravgrunnlag detaljer: ${response.status}")
            }
        }
    }

    suspend fun annulerKravgrunnlag(request: KravgrunnlagAnnulerRequest): KravgrunnlagAnnulerResponse {
        logger.info { "Annullerer kravgrunnlag i OS" }

        val zosRequest = request.toZosRequest()
        val response = osHttpClient.postKravgrunnlagAnnuler(zosRequest)

        return when (response.status) {
            HttpStatusCode.OK -> {
                val body = response.body<ZosKravgrunnlagAnnulerResponse>()
                logger.info { "Kravgrunnlag annullert, status: ${body.operation.container.response.status}" }
                body.toDto()
            }
            else -> {
                logger.error { "Feil ved annullering av kravgrunnlag: ${response.status}" }
                throw TilbakekrevingException("Feil ved annullering av kravgrunnlag: ${response.status}")
            }
        }
    }
}

class TilbakekrevingException(
    message: String,
) : Exception(message)
