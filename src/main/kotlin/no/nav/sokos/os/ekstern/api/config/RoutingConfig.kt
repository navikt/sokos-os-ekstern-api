package no.nav.sokos.os.ekstern.api.config

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.routing

import no.nav.sokos.os.ekstern.api.api.swaggerApi
import no.nav.sokos.os.ekstern.api.api.tilbakekrevingApi
import no.nav.sokos.os.ekstern.api.os.OsHttpClient
import no.nav.sokos.os.ekstern.api.service.TilbakekrevingService

fun Application.routingConfig(
    useAuthentication: Boolean,
    applicationState: ApplicationState,
    osConfiguration: PropertiesConfig.OsConfiguration,
) {
    val osHttpClient = OsHttpClient(osConfiguration)
    val tilbakekrevingService = TilbakekrevingService(osHttpClient)

    routing {
        internalNaisRoutes(applicationState)
        swaggerApi()
        authenticate(useAuthentication, AUTHENTICATION_NAME) {
            tilbakekrevingApi()
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
