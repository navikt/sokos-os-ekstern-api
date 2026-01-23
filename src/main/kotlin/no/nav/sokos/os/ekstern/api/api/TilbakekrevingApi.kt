package no.nav.sokos.os.ekstern.api.api

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
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
            call.handleRequest("sende tilbakekrevingsvedtak") {
                val request = call.receive<TilbakekrevingsvedtakRequest>()
                tilbakekrevingService.sendTilbakekrevingsvedtak(request)
            }
        }

        route("/kravgrunnlag") {
            post("/liste") {
                call.handleRequest("hente kravgrunnlag liste") {
                    val request = call.receive<HentKravgrunnlagRequest>()
                    tilbakekrevingService.hentKravgrunnlagListe(request)
                }
            }

            post("/detaljer") {
                call.handleRequest("hente kravgrunnlag detaljer") {
                    val request = call.receive<HentKravgrunnlagDetaljerRequest>()
                    tilbakekrevingService.hentKravgrunnlagDetaljer(request)
                }
            }

            post("/annuler") {
                call.handleRequest("annullere kravgrunnlag") {
                    val request = call.receive<KravgrunnlagAnnulerRequest>()
                    tilbakekrevingService.annulerKravgrunnlag(request)
                }
            }
        }
    }
}

private suspend fun ApplicationCall.handleRequest(
    operation: String,
    block: suspend () -> Any,
) {
    try {
        logger.info { "Mottatt request for å $operation" }
        val response = block()
        respond(HttpStatusCode.OK, response)
    } catch (e: TilbakekrevingException) {
        logger.error(e) { "Feil ved $operation: ${e.message}" }
        val statusCode = e.statusCode ?: HttpStatusCode.InternalServerError
        respond(statusCode, mapOf("error" to e.message))
    } catch (e: Exception) {
        logger.error(e) { "Uventet feil ved $operation: ${e.message}" }
        respond(HttpStatusCode.InternalServerError, mapOf("error" to "Uventet feil oppstod"))
    }
}
