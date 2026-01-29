package no.nav.sokos.os.ekstern.api.security

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.mockk

import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import no.nav.security.mock.oauth2.withMockOAuth2Server
import no.nav.sokos.os.ekstern.api.API_BASE_PATH
import no.nav.sokos.os.ekstern.api.api.tilbakekrevingApi
import no.nav.sokos.os.ekstern.api.config.AUTHENTICATION_NAME
import no.nav.sokos.os.ekstern.api.config.PropertiesConfig
import no.nav.sokos.os.ekstern.api.config.authenticate
import no.nav.sokos.os.ekstern.api.config.commonConfig
import no.nav.sokos.os.ekstern.api.config.jsonConfig
import no.nav.sokos.os.ekstern.api.config.securityConfig
import no.nav.sokos.os.ekstern.api.service.TilbakekrevingService

private const val TILBAKEKREVING_BASE_API_PATH = "$API_BASE_PATH/tilbakekreving"
val tilbakekrevingService: TilbakekrevingService = mockk()

class SecurityTest :
    FunSpec({

        val endpoints =
            listOf(
                "$TILBAKEKREVING_BASE_API_PATH/vedtak",
                "$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/liste",
                "$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/detaljer",
                "$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/annuler",
            )

        test("alle endepunkter med token returnerer 200 OK") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client =
                        createClient {
                            install(ContentNegotiation) {
                                json(
                                    jsonConfig,
                                )
                            }
                        }
                    application {
                        configureTestApplication(mockOAuth2Server, tilbakekrevingService)
                    }

                    endpoints.forEach { endpoint ->
                        val response =
                            client.post(endpoint) {
                                header("Authorization", "Bearer ${mockOAuth2Server.tokenFromDefaultProvider()}")
                                contentType(ContentType.Application.Json)
                                setBody("{  }")
                            }
                        response.status shouldBe HttpStatusCode.InternalServerError
                    }
                }
            }
        }

        test("alle endepunkter uten token returnerer 401 Unauthorized") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client =
                        createClient {
                            install(ContentNegotiation) {
                                json(
                                    jsonConfig,
                                )
                            }
                        }
                    application {
                        configureTestApplication(mockOAuth2Server, tilbakekrevingService)
                    }

                    endpoints.forEach { endpoint ->
                        val response =
                            client.post(endpoint) {
                                contentType(ContentType.Application.Json)
                            }

                        response.status shouldBe HttpStatusCode.Unauthorized
                    }
                }
            }
        }
    })

private fun MockOAuth2Server.authConfig() =
    PropertiesConfig.AzureAdProperties(
        wellKnownUrl = wellKnownUrl("default").toString(),
        clientId = "default",
    )

private fun MockOAuth2Server.tokenFromDefaultProvider() =
    issueToken(
        issuerId = "default",
        clientId = "default",
        tokenCallback = DefaultOAuth2TokenCallback(),
    ).serialize()

private fun Application.configureTestApplication(
    mockOAuth2Server: MockOAuth2Server,
    tilbakekrevingService: TilbakekrevingService,
) {
    commonConfig()
    securityConfig(true, mockOAuth2Server.authConfig())
    routing {
        authenticate(true, AUTHENTICATION_NAME) {
            tilbakekrevingApi(tilbakekrevingService)
        }
    }
}
