package no.nav.sokos.os.ekstern.api.service

import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import mu.KotlinLogging

import no.nav.sokos.os.ekstern.api.domain.HentKravgrunnlagDetaljerRequest
import no.nav.sokos.os.ekstern.api.domain.HentKravgrunnlagDetaljerResponse
import no.nav.sokos.os.ekstern.api.domain.HentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.domain.HentKravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.domain.KravgrunnlagAnnulerRequest
import no.nav.sokos.os.ekstern.api.domain.KravgrunnlagAnnulerResponse
import no.nav.sokos.os.ekstern.api.domain.TilbakekrevingsvedtakRequest
import no.nav.sokos.os.ekstern.api.domain.TilbakekrevingsvedtakResponse
import no.nav.sokos.os.ekstern.api.zOs.OsHttpClient

private val logger = KotlinLogging.logger {}

class TilbakekrevingService(
    private val osHttpClient: OsHttpClient,
) {
    suspend fun sendTilbakekrevingsvedtak(request: TilbakekrevingsvedtakRequest): TilbakekrevingsvedtakResponse {
        logger.info { "Sender tilbakekrevingsvedtak request til OS" }

        val response = osHttpClient.postTilbakekrevingsvedtak(request)

        return when (response.status) {
            HttpStatusCode.OK -> {
                val body = response.body<TilbakekrevingsvedtakResponse>()
                logger.info { "Tilbakekrevingsvedtak sendt, status: ${body.operationResponse.container.response.status}" }
                body
            }
            else -> {
                logger.error { "Feil ved sending av tilbakekrevingsvedtak: ${response.status}" }
                throw TilbakekrevingException("Feil ved sending av tilbakekrevingsvedtak: ${response.status}")
            }
        }
    }

    suspend fun hentKravgrunnlagListe(request: HentKravgrunnlagRequest): HentKravgrunnlagResponse {
        logger.info { "Henter kravgrunnlag liste fra OS" }

        val response = osHttpClient.postHentKravgrunnlag(request)

        return when (response.status) {
            HttpStatusCode.OK -> {
                val body = response.body<HentKravgrunnlagResponse>()
                logger.info { "Kravgrunnlag liste hentet, status: ${body.operationResponse.kravgrunnlagListe.response.status}" }
                body
            }
            else -> {
                logger.error { "Feil ved henting av kravgrunnlag liste: ${response.status}" }
                throw TilbakekrevingException("Feil ved henting av kravgrunnlag liste: ${response.status}")
            }
        }
    }

    suspend fun hentKravgrunnlagDetaljer(request: HentKravgrunnlagDetaljerRequest): HentKravgrunnlagDetaljerResponse {
        logger.info { "Henter kravgrunnlag detaljer fra OS" }

        val response = osHttpClient.postHentKravgrunnlagDetaljer(request)

        return when (response.status) {
            HttpStatusCode.OK -> {
                val body = response.body<HentKravgrunnlagDetaljerResponse>()
                logger.info { "Kravgrunnlag detaljer hentet, status: ${body.operationResponse.kravgrunnlagsdetaljer.response.status}" }
                body
            }
            else -> {
                logger.error { "Feil ved henting av kravgrunnlag detaljer: ${response.status}" }
                throw TilbakekrevingException("Feil ved henting av kravgrunnlag detaljer: ${response.status}")
            }
        }
    }

    suspend fun annulerKravgrunnlag(request: KravgrunnlagAnnulerRequest): KravgrunnlagAnnulerResponse {
        logger.info { "Annullerer kravgrunnlag i OS" }

        val response = osHttpClient.postKravgrunnlagAnnuler(request)

        return when (response.status) {
            HttpStatusCode.OK -> {
                val body = response.body<KravgrunnlagAnnulerResponse>()
                logger.info { "Kravgrunnlag annullert, status: ${body.operationResponse.kravgrunnlagAnnulert.response.status}" }
                body
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
