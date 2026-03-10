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

private const val DETALJER_PATH = "/api/v1/tilbakekreving/kravgrunnlag/detaljer"

internal class DetaljerServiceTest :
    FunSpec({

        extensions(listOf(WireMockListener))

        val detaljerService: DetaljerService by lazy {
            DetaljerService(
                httpClient = testHttpClient,
                endpointUrl = wiremock.baseUrl(),
            )
        }

        test("postDetaljer returnerer KravdetaljerResponse når OS returnerer status 0") {
            wiremock.stubFor(
                post(urlEqualTo("/kravgrunnlagHentDetalj"))
                    .willReturn(
                        aResponse()
                            .withHeader(HttpHeaders.ContentType, ContentTypes.APPLICATION_JSON)
                            .withStatus(HttpStatusCode.OK.value)
                            .withBody(jsonConfig.encodeToString(osDetaljerSuccessResponse)),
                    ),
            )

            val response = detaljerService.postDetaljer(Testdata.kravDetaljerRequest, DETALJER_PATH)

            response shouldBe Testdata.kravDetaljerResponse
        }

        test("postDetaljer kaster OsException med BadRequest når OS returnerer status 8") {
            wiremock.stubFor(
                post(urlEqualTo("/kravgrunnlagHentDetalj"))
                    .willReturn(
                        aResponse()
                            .withHeader(HttpHeaders.ContentType, ContentTypes.APPLICATION_JSON)
                            .withStatus(HttpStatusCode.OK.value)
                            .withBody(jsonConfig.encodeToString(osDetaljerErrorResponse)),
                    ),
            )

            val exception =
                shouldThrow<OsException> {
                    detaljerService.postDetaljer(Testdata.kravDetaljerRequest, DETALJER_PATH)
                }

            exception.apiError.status shouldBe HttpStatusCode.BadRequest.value
            exception.apiError.error shouldBe HttpStatusCode.BadRequest.description
            exception.apiError.message shouldBe "Kravgrunnlag ikke funnet"
            exception.apiError.path shouldBe DETALJER_PATH
        }

        test("postDetaljer kaster OsException når OS returnerer HTTP feilstatus") {
            wiremock.stubFor(
                post(urlEqualTo("/kravgrunnlagHentDetalj"))
                    .willReturn(
                        aResponse()
                            .withHeader(HttpHeaders.ContentType, ContentTypes.APPLICATION_JSON)
                            .withStatus(HttpStatusCode.InternalServerError.value)
                            .withBody("""{"errorMessage": "Intern feil i OS", "errorDetails": "Stack trace"}"""),
                    ),
            )

            val exception =
                shouldThrow<OsException> {
                    detaljerService.postDetaljer(Testdata.kravDetaljerRequest, DETALJER_PATH)
                }

            exception.apiError.status shouldBe HttpStatusCode.InternalServerError.value
            exception.apiError.message shouldBe "Message: Intern feil i OS, Details: Stack trace"
            exception.apiError.path shouldBe DETALJER_PATH
        }
    })

private val osDetaljerSuccessResponse =
    PostOsHentKravgrunnlagDetaljerResponse200(
        osHentKravgrunnlagDetaljerOperationResponse =
            PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponse(
                kravgrunnlagsdetaljer =
                    PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljer(
                        responsDetaljer =
                            PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljer(
                                status = 0,
                                statusMelding = "",
                                kravgrunnlagId = 570598,
                                vedtakId = 851138,
                                kodeStatusKrav = "BEHA",
                                kodeFagomraade = "FP",
                                fagsystemId = "152465243100",
                                datoVedtakFagsystem = "2025-10-28",
                                vedtakIdImgjort = 851138,
                                gjelderId = "98765432111",
                                typeGjelder = "PERSON",
                                utbetalesTilId = "98765432111",
                                typeUtbetalesTilId = "PERSON",
                                kodeHjemmel = null,
                                renterBeregnes = false,
                                enhetAnsvarlig = "8020",
                                enhetBosted = "8020",
                                enhetBehandl = "8020",
                                kontrollfelt = "2025-10-28-22.57.21.466798",
                                saksbehandlerId = "Z999999",
                                referanse = "3650158",
                                datoTilleggsfrist = null,
                                tilbakekrevingsperiode =
                                    listOf(
                                        PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljerTilbakekrevingsperiodeInner(
                                            datoPeriodeFom = "2025-08-11",
                                            datoPeriodeTom = "2025-08-31",
                                            belopSkattMnd = BigDecimal.valueOf(6783),
                                            tilbakekrevingsbelop =
                                                listOf(
                                                    PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljerTilbakekrevingsperiodeInnerTilbakekrevingsbelopInner(
                                                        kodeKlasse = "FPATORD",
                                                        typeKlasse = "YTEL",
                                                        belopOpprinneligUtbetalt = BigDecimal.valueOf(31005),
                                                        belopNy = BigDecimal.ZERO,
                                                        belopTilbakekreves = BigDecimal.valueOf(31005),
                                                        belopUinnkrevd = BigDecimal.ZERO,
                                                        skattProsent = BigDecimal.valueOf(21.8771),
                                                        kodeResultat = null,
                                                        kodeAarsak = null,
                                                        kodeSkyld = null,
                                                    ),
                                                ),
                                        ),
                                    ),
                            ),
                    ),
            ),
    )

private val osDetaljerErrorResponse =
    PostOsHentKravgrunnlagDetaljerResponse200(
        osHentKravgrunnlagDetaljerOperationResponse =
            PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponse(
                kravgrunnlagsdetaljer =
                    PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljer(
                        responsDetaljer =
                            PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljer(
                                status = 8,
                                statusMelding = "Kravgrunnlag ikke funnet",
                                kravgrunnlagId = 570598,
                                vedtakId = 851138,
                            ),
                    ),
            ),
    )
