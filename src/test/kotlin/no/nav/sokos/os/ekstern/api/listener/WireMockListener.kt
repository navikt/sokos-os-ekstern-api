package no.nav.sokos.os.ekstern.api.listener

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase

object WireMockListener : TestListener {
    val wiremock = WireMockServer(WireMockConfiguration.options().dynamicPort())

    override suspend fun beforeSpec(spec: Spec) {
        wiremock.start()
    }

    override suspend fun afterSpec(spec: Spec) {
        wiremock.stop()
    }

    override suspend fun beforeTest(testCase: TestCase) {
        wiremock.resetAll()
    }
}
