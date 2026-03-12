package no.nav.sokos.os.ekstern.api.os

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.common.ContentTypes
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode

import no.nav.sokos.os.ekstern.api.Testdata
import no.nav.sokos.os.ekstern.api.config.jsonConfig
import no.nav.sokos.os.ekstern.api.listener.WireMockListener
import no.nav.sokos.os.ekstern.api.listener.WireMockListener.wiremock

private const val VEDTAK_PATH = "/api/v1/tilbakekreving/vedtak"

internal class VedtakServiceTest :
    FunSpec({

        extensions(listOf(WireMockListener))

        val vedtakService: VedtakService by lazy {
            VedtakService(
                httpClient = testHttpClient,
                endpointUrl = wiremock.baseUrl(),
            )
        }

        test("postVedtak returnerer VedtakResponse når OS returnerer status 0") {
            wiremock.stubFor(
                post(urlEqualTo("/tilbakekrevingsvedtak"))
                    .willReturn(
                        aResponse()
                            .withHeader(HttpHeaders.ContentType, ContentTypes.APPLICATION_JSON)
                            .withStatus(HttpStatusCode.OK.value)
                            .withBody(jsonConfig.encodeToString(osVedtakSuccessResponse)),
                    ),
            )

            val response = vedtakService.postVedtak(Testdata.vedtakRequest, VEDTAK_PATH)

            response shouldBe Testdata.vedtakResponse
        }

        test("postVedtak kaster OsException med BadRequest når OS returnerer status 8") {
            wiremock.stubFor(
                post(urlEqualTo("/tilbakekrevingsvedtak"))
                    .willReturn(
                        aResponse()
                            .withHeader(HttpHeaders.ContentType, ContentTypes.APPLICATION_JSON)
                            .withStatus(HttpStatusCode.OK.value)
                            .withBody(jsonConfig.encodeToString(osVedtakErrorResponse)),
                    ),
            )

            val exception =
                shouldThrow<OsException> {
                    vedtakService.postVedtak(Testdata.vedtakRequest, VEDTAK_PATH)
                }

            exception.apiError.status shouldBe HttpStatusCode.BadRequest.value
            exception.apiError.error shouldBe HttpStatusCode.BadRequest.description
            exception.apiError.message shouldBe "Vedtak ikke funnet"
            exception.apiError.path shouldBe VEDTAK_PATH
        }

        test("postVedtak kaster OsException når OS returnerer HTTP feilstatus") {
            wiremock.stubFor(
                post(urlEqualTo("/tilbakekrevingsvedtak"))
                    .willReturn(
                        aResponse()
                            .withHeader(HttpHeaders.ContentType, ContentTypes.APPLICATION_JSON)
                            .withStatus(HttpStatusCode.InternalServerError.value)
                            .withBody("""{"errorMessage": "Intern feil i OS", "errorDetails": "Stack trace"}"""),
                    ),
            )

            val exception =
                shouldThrow<OsException> {
                    vedtakService.postVedtak(Testdata.vedtakRequest, VEDTAK_PATH)
                }

            exception.apiError.status shouldBe HttpStatusCode.InternalServerError.value
            exception.apiError.message shouldBe "Message: Intern feil i OS, Details: Stack trace"
            exception.apiError.path shouldBe VEDTAK_PATH
        }
    })

private val osVedtakSuccessResponse =
    PostOsTilbakekrevingsvedtakResponse200(
        osTilbakekrevingsvedtakOperationResponse =
            PostOsTilbakekrevingsvedtakResponse200OsTilbakekrevingsvedtakOperationResponse(
                zt1OCont =
                    PostOsTilbakekrevingsvedtakResponse200OsTilbakekrevingsvedtakOperationResponseZT1OCont(
                        responsTilbakekrevingsvedtak =
                            PostOsTilbakekrevingsvedtakResponse200OsTilbakekrevingsvedtakOperationResponseZT1OContResponsTilbakekrevingsvedtak(
                                status = 0,
                                statusMelding = "",
                                vedtakId = 892793,
                                datoVedtakFagsystem = "2026-02-11",
                            ),
                    ),
            ),
    )

private val osVedtakErrorResponse =
    PostOsTilbakekrevingsvedtakResponse200(
        osTilbakekrevingsvedtakOperationResponse =
            PostOsTilbakekrevingsvedtakResponse200OsTilbakekrevingsvedtakOperationResponse(
                zt1OCont =
                    PostOsTilbakekrevingsvedtakResponse200OsTilbakekrevingsvedtakOperationResponseZT1OCont(
                        responsTilbakekrevingsvedtak =
                            PostOsTilbakekrevingsvedtakResponse200OsTilbakekrevingsvedtakOperationResponseZT1OContResponsTilbakekrevingsvedtak(
                                status = 8,
                                statusMelding = "Vedtak ikke funnet",
                                vedtakId = 892793,
                                datoVedtakFagsystem = "2026-02-11",
                            ),
                    ),
            ),
    )
