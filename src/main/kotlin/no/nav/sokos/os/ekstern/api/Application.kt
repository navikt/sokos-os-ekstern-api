package no.nav.sokos.os.ekstern.api

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

import no.nav.sokos.os.ekstern.api.config.ApplicationState
import no.nav.sokos.os.ekstern.api.config.PropertiesConfig
import no.nav.sokos.os.ekstern.api.config.applicationLifecycleConfig
import no.nav.sokos.os.ekstern.api.config.commonConfig
import no.nav.sokos.os.ekstern.api.config.routingConfig
import no.nav.sokos.os.ekstern.api.config.securityConfig

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(true)
}

fun Application.module() {
    val useAuthentication = PropertiesConfig.Configuration().useAuthentication
    val applicationState = ApplicationState()

    applicationLifecycleConfig(applicationState)
    commonConfig()
    securityConfig(useAuthentication)
    routingConfig(useAuthentication, applicationState)
}
