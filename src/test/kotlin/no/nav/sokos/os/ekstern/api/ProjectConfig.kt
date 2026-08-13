package no.nav.sokos.os.ekstern.api

import io.kotest.core.config.AbstractProjectConfig
import io.ktor.server.config.ApplicationConfig

import no.nav.sokos.os.ekstern.api.config.PropertiesConfigNew

class ProjectConfig : AbstractProjectConfig() {
    override suspend fun beforeProject() {
        PropertiesConfigNew.load(ApplicationConfig("application-test.conf"))
    }
}
