package no.nav.sokos.os.ekstern.api.config

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.routing

import no.nav.sokos.os.ekstern.api.api.swaggerApi
import no.nav.sokos.os.ekstern.api.api.tilbakekrevingApi

fun Application.routingConfig(
    useAuthentication: Boolean,
    applicationState: ApplicationState,
) {
    routing {
        internalNaisRoutes(applicationState)
        swaggerApi()
        authenticate(useAuthentication, AUTHENTICATION_NAME) {
            tilbakekrevingApi() // TODO: tilbakekrevingApi() relies on default-constructed services (VedtakService(), KravgrunnlagService(), etc.), and each service default-constructs its own HttpClient via osHttpClient(...).
            // This creates multiple clients that are never closed, which can leak resources and waste connections. Prefer constructing a single shared OS HttpClient in routingConfig (or a DI module), passing it into
            // the services, and closing it on ApplicationStopped.
        }
    }
}

fun Route.authenticate(
    useAuthentication: Boolean,
    authenticationProviderId: String? = null,
    block: Route.() -> Unit,
) {
    if (useAuthentication) authenticate(authenticationProviderId) { block() } else block()
}
