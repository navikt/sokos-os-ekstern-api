package no.nav.sokos.os.ekstern.api.api

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import mu.KotlinLogging

import no.nav.sokos.os.ekstern.api.api.models.annuler.AnnulerRequest
import no.nav.sokos.os.ekstern.api.api.models.detaljer.KravdetaljerRequest
import no.nav.sokos.os.ekstern.api.api.models.liste.KravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.api.models.vedtak.VedtakRequest
import no.nav.sokos.os.ekstern.api.os.AnnulerService
import no.nav.sokos.os.ekstern.api.os.DetaljerService
import no.nav.sokos.os.ekstern.api.os.KravgrunnlagService
import no.nav.sokos.os.ekstern.api.os.VedtakService

private val logger = KotlinLogging.logger {}

const val API_BASE_PATH = "/api/v1/tilbakekreving"

fun Route.tilbakekrevingApi(
    vedtakService: VedtakService = VedtakService(),
    annulerService: AnnulerService = AnnulerService(),
    detaljerService: DetaljerService = DetaljerService(),
    kravgrunnlagService: KravgrunnlagService = KravgrunnlagService(),
) {
    route(API_BASE_PATH) {
        post("/vedtak") {
            val request = call.receive<VedtakRequest>()
            val response = vedtakService.postVedtak(request)
            call.respond(response)
        }

        route("/kravgrunnlag") {
            post("/liste") {
                val request = call.receive<KravgrunnlagRequest>()
                val response = kravgrunnlagService.postListe(request)
                call.respond(response)
            }

            post("/detaljer") {
                val request = call.receive<KravdetaljerRequest>()
                val response = detaljerService.postDetaljer(request)
                call.respond(response)
            }

            post("/annuler") {
                val request = call.receive<AnnulerRequest>()
                val response = annulerService.postAnnuler(request)
                call.respond(response)
            }
        }
    }
}
