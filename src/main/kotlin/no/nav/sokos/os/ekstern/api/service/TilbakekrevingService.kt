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
import no.nav.sokos.os.ekstern.api.zOs.OsHttpClient
import no.nav.sokos.os.ekstern.api.zOs.entitet.annuler.OsKravgrunnlagAnnulerResponse
import no.nav.sokos.os.ekstern.api.zOs.entitet.detaljer.OsHentKravgrunnlagDetaljerResponse
import no.nav.sokos.os.ekstern.api.zOs.entitet.kravgrunnlag.OsHentKravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.zOs.entitet.vedtak.OsTilbakekrevingsvedtakResponse

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
                val body = response.body<OsTilbakekrevingsvedtakResponse>()
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
                val body = response.body<OsHentKravgrunnlagResponse>()
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
                val body = response.body<OsHentKravgrunnlagDetaljerResponse>()
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
                val body = response.body<OsKravgrunnlagAnnulerResponse>()
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
