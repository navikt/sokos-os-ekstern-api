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

import no.nav.sokos.os.ekstern.api.api.models.detaljer.DetaljerPeriode
import no.nav.sokos.os.ekstern.api.api.models.detaljer.DetaljerPostering
import no.nav.sokos.os.ekstern.api.api.models.detaljer.KravdetaljerRequest
import no.nav.sokos.os.ekstern.api.api.models.detaljer.KravdetaljerResponse
import no.nav.sokos.os.ekstern.api.api.models.detaljer.KravgrunnlagDetaljer
import no.nav.sokos.os.ekstern.api.config.ApiError
import no.nav.sokos.os.ekstern.api.config.PropertiesConfig
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerRequest
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerRequestOsHentKravgrunnlagDetaljerOperation
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerRequestOsHentKravgrunnlagDetaljerOperationKravgrunnlagsdetaljer
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerRequestOsHentKravgrunnlagDetaljerOperationKravgrunnlagsdetaljerRequestDetaljer
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljer
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljerTilbakekrevingsperiodeInner
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljerTilbakekrevingsperiodeInnerTilbakekrevingsbelopInner
import no.nav.sokos.os.ekstern.api.os.errorDetails
import no.nav.sokos.os.ekstern.api.os.errorMessage
import no.nav.sokos.os.ekstern.api.os.osHttpClient

@OptIn(ExperimentalTime::class)
class DetaljerService(
    private val osConfig: PropertiesConfig.OsConfiguration = PropertiesConfig.OsConfiguration(),
    private val httpClient: HttpClient = osHttpClient(osConfig),
    endpointUrl: String = PropertiesConfig.OsConfiguration().endpointUrl,
) {
    private val url = "$endpointUrl/kravgrunnlagHentDetalj"

    suspend fun postDetaljer(request: KravdetaljerRequest): KravdetaljerResponse {
        val response: HttpResponse =
            httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request.toOsRequest())
            }

        return when {
            response.status.isSuccess() -> {
                val result = response.body<PostOsHentKravgrunnlagDetaljerResponse200>()
                val responsDetaljer = result.osHentKravgrunnlagDetaljerOperationResponse?.kravgrunnlagsdetaljer?.responsDetaljer
                val response =
                    KravdetaljerResponse(
                        status = responsDetaljer?.status!!,
                        melding = responsDetaljer.statusMelding!!,
                        kravgrunnlag = responsDetaljer.toKravgrunnlagDetaljer(),
                    )

                if (responsDetaljer.status != 0) {
                    throw OsException(
                        ApiError(
                            Clock.System.now(),
                            HttpStatusCode.BadRequest.value,
                            HttpStatusCode.BadRequest.description,
                            responsDetaljer.statusMelding,
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

    fun KravdetaljerRequest.toOsRequest(): PostOsHentKravgrunnlagDetaljerRequest =
        PostOsHentKravgrunnlagDetaljerRequest(
            osHentKravgrunnlagDetaljerOperation =
                PostOsHentKravgrunnlagDetaljerRequestOsHentKravgrunnlagDetaljerOperation(
                    kravgrunnlagsdetaljer =
                        PostOsHentKravgrunnlagDetaljerRequestOsHentKravgrunnlagDetaljerOperationKravgrunnlagsdetaljer(
                            requestDetaljer =
                                PostOsHentKravgrunnlagDetaljerRequestOsHentKravgrunnlagDetaljerOperationKravgrunnlagsdetaljerRequestDetaljer(
                                    kodeAksjon = kodeAksjon,
                                    kravgrunnlagId = kravgrunnlagId,
                                    enhetAnsvarlig = enhetAnsvarlig,
                                    saksbehandlerId = saksbehandlerId,
                                ),
                        ),
                ),
        )

    private fun PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljer.toKravgrunnlagDetaljer(): KravgrunnlagDetaljer =
        KravgrunnlagDetaljer(
            kravgrunnlagId = kravgrunnlagId?.toLong() ?: throw Exception("OS response mangler kravgrunnlagId"),
            vedtakId = vedtakId?.toLong() ?: throw Exception("OS response mangler vedtakId"),
            kodeStatusKrav = kodeStatusKrav.orEmpty(),
            kodeFagomraade = kodeFagomraade.orEmpty(),
            fagsystemId = fagsystemId.orEmpty(),
            datoVedtakFagsystem = datoVedtakFagsystem.orEmpty(),
            vedtakIdImgjort = vedtakIdImgjort?.toLong() ?: 0L,
            gjelderId = gjelderId.orEmpty(),
            typeGjelder = typeGjelder.orEmpty(),
            utbetalesTilId = utbetalesTilId.orEmpty(),
            typeUtbetalesTilId = typeUtbetalesTilId.orEmpty(),
            kodeHjemmel = kodeHjemmel.orEmpty(),
            renterBeregnes = renterBeregnes ?: false,
            enhetAnsvarlig = enhetAnsvarlig.orEmpty(),
            enhetBosted = enhetBosted.orEmpty(),
            enhetBehandl = enhetBehandl.orEmpty(),
            kontrollfelt = kontrollfelt.orEmpty(),
            saksbehandlerId = saksbehandlerId.orEmpty(),
            referanse = referanse.orEmpty(),
            datoTilleggsfrist = datoTilleggsfrist,
            perioder = tilbakekrevingsperiode?.map { it.toDetaljerPeriode() } ?: emptyList(),
        )

    private fun PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljerTilbakekrevingsperiodeInner.toDetaljerPeriode(): DetaljerPeriode =
        DetaljerPeriode(
            periodeFom = datoPeriodeFom.orEmpty(),
            periodeTom = datoPeriodeTom.orEmpty(),
            belopSkattMnd = belopSkattMnd ?: 0.0,
            posteringer = tilbakekrevingsbelop?.map { it.toDetaljerPostering() } ?: emptyList(),
        )

    private fun PostOsHentKravgrunnlagDetaljerResponse200OsHentKravgrunnlagDetaljerOperationResponseKravgrunnlagsdetaljerResponsDetaljerTilbakekrevingsperiodeInnerTilbakekrevingsbelopInner.toDetaljerPostering(): DetaljerPostering =
        DetaljerPostering(
            kodeKlasse = kodeKlasse.orEmpty(),
            typeKlasse = typeKlasse.orEmpty(),
            belopOpprinneligUtbetalt = belopOpprinneligUtbetalt ?: 0.0,
            belopNy = belopNy ?: 0.0,
            belopTilbakekreves = belopTilbakekreves ?: 0.0,
            belopUinnkrevd = belopUinnkrevd ?: 0.0,
            skattProsent = skattProsent ?: 0.0,
            kodeResultat = kodeResultat.orEmpty(),
            kodeAarsak = kodeAarsak.orEmpty(),
            kodeSkyld = kodeSkyld.orEmpty(),
        )
}
