package no.nav.sokos.os.ekstern.api.api

import kotlinx.serialization.json.Json

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk

import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import no.nav.security.mock.oauth2.withMockOAuth2Server
import no.nav.sokos.os.ekstern.api.API_BASE_PATH
import no.nav.sokos.os.ekstern.api.Testdata.hentKravgrunnlagDetaljerRequest
import no.nav.sokos.os.ekstern.api.Testdata.hentKravgrunnlagDetaljerResponse
import no.nav.sokos.os.ekstern.api.Testdata.hentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.Testdata.hentKravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.Testdata.kravgrunnlagAnnulerRequest
import no.nav.sokos.os.ekstern.api.Testdata.kravgrunnlagAnnulerResponse
import no.nav.sokos.os.ekstern.api.Testdata.tilbakekrevingsvedtakRequest
import no.nav.sokos.os.ekstern.api.Testdata.tilbakekrevingsvedtakResponse
import no.nav.sokos.os.ekstern.api.config.AUTHENTICATION_NAME
import no.nav.sokos.os.ekstern.api.config.PropertiesConfig
import no.nav.sokos.os.ekstern.api.config.authenticate
import no.nav.sokos.os.ekstern.api.config.commonConfig
import no.nav.sokos.os.ekstern.api.config.securityConfig
import no.nav.sokos.os.ekstern.api.dto.annuler.KravgrunnlagAnnulerResponse
import no.nav.sokos.os.ekstern.api.dto.detaljer.HentKravgrunnlagDetaljerResponse
import no.nav.sokos.os.ekstern.api.dto.kravgrunnlag.HentKravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.dto.vedtak.TilbakekrevingsvedtakResponse
import no.nav.sokos.os.ekstern.api.service.TilbakekrevingException
import no.nav.sokos.os.ekstern.api.service.TilbakekrevingService

private const val TILBAKEKREVING_BASE_API_PATH = "$API_BASE_PATH/tilbakekreving"

private val tilbakekrevingService = mockk<TilbakekrevingService>()

private fun ApplicationTestBuilder.createTestClient(): HttpClient =
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

private fun Application.configureTestApplication(mockOAuth2Server: MockOAuth2Server) {
    commonConfig()
    securityConfig(true, mockOAuth2Server.authConfig())
    routing {
        authenticate(true, AUTHENTICATION_NAME) {
            tilbakekrevingApi(tilbakekrevingService)
        }
    }
}

