package no.nav.sokos.os.ekstern.api.os

import java.math.BigDecimal

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

private const val LISTE_PATH = "/api/v1/tilbakekreving/kravgrunnlag/liste"

internal class KravgrunnlagServiceTest :
    FunSpec({

        extensions(listOf(WireMockListener))

        val kravgrunnlagService: KravgrunnlagService by lazy {
            KravgrunnlagService(
                httpClient = testHttpClient,
                endpointUrl = wiremock.baseUrl(),
            )
        }

        test("postListe returnerer KravgrunnlagResponse med liste når OS returnerer status 0") {
            wiremock.stubFor(
                post(urlEqualTo("/kravgrunnlagHentListe"))
                    .willReturn(
                        aResponse()
                            .withHeader(HttpHeaders.ContentType, ContentTypes.APPLICATION_JSON)
                            .withStatus(HttpStatusCode.OK.value)
                            .withBody(jsonConfig.encodeToString(osKravgrunnlagSuccessResponse)),
                    ),
            )

            val response = kravgrunnlagService.postListe(Testdata.kravListeRequest, LISTE_PATH)

            response shouldBe Testdata.kravgrunnlagResponse
        }

        test("postListe kaster OsException med BadRequest når OS returnerer status 8") {
            wiremock.stubFor(
                post(urlEqualTo("/kravgrunnlagHentListe"))
                    .willReturn(
                        aResponse()
                            .withHeader(HttpHeaders.ContentType, ContentTypes.APPLICATION_JSON)
                            .withStatus(HttpStatusCode.OK.value)
                            .withBody(jsonConfig.encodeToString(osKravgrunnlagErrorResponse)),
                    ),
            )

            val exception =
                shouldThrow<OsException> {
                    kravgrunnlagService.postListe(Testdata.kravListeRequest, LISTE_PATH)
                }

            exception.apiError.status shouldBe HttpStatusCode.BadRequest.value
            exception.apiError.error shouldBe HttpStatusCode.BadRequest.description
            exception.apiError.message shouldBe "Kravgrunnlag ikke funnet"
            exception.apiError.path shouldBe LISTE_PATH
        }

        test("postListe kaster OsException når OS returnerer HTTP feilstatus") {
            wiremock.stubFor(
                post(urlEqualTo("/kravgrunnlagHentListe"))
                    .willReturn(
                        aResponse()
                            .withHeader(HttpHeaders.ContentType, ContentTypes.APPLICATION_JSON)
                            .withStatus(HttpStatusCode.InternalServerError.value)
                            .withBody("""{"errorMessage": "Intern feil i OS", "errorDetails": "Stack trace"}"""),
                    ),
            )

            val exception =
                shouldThrow<OsException> {
                    kravgrunnlagService.postListe(Testdata.kravListeRequest, LISTE_PATH)
                }

            exception.apiError.status shouldBe HttpStatusCode.InternalServerError.value
            exception.apiError.message shouldBe "Message: Intern feil i OS, Details: Stack trace"
            exception.apiError.path shouldBe LISTE_PATH
        }
    })

private val osKravgrunnlagSuccessResponse =
    PostOsHentKravgrunnlagResponse200(
        osHentKravgrunnlagOperationResponse =
            PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponse(
                kravgrunnlagListe =
                    PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponseKravgrunnlagListe(
                        responsKravgrunnlagListe =
                            PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponseKravgrunnlagListeResponsKravgrunnlagListe(
                                status = 0,
                                statusMelding = "",
                                kravgrunnlag =
                                    listOf(
                                        PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponseKravgrunnlagListeResponsKravgrunnlagListeKravgrunnlagInner(
                                            kravgrunnlagId = 123456,
                                            kodeStatusKrav = "NY",
                                            gjelderId = "12345678901",
                                            typeGjelder = "PERSON",
                                            utbetalesTilId = "12345678901",
                                            typeUtbetalesTil = "PERSON",
                                            kodeFagomraade = "PENALD",
                                            fagsystemId = "FS123456",
                                            datoVedtakFagsystem = "2026-01-15",
                                            enhetBosted = "0219",
                                            enhetAnsvarlig = "8020",
                                            datoKravDannet = "2026-01-15",
                                            datoPeriodeFom = "2026-01-01",
                                            datoPeriodeTom = "2026-01-31",
                                            belopSumFeilutbetalt = BigDecimal.valueOf(10000.00),
                                        ),
                                    ),
                            ),
                    ),
            ),
    )

private val osKravgrunnlagErrorResponse =
    PostOsHentKravgrunnlagResponse200(
        osHentKravgrunnlagOperationResponse =
            PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponse(
                kravgrunnlagListe =
                    PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponseKravgrunnlagListe(
                        responsKravgrunnlagListe =
                            PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponseKravgrunnlagListeResponsKravgrunnlagListe(
                                status = 8,
                                statusMelding = "Kravgrunnlag ikke funnet",
                                kravgrunnlag = emptyList(),
                            ),
                    ),
            ),
    )
