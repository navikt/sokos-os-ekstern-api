package no.nav.sokos.os.ekstern.api.api

import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import mu.KotlinLogging

import no.nav.sokos.os.ekstern.api.dto.annuler.KravgrunnlagAnnulerRequest
import no.nav.sokos.os.ekstern.api.dto.detaljer.HentKravgrunnlagDetaljerRequest
import no.nav.sokos.os.ekstern.api.dto.kravgrunnlag.HentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.dto.vedtak.TilbakekrevingsvedtakRequest
import no.nav.sokos.os.ekstern.api.service.TilbakekrevingService
import no.nav.sokos.os.ekstern.api.service.VedtakService

private val logger = KotlinLogging.logger {}

fun Route.tilbakekrevingApi(
    tilbakekrevingService: TilbakekrevingService = TilbakekrevingService(),
    vedtakService: VedtakService = VedtakService(),
) {
    route("/api/v1/tilbakekreving") {
        post("/vedtak") {
            val request = call.receive<TilbakekrevingsvedtakRequest>()
            vedtakService.postVedtak(request)
        }

        route("/kravgrunnlag") {
            post("/liste") {
                val request = call.receive<HentKravgrunnlagRequest>()
                tilbakekrevingService.hentKravgrunnlagListe(request)
            }

            post("/detaljer") {
                val request = call.receive<HentKravgrunnlagDetaljerRequest>()
                tilbakekrevingService.hentKravgrunnlagDetaljer(request)
            }

            post("/annuler") {
                val request = call.receive<KravgrunnlagAnnulerRequest>()
                tilbakekrevingService.annulerKravgrunnlag(request)
            }
        }
    }
}
