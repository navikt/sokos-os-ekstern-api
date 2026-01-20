package no.nav.sokos.os.ekstern.api.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import mu.KotlinLogging

import no.nav.sokos.os.ekstern.api.dto.annuler.KravgrunnlagAnnulerRequest
import no.nav.sokos.os.ekstern.api.dto.detaljer.HentKravgrunnlagDetaljerRequest
import no.nav.sokos.os.ekstern.api.dto.kravgrunnlag.HentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.dto.vedtak.TilbakekrevingsvedtakRequest
import no.nav.sokos.os.ekstern.api.service.TilbakekrevingException
import no.nav.sokos.os.ekstern.api.service.TilbakekrevingService

private val logger = KotlinLogging.logger {}

fun Route.tilbakekrevingApi(tilbakekrevingService: TilbakekrevingService) {
    route("/api/v1/tilbakekreving") {
        post("/vedtak") {
            try {
                logger.info { "Mottatt request for å sende tilbakekrevingsvedtak" }
                val request = call.receive<TilbakekrevingsvedtakRequest>()
                val response = tilbakekrevingService.sendTilbakekrevingsvedtak(request)
                call.respond(HttpStatusCode.OK, response)
            } catch (e: TilbakekrevingException) {
                logger.error(e) { "Feil ved sending av tilbakekrevingsvedtak: ${e.message}" }
                val statusCode = e.statusCode ?: HttpStatusCode.InternalServerError
                call.respond(statusCode, mapOf("error" to e.message))
            } catch (e: Exception) {
                logger.error(e) { "Uventet feil ved sending av tilbakekrevingsvedtak: ${e.message}" }
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Uventet feil oppstod"))
            }
        }

        route("/kravgrunnlag") {
            post("/liste") {
                try {
                    logger.info { "Mottatt request for å hente kravgrunnlag liste" }
                    val request = call.receive<HentKravgrunnlagRequest>()
                    val response = tilbakekrevingService.hentKravgrunnlagListe(request)
                    call.respond(HttpStatusCode.OK, response)
                } catch (e: TilbakekrevingException) {
                    logger.error(e) { "Feil ved henting av kravgrunnlag liste: ${e.message}" }
                    val statusCode = e.statusCode ?: HttpStatusCode.InternalServerError
                    call.respond(statusCode, mapOf("error" to e.message))
                } catch (e: Exception) {
                    logger.error(e) { "Uventet feil ved henting av kravgrunnlag liste: ${e.message}" }
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Uventet feil oppstod"))
                }
            }

            post("/detaljer") {
                try {
                    logger.info { "Mottatt request for å hente kravgrunnlag detaljer" }
                    val request = call.receive<HentKravgrunnlagDetaljerRequest>()
                    val response = tilbakekrevingService.hentKravgrunnlagDetaljer(request)
                    call.respond(HttpStatusCode.OK, response)
                } catch (e: TilbakekrevingException) {
                    logger.error(e) { "Feil ved henting av kravgrunnlag detaljer: ${e.message}" }
                    val statusCode = e.statusCode ?: HttpStatusCode.InternalServerError
                    call.respond(statusCode, mapOf("error" to e.message))
                } catch (e: Exception) {
                    logger.error(e) { "Uventet feil ved henting av kravgrunnlag detaljer: ${e.message}" }
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Uventet feil oppstod"))
                }
            }

            post("/annuler") {
                try {
                    logger.info { "Mottatt request for å annullere kravgrunnlag" }
                    val request = call.receive<KravgrunnlagAnnulerRequest>()
                    val response = tilbakekrevingService.annulerKravgrunnlag(request)
                    call.respond(HttpStatusCode.OK, response)
                } catch (e: TilbakekrevingException) {
                    logger.error(e) { "Feil ved annullering av kravgrunnlag: ${e.message}" }
                    val statusCode = e.statusCode ?: HttpStatusCode.InternalServerError
                    call.respond(statusCode, mapOf("error" to e.message))
                } catch (e: Exception) {
                    logger.error(e) { "Uventet feil ved annullering av kravgrunnlag: ${e.message}" }
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Uventet feil oppstod"))
                }
            }
        }
    }
}
