package no.nav.sokos.os.ekstern.api.service

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess

import no.nav.sokos.os.ekstern.api.config.ApiError
import no.nav.sokos.os.ekstern.api.config.PropertiesConfig
import no.nav.sokos.os.ekstern.api.dto.toZosPeriode
import no.nav.sokos.os.ekstern.api.dto.vedtak.TilbakekrevingsvedtakRequest
import no.nav.sokos.os.ekstern.api.dto.vedtak.TilbakekrevingsvedtakResponse
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequest
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperation
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1ICont
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtak
import no.nav.sokos.os.ekstern.api.os.PostOsTilbakekrevingsvedtakResponse200
import no.nav.sokos.os.ekstern.api.os.errorDetails
import no.nav.sokos.os.ekstern.api.os.errorMessage
import no.nav.sokos.os.ekstern.api.os.osHttpClient

@OptIn(ExperimentalTime::class)
class VedtakService(
    private val osConfig: PropertiesConfig.OsConfiguration = PropertiesConfig.OsConfiguration(),
    private val httpClient: HttpClient = osHttpClient(osConfig),
    endpointUrl: String = PropertiesConfig.OsConfiguration().endpointUrl,
) {
    private val url = "$endpointUrl/tilbakekrevingsvedtak"

    suspend fun postVedtak(request: TilbakekrevingsvedtakRequest): TilbakekrevingsvedtakResponse {
        val response: HttpResponse =
            httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request.toZosRequest())
            }

        return when {
            response.status.isSuccess() -> {
                val result = response.body<PostOsTilbakekrevingsvedtakResponse200>()
                val kvittering = result.osTilbakekrevingsvedtakOperationResponse?.zt1OCont?.responsTilbakekrevingsvedtak
                val response =
                    TilbakekrevingsvedtakResponse(
                        status =
                            result.osTilbakekrevingsvedtakOperationResponse!!
                                .zt1OCont.responsTilbakekrevingsvedtak!!
                                .status!!,
                        melding = result.osTilbakekrevingsvedtakOperationResponse.zt1OCont.responsTilbakekrevingsvedtak.statusMelding!!,
                        vedtakId = result.osTilbakekrevingsvedtakOperationResponse.zt1OCont.responsTilbakekrevingsvedtak.vedtakId!!,
                        datoVedtakFagsystem = result.osTilbakekrevingsvedtakOperationResponse.zt1OCont.responsTilbakekrevingsvedtak.datoVedtakFagsystem!!,
                    )
                if (kvittering.status != 0) {
                    throw OsException(
                        ApiError(
                            Clock.System.now(),
                            HttpStatusCode.BadRequest.value,
                            HttpStatusCode.BadRequest.description,
                            kvittering.statusMelding,
                            url,
                        ),
                    )
                }
                response
            }

            else -> throw OsException(
                ApiError(
                    Clock.System.now(),
                    response.status.value,
                    response.status.description,
                    "Message: ${response.errorMessage()}, Details: ${response.errorDetails()}",
                    url,
                ),
            )
        }
    }

    fun TilbakekrevingsvedtakRequest.toZosRequest(): PostOsTilbakekrevingsvedtakRequest =
        PostOsTilbakekrevingsvedtakRequest(
            osTilbakekrevingsvedtakOperation =
                PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperation(
                    zt1ICont =
                        PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1ICont(
                            requestTilbakekrevingsvedtak =
                                PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtak(
                                    kodeAksjon = kodeAksjon,
                                    vedtakId = vedtakId.toInt(),
                                    datoVedtakFagsystem = vedtaksDato,
                                    kodeHjemmel = kodeHjemmel,
                                    renterBeregnes = renterBeregnes,
                                    enhetAnsvarlig = enhetAnsvarlig,
                                    kontrollfelt = kontrollfelt,
                                    saksbehandlerId = saksbehandlerId,
                                    datoTilleggsfrist = datoTilleggsfrist,
                                    tilbakekrevingsperiode = perioder.map { it.toZosPeriode() },
                                ),
                        ),
                ),
        )
}
