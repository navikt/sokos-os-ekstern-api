package no.nav.sokos.os.ekstern.api.api

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

import com.atlassian.oai.validator.OpenApiInteractionValidator
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.routing.routing
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.restassured.RestAssured

import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import no.nav.sokos.os.ekstern.api.Testdata
import no.nav.sokos.os.ekstern.api.api.models.annuler.AnnulerResponse
import no.nav.sokos.os.ekstern.api.api.models.detaljer.KravdetaljerResponse
import no.nav.sokos.os.ekstern.api.api.models.liste.KravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.api.models.vedtak.VedtakResponse
import no.nav.sokos.os.ekstern.api.config.AUTHENTICATION_NAME
import no.nav.sokos.os.ekstern.api.config.ApiError
import no.nav.sokos.os.ekstern.api.config.PropertiesConfig
import no.nav.sokos.os.ekstern.api.config.commonConfig
import no.nav.sokos.os.ekstern.api.config.jsonConfig
import no.nav.sokos.os.ekstern.api.config.securityConfig
import no.nav.sokos.os.ekstern.api.os.AnnulerService
import no.nav.sokos.os.ekstern.api.os.DetaljerService
import no.nav.sokos.os.ekstern.api.os.KravgrunnlagService
import no.nav.sokos.os.ekstern.api.os.OsException
import no.nav.sokos.os.ekstern.api.os.VedtakService

private const val PORT = 9091

private lateinit var server: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>
private lateinit var mockOAuth2Server: MockOAuth2Server

private val validationFilter =
    OpenApiValidationFilter(
        OpenApiInteractionValidator
            .createForSpecificationUrl("spec/tilbakekreving-v1-swagger.yaml")
            .build(),
    )

private val vedtakService = mockk<VedtakService>()
private val kravgrunnlagService = mockk<KravgrunnlagService>()
private val detaljerService = mockk<DetaljerService>()
private val annulerService = mockk<AnnulerService>()

