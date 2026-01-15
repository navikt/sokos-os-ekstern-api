package no.nav.sokos.os.ekstern.api.api

import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.Routing

fun Routing.swaggerApi() {
    swaggerUI(
        path = "api/v1/tilbakekreving/docs",
        swaggerFile = "spec/tilbakekreving-v1-swagger.yaml",
    )
    swaggerUI(
        path = "ostilbakekrevingapi/docs",
        swaggerFile = "spec/os-tilbakekreving-swagger.json",
    )
}
