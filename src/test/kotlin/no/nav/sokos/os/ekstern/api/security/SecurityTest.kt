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
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk

import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import no.nav.security.mock.oauth2.withMockOAuth2Server
import no.nav.sokos.os.ekstern.api.Testdata
import no.nav.sokos.os.ekstern.api.api.API_BASE_PATH
import no.nav.sokos.os.ekstern.api.api.tilbakekrevingApi
import no.nav.sokos.os.ekstern.api.config.AUTHENTICATION_NAME
import no.nav.sokos.os.ekstern.api.config.PropertiesConfig
import no.nav.sokos.os.ekstern.api.config.commonConfig
import no.nav.sokos.os.ekstern.api.config.jsonConfig
import no.nav.sokos.os.ekstern.api.config.securityConfig
import no.nav.sokos.os.ekstern.api.os.AnnulerService
import no.nav.sokos.os.ekstern.api.os.DetaljerService
import no.nav.sokos.os.ekstern.api.os.KravgrunnlagService
import no.nav.sokos.os.ekstern.api.os.VedtakService

val vedtakService: VedtakService = mockk()
val kravgrunnlagService: KravgrunnlagService = mockk()
val detaljerService: DetaljerService = mockk()
val annulerService: AnnulerService = mockk()

class SecurityTest :
    FunSpec({

        val endpoints =
            listOf(
                "$API_BASE_PATH/vedtak",
                "$API_BASE_PATH/kravgrunnlag/liste",
                "$API_BASE_PATH/kravgrunnlag/detaljer",
                "$API_BASE_PATH/kravgrunnlag/annuler",
            )

        test("alle endepunkter med token returnerer 200 OK") {

            coEvery { vedtakService.postVedtak(any()) } returns Testdata.vedtakResponse
            coEvery { kravgrunnlagService.postListe(any()) } returns Testdata.kravgrunnlagResponse
            coEvery { detaljerService.postDetaljer(any()) } returns Testdata.kravDetaljerResponse
            coEvery { annulerService.postAnnuler(any()) } returns Testdata.annulerResponse

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
                        configureTestApplication(
                            mockOAuth2Server,
                            vedtakService,
                            kravgrunnlagService,
                            detaljerService,
                            annulerService,
                        )
                    }

                    val requestBodies =
                        listOf(
                            Testdata.vedtakRequest,
                            Testdata.kravListeRequest,
                            Testdata.kravDetaljerRequest,
                            Testdata.annulerRequest,
                        )

                    endpoints.zip(requestBodies).forEach { (endpoint, body) ->
                        val response =
                            client.post(endpoint) {
                                println("Posting to $endpoint with body $body")
                                header("Authorization", "Bearer ${mockOAuth2Server.tokenFromDefaultProvider()}")
                                contentType(ContentType.Application.Json)
                                setBody(body)
                            }
                        response.status shouldBe HttpStatusCode.OK
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
                        configureTestApplication(
                            mockOAuth2Server,
                            vedtakService,
                            kravgrunnlagService,
                            detaljerService,
                            annulerService,
                        )
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
    vedtakService: VedtakService,
    kravgrunnlagService: KravgrunnlagService,
    detaljerService: DetaljerService,
    annulerService: AnnulerService,
) {
    commonConfig()
    securityConfig(true, mockOAuth2Server.authConfig())
    routing {
        authenticate(AUTHENTICATION_NAME) {
            tilbakekrevingApi(
                vedtakService,
                annulerService,
                detaljerService,
                kravgrunnlagService,
            )
        }
    }
}