@OptIn(ExperimentalTime::class)
internal class TilbakekrevingApiTest :
    FunSpec({

        beforeSpec {
            mockOAuth2Server = MockOAuth2Server()
            mockOAuth2Server.start()
            server = embeddedServer(Netty, PORT, module = Application::applicationTestModule).start()
        }

        afterSpec {
            server.stop(5, 5)
            mockOAuth2Server.shutdown()
        }

        beforeTest {
            clearMocks(vedtakService, kravgrunnlagService, detaljerService, annulerService)
        }

        test("POST /vedtak returnerer 200 OK") {
            coEvery { vedtakService.postVedtak(any(), any()) } returns Testdata.vedtakResponse

            val response =
                RestAssured
                    .given()
                    .filter(validationFilter)
                    .header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    .header(HttpHeaders.Authorization, "Bearer ${mockOAuth2Server.token()}")
                    .body(jsonConfig.encodeToString(Testdata.vedtakRequest))
                    .port(PORT)
                    .post("$API_BASE_PATH/vedtak")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatusCode.OK.value)
                    .extract()
                    .response()

            jsonConfig.decodeFromString<VedtakResponse>(response.body.asString()) shouldBe Testdata.vedtakResponse
        }

        test("POST /vedtak returnerer 500 Internal Server Error når service kaster exception") {
            coEvery { vedtakService.postVedtak(any(), any()) } throws RuntimeException("Feil mot OS")

            val response =
                RestAssured
                    .given()
                    .filter(validationFilter)
                    .header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    .header(HttpHeaders.Authorization, "Bearer ${mockOAuth2Server.token()}")
                    .body(jsonConfig.encodeToString(Testdata.vedtakRequest))
                    .port(PORT)
                    .post("$API_BASE_PATH/vedtak")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatusCode.InternalServerError.value)
                    .extract()
                    .response()

            jsonConfig.decodeFromString<ApiError>(response.asString()) shouldBe
                ApiError(
                    timestamp = Instant.parse(response.jsonPath().getString("timestamp")),
                    status = HttpStatusCode.InternalServerError.value,
                    error = HttpStatusCode.InternalServerError.description,
                    message = "Feil mot OS",
                    path = "$API_BASE_PATH/vedtak",
                )
        }

        test("POST /kravgrunnlag/liste returnerer 200 OK") {
            coEvery { kravgrunnlagService.postListe(any(), any()) } returns Testdata.kravgrunnlagResponse

            val response =
                RestAssured
                    .given()
                    .filter(validationFilter)
                    .header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    .header(HttpHeaders.Authorization, "Bearer ${mockOAuth2Server.token()}")
                    .body(jsonConfig.encodeToString(Testdata.kravListeRequest))
                    .port(PORT)
                    .post("$API_BASE_PATH/kravgrunnlag/liste")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatusCode.OK.value)
                    .extract()
                    .response()

            jsonConfig.decodeFromString<KravgrunnlagResponse>(response.body.asString()) shouldBe Testdata.kravgrunnlagResponse
        }

        test("POST /kravgrunnlag/liste returnerer 500 Internal Server Error når service kaster exception") {
            coEvery { kravgrunnlagService.postListe(any(), any()) } throws RuntimeException("Feil mot OS")

            val response =
                RestAssured
                    .given()
                    .filter(validationFilter)
                    .header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    .header(HttpHeaders.Authorization, "Bearer ${mockOAuth2Server.token()}")
                    .body(jsonConfig.encodeToString(Testdata.kravListeRequest))
                    .port(PORT)
                    .post("$API_BASE_PATH/kravgrunnlag/liste")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatusCode.InternalServerError.value)
                    .extract()
                    .response()

            jsonConfig.decodeFromString<ApiError>(response.asString()) shouldBe
                ApiError(
                    timestamp = Instant.parse(response.jsonPath().getString("timestamp")),
                    status = HttpStatusCode.InternalServerError.value,
                    error = HttpStatusCode.InternalServerError.description,
                    message = "Feil mot OS",
                    path = "$API_BASE_PATH/kravgrunnlag/liste",
                )
        }

        test("POST /kravgrunnlag/detaljer returnerer 200 OK") {
            coEvery { detaljerService.postDetaljer(any(), any()) } returns Testdata.kravDetaljerResponse

            val response =
                RestAssured
                    .given()
                    .filter(validationFilter)
                    .header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    .header(HttpHeaders.Authorization, "Bearer ${mockOAuth2Server.token()}")
                    .body(jsonConfig.encodeToString(Testdata.kravDetaljerRequest))
                    .port(PORT)
                    .post("$API_BASE_PATH/kravgrunnlag/detaljer")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatusCode.OK.value)
                    .extract()
                    .response()

            jsonConfig.decodeFromString<KravdetaljerResponse>(response.body.asString()) shouldBe Testdata.kravDetaljerResponse
        }

        test("POST /kravgrunnlag/detaljer returnerer 500 Internal Server Error når service kaster exception") {
            coEvery { detaljerService.postDetaljer(any(), any()) } throws RuntimeException("Feil mot OS")

            val response =
                RestAssured
                    .given()
                    .filter(validationFilter)
                    .header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    .header(HttpHeaders.Authorization, "Bearer ${mockOAuth2Server.token()}")
                    .body(jsonConfig.encodeToString(Testdata.kravDetaljerRequest))
                    .port(PORT)
                    .post("$API_BASE_PATH/kravgrunnlag/detaljer")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatusCode.InternalServerError.value)
                    .extract()
                    .response()

            jsonConfig.decodeFromString<ApiError>(response.asString()) shouldBe
                ApiError(
                    timestamp = Instant.parse(response.jsonPath().getString("timestamp")),
                    status = HttpStatusCode.InternalServerError.value,
                    error = HttpStatusCode.InternalServerError.description,
                    message = "Feil mot OS",
                    path = "$API_BASE_PATH/kravgrunnlag/detaljer",
                )
        }

        test("POST /kravgrunnlag/annuler returnerer 200 OK") {
            coEvery { annulerService.postAnnuler(any(), any()) } returns Testdata.annulerResponse

            val response =
                RestAssured
                    .given()
                    .filter(validationFilter)
                    .header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    .header(HttpHeaders.Authorization, "Bearer ${mockOAuth2Server.token()}")
                    .body(jsonConfig.encodeToString(Testdata.annulerRequest))
                    .port(PORT)
                    .post("$API_BASE_PATH/kravgrunnlag/annuler")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatusCode.OK.value)
                    .extract()
                    .response()

            jsonConfig.decodeFromString<AnnulerResponse>(response.body.asString()) shouldBe Testdata.annulerResponse
        }

        test("POST /kravgrunnlag/annuler returnerer 500 Internal Server Error når service kaster exception") {
            coEvery { annulerService.postAnnuler(any(), any()) } throws RuntimeException("Feil mot OS")

            val response =
                RestAssured
                    .given()
                    .filter(validationFilter)
                    .header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    .header(HttpHeaders.Authorization, "Bearer ${mockOAuth2Server.token()}")
                    .body(jsonConfig.encodeToString(Testdata.annulerRequest))
                    .port(PORT)
                    .post("$API_BASE_PATH/kravgrunnlag/annuler")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatusCode.InternalServerError.value)
                    .extract()
                    .response()

            jsonConfig.decodeFromString<ApiError>(response.asString()) shouldBe
                ApiError(
                    timestamp = Instant.parse(response.jsonPath().getString("timestamp")),
                    status = HttpStatusCode.InternalServerError.value,
                    error = HttpStatusCode.InternalServerError.description,
                    message = "Feil mot OS",
                    path = "$API_BASE_PATH/kravgrunnlag/annuler",
                )
        }

        test("POST /vedtak returnerer 400 Bad Request når service kaster OsException") {
            val apiError =
                ApiError(
                    timestamp = Instant.parse("2026-01-01T00:00:00Z"),
                    status = HttpStatusCode.BadRequest.value,
                    error = HttpStatusCode.BadRequest.description,
                    message = "Vedtak ikke funnet i OS",
                    path = "$API_BASE_PATH/vedtak",
                )
            coEvery { vedtakService.postVedtak(any(), any()) } throws OsException(apiError)

            val response =
                RestAssured
                    .given()
                    .filter(validationFilter)
                    .header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    .header(HttpHeaders.Authorization, "Bearer ${mockOAuth2Server.token()}")
                    .body(jsonConfig.encodeToString(Testdata.vedtakRequest))
                    .port(PORT)
                    .post("$API_BASE_PATH/vedtak")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatusCode.BadRequest.value)
                    .extract()
                    .response()

            jsonConfig.decodeFromString<ApiError>(response.asString()) shouldBe apiError
        }

        test("POST /kravgrunnlag/liste returnerer 400 Bad Request når service kaster OsException") {
            val apiError =
                ApiError(
                    timestamp = Instant.parse("2026-01-01T00:00:00Z"),
                    status = HttpStatusCode.BadRequest.value,
                    error = HttpStatusCode.BadRequest.description,
                    message = "Kravgrunnlag ikke funnet i OS",
                    path = "$API_BASE_PATH/kravgrunnlag/liste",
                )
            coEvery { kravgrunnlagService.postListe(any(), any()) } throws OsException(apiError)

            val response =
                RestAssured
                    .given()
                    .filter(validationFilter)
                    .header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    .header(HttpHeaders.Authorization, "Bearer ${mockOAuth2Server.token()}")
                    .body(jsonConfig.encodeToString(Testdata.kravListeRequest))
                    .port(PORT)
                    .post("$API_BASE_PATH/kravgrunnlag/liste")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatusCode.BadRequest.value)
                    .extract()
                    .response()

            jsonConfig.decodeFromString<ApiError>(response.asString()) shouldBe apiError
        }

        test("POST /kravgrunnlag/detaljer returnerer 400 Bad Request når service kaster OsException") {
            val apiError =
                ApiError(
                    timestamp = Instant.parse("2026-01-01T00:00:00Z"),
                    status = HttpStatusCode.BadRequest.value,
                    error = HttpStatusCode.BadRequest.description,
                    message = "Kravgrunnlag detaljer ikke funnet i OS",
                    path = "$API_BASE_PATH/kravgrunnlag/detaljer",
                )
            coEvery { detaljerService.postDetaljer(any(), any()) } throws OsException(apiError)

            val response =
                RestAssured
                    .given()
                    .filter(validationFilter)
                    .header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    .header(HttpHeaders.Authorization, "Bearer ${mockOAuth2Server.token()}")
                    .body(jsonConfig.encodeToString(Testdata.kravDetaljerRequest))
                    .port(PORT)
                    .post("$API_BASE_PATH/kravgrunnlag/detaljer")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatusCode.BadRequest.value)
                    .extract()
                    .response()

            jsonConfig.decodeFromString<ApiError>(response.asString()) shouldBe apiError
        }

        test("POST /kravgrunnlag/annuler returnerer 400 Bad Request når service kaster OsException") {
            val apiError =
                ApiError(
                    timestamp = Instant.parse("2026-01-01T00:00:00Z"),
                    status = HttpStatusCode.BadRequest.value,
                    error = HttpStatusCode.BadRequest.description,
                    message = "Kravgrunnlag ikke funnet for annulering i OS",
                    path = "$API_BASE_PATH/kravgrunnlag/annuler",
                )
            coEvery { annulerService.postAnnuler(any(), any()) } throws OsException(apiError)

            val response =
                RestAssured
                    .given()
                    .filter(validationFilter)
                    .header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    .header(HttpHeaders.Authorization, "Bearer ${mockOAuth2Server.token()}")
                    .body(jsonConfig.encodeToString(Testdata.annulerRequest))
                    .port(PORT)
                    .post("$API_BASE_PATH/kravgrunnlag/annuler")
                    .then()
                    .assertThat()
                    .statusCode(HttpStatusCode.BadRequest.value)
                    .extract()
                    .response()

            jsonConfig.decodeFromString<ApiError>(response.asString()) shouldBe apiError
        }
    })

private fun MockOAuth2Server.authConfig() =
    PropertiesConfig.AzureAdProperties(
        wellKnownUrl = wellKnownUrl("default").toString(),
        clientId = "default",
    )

private fun MockOAuth2Server.token() =
    issueToken(
        issuerId = "default",
        clientId = "default",
        tokenCallback = DefaultOAuth2TokenCallback(),
    ).serialize()

private fun Application.applicationTestModule() {
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
