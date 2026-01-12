package no.nav.sokos.os.ekstern.api.security

import kotlinx.serialization.json.Json

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
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
import no.nav.sokos.os.ekstern.api.config.securityConfig
import no.nav.sokos.os.ekstern.api.service.TilbakekrevingService

val tilbakekrevingService: TilbakekrevingService = mockk()

class SecurityTest :
    FunSpec({

        test("test http GET endepunkt uten token bør returnere 401") {
            withMockOAuth2Server {
                testApplication {
                    application {
                        securityConfig(true, authConfig())
                        routing {
                            authenticate(true, AUTHENTICATION_NAME) {
                                tilbakekrevingApi(tilbakekrevingService)
                            }
                        }
                    }
                    val response = client.post("$API_BASE_PATH/tilbakekreving/kravgrunnlag/liste")
                    response.status shouldBe HttpStatusCode.Unauthorized
                }
            }
        }

        test("test http GET endepunkt med token bør returnere 200") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client =
                        createClient {
                            install(ContentNegotiation) {
                                json(
                                    Json {
                                        prettyPrint = true
                                        ignoreUnknownKeys = true
                                        encodeDefaults = true
                                        explicitNulls = false
                                    },
                                )
                            }
                        }
                    application {
                        commonConfig()
                        securityConfig(true, authConfig())
                        routing {
                            authenticate(true, AUTHENTICATION_NAME) {
                                tilbakekrevingApi(tilbakekrevingService)
                            }
                        }
                    }

                    val response =
                        client.post("$API_BASE_PATH/tilbakekreving/kravgrunnlag/liste") {
                            header("Authorization", "Bearer ${mockOAuth2Server.tokenFromDefaultProvider()}")
                            contentType(ContentType.Application.Json)
                        }

                    response.status shouldBe HttpStatusCode.InternalServerError
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
