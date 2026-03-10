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

private const val ANNULER_PATH = "/api/v1/tilbakekreving/kravgrunnlag/annuler"

internal class AnnulerServiceTest :
    FunSpec({

        extensions(listOf(WireMockListener))

        val annulerService: AnnulerService by lazy {
            AnnulerService(
                httpClient = testHttpClient,
                endpointUrl = wiremock.baseUrl(),
            )
        }

        test("postAnnuler returnerer AnnulerResponse når OS returnerer status 0") {
            wiremock.stubFor(
                post(urlEqualTo("/kravgrunnlagAnnuler"))
                    .willReturn(
                        aResponse()
                            .withHeader(HttpHeaders.ContentType, ContentTypes.APPLICATION_JSON)
                            .withStatus(HttpStatusCode.OK.value)
                            .withBody(jsonConfig.encodeToString(osAnnulerSuccessResponse)),
                    ),
            )

            val response = annulerService.postAnnuler(Testdata.annulerRequest, ANNULER_PATH)

            response shouldBe Testdata.annulerResponse
        }

        test("postAnnuler kaster OsException med BadRequest når OS returnerer status 8") {
            wiremock.stubFor(
                post(urlEqualTo("/kravgrunnlagAnnuler"))
                    .willReturn(
                        aResponse()
                            .withHeader(HttpHeaders.ContentType, ContentTypes.APPLICATION_JSON)
                            .withStatus(HttpStatusCode.OK.value)
                            .withBody(jsonConfig.encodeToString(osAnnulerErrorResponse)),
                    ),
            )

            val exception =
                shouldThrow<OsException> {
                    annulerService.postAnnuler(Testdata.annulerRequest, ANNULER_PATH)
                }

            exception.apiError.status shouldBe HttpStatusCode.BadRequest.value
            exception.apiError.error shouldBe HttpStatusCode.BadRequest.description
            exception.apiError.message shouldBe "Kravgrunnlag ikke funnet for annulering"
            exception.apiError.path shouldBe ANNULER_PATH
        }

        test("postAnnuler kaster OsException når OS returnerer HTTP feilstatus") {
            wiremock.stubFor(
                post(urlEqualTo("/kravgrunnlagAnnuler"))
                    .willReturn(
                        aResponse()
                            .withHeader(HttpHeaders.ContentType, ContentTypes.APPLICATION_JSON)
                            .withStatus(HttpStatusCode.InternalServerError.value)
                            .withBody("""{"errorMessage": "Intern feil i OS", "errorDetails": "Stack trace"}"""),
                    ),
            )

            val exception =
                shouldThrow<OsException> {
                    annulerService.postAnnuler(Testdata.annulerRequest, ANNULER_PATH)
                }

            exception.apiError.status shouldBe HttpStatusCode.InternalServerError.value
            exception.apiError.message shouldBe "Message: Intern feil i OS, Details: Stack trace"
            exception.apiError.path shouldBe ANNULER_PATH
        }
    })

private val osAnnulerSuccessResponse =
    PostOsKravgrunnlagAnnulerResponse200(
        osKravgrunnlagAnnulerOperationResponse =
            PostOsKravgrunnlagAnnulerResponse200OsKravgrunnlagAnnulerOperationResponse(
                kravgrunnlagAnnulert =
                    PostOsKravgrunnlagAnnulerResponse200OsKravgrunnlagAnnulerOperationResponseKravgrunnlagAnnulert(
                        responsKravgrunnlagAnnuler =
                            PostOsKravgrunnlagAnnulerResponse200OsKravgrunnlagAnnulerOperationResponseKravgrunnlagAnnulertResponsKravgrunnlagAnnuler(
                                status = 0,
                                statusMelding = "",
                                vedtakId = 123456789,
                                saksbehandlerId = "Z999999",
                            ),
                    ),
            ),
    )

private val osAnnulerErrorResponse =
    PostOsKravgrunnlagAnnulerResponse200(
        osKravgrunnlagAnnulerOperationResponse =
            PostOsKravgrunnlagAnnulerResponse200OsKravgrunnlagAnnulerOperationResponse(
                kravgrunnlagAnnulert =
                    PostOsKravgrunnlagAnnulerResponse200OsKravgrunnlagAnnulerOperationResponseKravgrunnlagAnnulert(
                        responsKravgrunnlagAnnuler =
                            PostOsKravgrunnlagAnnulerResponse200OsKravgrunnlagAnnulerOperationResponseKravgrunnlagAnnulertResponsKravgrunnlagAnnuler(
                                status = 8,
                                statusMelding = "Kravgrunnlag ikke funnet for annulering",
                                vedtakId = 123456789,
                                saksbehandlerId = "Z999999",
                            ),
                    ),
            ),
    )
