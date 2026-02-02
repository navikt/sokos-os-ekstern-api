package no.nav.sokos.os.ekstern.api.os

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

import no.nav.sokos.os.ekstern.api.api.models.vedtak.Periode
import no.nav.sokos.os.ekstern.api.api.models.vedtak.VedtakRequest
import no.nav.sokos.os.ekstern.api.api.models.vedtak.VedtakResponse
import no.nav.sokos.os.ekstern.api.config.ApiError
import no.nav.sokos.os.ekstern.api.config.PropertiesConfig

@OptIn(ExperimentalTime::class)
class VedtakService(
    private val osConfig: PropertiesConfig.OsConfiguration = PropertiesConfig.OsConfiguration(),
    private val httpClient: HttpClient = osHttpClient(osConfig),
    endpointUrl: String = osConfig.endpointUrl,
) {
    private val url = "$endpointUrl/tilbakekrevingsvedtak"

    suspend fun postVedtak(request: VedtakRequest): VedtakResponse {
        val response: HttpResponse =
            httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request.toOsRequest())
            }

        return when {
            response.status.isSuccess() -> {
                val result = response.body<PostOsTilbakekrevingsvedtakResponse200>()
                val osResponse = result.osTilbakekrevingsvedtakOperationResponse?.zt1OCont?.responsTilbakekrevingsvedtak
                val response =
                    VedtakResponse(
                        status = osResponse?.status!!,
                        melding = osResponse.statusMelding!!,
                        vedtakId = osResponse.vedtakId!!,
                        datoVedtakFagsystem = osResponse.datoVedtakFagsystem!!,
                    )
                if (osResponse.status != 0) {
                    throw OsException(
                        ApiError(
                            Clock.System.now(),
                            HttpStatusCode.BadRequest.value,
                            HttpStatusCode.BadRequest.description,
                            osResponse.statusMelding,
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

    fun VedtakRequest.toOsRequest(): PostOsTilbakekrevingsvedtakRequest =
        PostOsTilbakekrevingsvedtakRequest(
            osTilbakekrevingsvedtakOperation =
                PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperation(
                    zt1ICont =
                        PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1ICont(
                            requestTilbakekrevingsvedtak =
                                PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtak(
                                    kodeAksjon = kodeAksjon,
                                    vedtakId = vedtakId,
                                    datoVedtakFagsystem = vedtaksDato,
                                    kodeHjemmel = kodeHjemmel,
                                    renterBeregnes = renterBeregnes,
                                    enhetAnsvarlig = enhetAnsvarlig,
                                    kontrollfelt = kontrollfelt,
                                    saksbehandlerId = saksbehandlerId,
                                    datoTilleggsfrist = datoTilleggsfrist,
                                    tilbakekrevingsperiode = perioder.map { it.toOsPeriode() },
                                ),
                        ),
                ),
        )

    fun Periode.toOsPeriode(): PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtakTilbakekrevingsperiodeInner =
        PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtakTilbakekrevingsperiodeInner(
            datoPeriodeFom = periodeFom,
            datoPeriodeTom = periodeTom,
            renterPeriodeBeregnes = renterPeriodeBeregnes,
            belopRenter = belopRenter,
            tilbakerevingsbelop =
                posteringer.map {
                    PostOsTilbakekrevingsvedtakRequestOsTilbakekrevingsvedtakOperationZT1IContRequestTilbakekrevingsvedtakTilbakekrevingsperiodeInnerTilbakerevingsbelopInner(
                        kodeKlasse = it.kodeKlasse,
                        belopOpprinneligUtbetalt = it.belopOpprinneligUtbetalt,
                        belopNy = it.belopNy,
                        belopTilbakekreves = it.belopTilbakekreves,
                        belopUinnkrevd = it.belopUinnkrevd,
                        belopSkatt = it.belopSkatt,
                        kodeResultat = it.kodeResultat,
                        kodeAarsak = it.kodeAarsak,
                        kodeSkyld = it.kodeSkyld,
                    )
                },
        )
}