internal class TilbakekrevingApiTest :
    FunSpec({

        test("send tilbakekrevingsvedtak returnerer 200 OK") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client = createTestClient()
                    application {
                        configureTestApplication(mockOAuth2Server)
                    }

                    coEvery { tilbakekrevingService.sendTilbakekrevingsvedtak(any()) } returns tilbakekrevingsvedtakResponse

                    val response =
                        client.post("$TILBAKEKREVING_BASE_API_PATH/vedtak") {
                            header("Authorization", "Bearer ${mockOAuth2Server.tokenFromDefaultProvider()}")
                            contentType(ContentType.Application.Json)
                            setBody(tilbakekrevingsvedtakRequest)
                        }

                    response.status shouldBe HttpStatusCode.OK
                    val result = Json.decodeFromString<TilbakekrevingsvedtakResponse>(response.bodyAsText())
                    result shouldBe tilbakekrevingsvedtakResponse
                }
            }
        }

        test("send tilbakekrevingsvedtak med feil returnerer 500 Internal Server Error") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client = createTestClient()
                    application {
                        configureTestApplication(mockOAuth2Server)
                    }

                    coEvery { tilbakekrevingService.sendTilbakekrevingsvedtak(any()) } throws
                        TilbakekrevingException("Feil ved sending av vedtak")

                    val response =
                        client.post("$TILBAKEKREVING_BASE_API_PATH/vedtak") {
                            header("Authorization", "Bearer ${mockOAuth2Server.tokenFromDefaultProvider()}")
                            contentType(ContentType.Application.Json)
                            setBody(tilbakekrevingsvedtakRequest)
                        }

                    response.status shouldBe HttpStatusCode.InternalServerError
                }
            }
        }

        test("hent kravgrunnlag liste returnerer 200 OK") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client = createTestClient()
                    application {
                        configureTestApplication(mockOAuth2Server)
                    }

                    coEvery { tilbakekrevingService.hentKravgrunnlagListe(any()) } returns hentKravgrunnlagResponse

                    val response =
                        client.post("$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/liste") {
                            header("Authorization", "Bearer ${mockOAuth2Server.tokenFromDefaultProvider()}")
                            contentType(ContentType.Application.Json)
                            setBody(hentKravgrunnlagRequest)
                        }

                    response.status shouldBe HttpStatusCode.OK
                    val result = Json.decodeFromString<HentKravgrunnlagResponse>(response.bodyAsText())
                    result shouldBe hentKravgrunnlagResponse
                    result.kravgrunnlagListe.size shouldBe 1
                }
            }
        }

        test("hent kravgrunnlag liste med tom liste returnerer 200 OK") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client = createTestClient()
                    application {
                        configureTestApplication(mockOAuth2Server)
                    }

                    val emptyResponse =
                        HentKravgrunnlagResponse(
                            status = 200,
                            melding = "Ingen kravgrunnlag funnet",
                            kravgrunnlagListe = emptyList(),
                        )

                    coEvery { tilbakekrevingService.hentKravgrunnlagListe(any()) } returns emptyResponse

                    val response =
                        client.post("$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/liste") {
                            header("Authorization", "Bearer ${mockOAuth2Server.tokenFromDefaultProvider()}")
                            contentType(ContentType.Application.Json)
                            setBody(hentKravgrunnlagRequest)
                        }

                    response.status shouldBe HttpStatusCode.OK
                    val result = Json.decodeFromString<HentKravgrunnlagResponse>(response.bodyAsText())
                    result.kravgrunnlagListe.size shouldBe 0
                }
            }
        }

        test("hent kravgrunnlag liste med feil returnerer 500 Internal Server Error") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client = createTestClient()
                    application {
                        configureTestApplication(mockOAuth2Server)
                    }

                    coEvery { tilbakekrevingService.hentKravgrunnlagListe(any()) } throws
                        TilbakekrevingException("Feil ved henting av kravgrunnlag")

                    val response =
                        client.post("$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/liste") {
                            header("Authorization", "Bearer ${mockOAuth2Server.tokenFromDefaultProvider()}")
                            contentType(ContentType.Application.Json)
                            setBody(hentKravgrunnlagRequest)
                        }

                    response.status shouldBe HttpStatusCode.InternalServerError
                }
            }
        }

        test("hent kravgrunnlag detaljer returnerer 200 OK") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client = createTestClient()
                    application {
                        configureTestApplication(mockOAuth2Server)
                    }

                    coEvery { tilbakekrevingService.hentKravgrunnlagDetaljer(any()) } returns hentKravgrunnlagDetaljerResponse

                    val response =
                        client.post("$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/detaljer") {
                            header("Authorization", "Bearer ${mockOAuth2Server.tokenFromDefaultProvider()}")
                            contentType(ContentType.Application.Json)
                            setBody(hentKravgrunnlagDetaljerRequest)
                        }

                    response.status shouldBe HttpStatusCode.OK
                    val result = Json.decodeFromString<HentKravgrunnlagDetaljerResponse>(response.bodyAsText())
                    result shouldBe hentKravgrunnlagDetaljerResponse
                    result.kravgrunnlag.perioder.size shouldBe 1
                }
            }
        }

        test("hent kravgrunnlag detaljer med feil returnerer 500 Internal Server Error") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client = createTestClient()
                    application {
                        configureTestApplication(mockOAuth2Server)
                    }

                    coEvery { tilbakekrevingService.hentKravgrunnlagDetaljer(any()) } throws
                        RuntimeException("Database feil")

                    val response =
                        client.post("$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/detaljer") {
                            header("Authorization", "Bearer ${mockOAuth2Server.tokenFromDefaultProvider()}")
                            contentType(ContentType.Application.Json)
                            setBody(hentKravgrunnlagDetaljerRequest)
                        }

                    response.status shouldBe HttpStatusCode.InternalServerError
                }
            }
        }

        test("annuler kravgrunnlag returnerer 200 OK") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client = createTestClient()
                    application {
                        configureTestApplication(mockOAuth2Server)
                    }

                    coEvery { tilbakekrevingService.annulerKravgrunnlag(any()) } returns kravgrunnlagAnnulerResponse

                    val response =
                        client.post("$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/annuler") {
                            header("Authorization", "Bearer ${mockOAuth2Server.tokenFromDefaultProvider()}")
                            contentType(ContentType.Application.Json)
                            setBody(kravgrunnlagAnnulerRequest)
                        }

                    response.status shouldBe HttpStatusCode.OK
                    val result = Json.decodeFromString<KravgrunnlagAnnulerResponse>(response.bodyAsText())
                    result shouldBe kravgrunnlagAnnulerResponse
                }
            }
        }

        test("annuler kravgrunnlag med feil returnerer 500 Internal Server Error") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client = createTestClient()
                    application {
                        configureTestApplication(mockOAuth2Server)
                    }

                    coEvery { tilbakekrevingService.annulerKravgrunnlag(any()) } throws
                        TilbakekrevingException("Feil ved annullering")

                    val response =
                        client.post("$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/annuler") {
                            header("Authorization", "Bearer ${mockOAuth2Server.tokenFromDefaultProvider()}")
                            contentType(ContentType.Application.Json)
                            setBody(kravgrunnlagAnnulerRequest)
                        }

                    response.status shouldBe HttpStatusCode.InternalServerError
                }
            }
        }

        test("alle endepunkter uten autentisering returnerer 401 Unauthorized") {
            withMockOAuth2Server {
                val mockOAuth2Server = this
                testApplication {
                    val client = createTestClient()
                    application {
                        configureTestApplication(mockOAuth2Server)
                    }

                    val endpoints =
                        listOf(
                            "$TILBAKEKREVING_BASE_API_PATH/vedtak",
                            "$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/liste",
                            "$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/detaljer",
                            "$TILBAKEKREVING_BASE_API_PATH/kravgrunnlag/annuler",
                        )

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
