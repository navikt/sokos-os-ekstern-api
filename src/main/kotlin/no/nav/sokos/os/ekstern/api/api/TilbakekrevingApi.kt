package no.nav.sokos.os.ekstern.api.api

import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import mu.KotlinLogging

import no.nav.sokos.os.ekstern.api.api.models.annuler.AnnulerRequest
import no.nav.sokos.os.ekstern.api.api.models.detaljer.KravdetaljerRequest
import no.nav.sokos.os.ekstern.api.api.models.liste.KravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.api.models.vedtak.VedtakRequest
import no.nav.sokos.os.ekstern.api.service.AnnulerService
import no.nav.sokos.os.ekstern.api.service.DetaljerService
import no.nav.sokos.os.ekstern.api.service.KravgrunnlagService
import no.nav.sokos.os.ekstern.api.service.VedtakService

private val logger = KotlinLogging.logger {}

fun Route.tilbakekrevingApi(
    vedtakService: VedtakService = VedtakService(),
    annulerService: AnnulerService = AnnulerService(),
    detaljerService: DetaljerService = DetaljerService(),
    kravgrunnlagService: KravgrunnlagService = KravgrunnlagService(),
) {
    route("/api/v1/tilbakekreving") {
        post("/vedtak") {
            val request = call.receive<VedtakRequest>()
            vedtakService.postVedtak(request)
        }

        route("/kravgrunnlag") {
            post("/liste") {
                val request = call.receive<KravgrunnlagRequest>()
                kravgrunnlagService.postListe(request)
            }

            post("/detaljer") {
                val request = call.receive<KravdetaljerRequest>()
                detaljerService.postDetaljer(request)
            }

            post("/annuler") {
                val request = call.receive<AnnulerRequest>()
                annulerService.postAnnuler(request)
            }
        }
    }
}
