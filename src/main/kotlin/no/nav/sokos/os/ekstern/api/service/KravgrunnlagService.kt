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

import no.nav.sokos.os.ekstern.api.api.models.liste.Kravgrunnlag
import no.nav.sokos.os.ekstern.api.api.models.liste.KravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.api.models.liste.KravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.config.ApiError
import no.nav.sokos.os.ekstern.api.config.PropertiesConfig
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperation
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperationKravgrunnlag
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperationKravgrunnlagRequestTilbakekrevingsgrunnlag
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponseKravgrunnlagListeResponsKravgrunnlagListeKravgrunnlagInner
import no.nav.sokos.os.ekstern.api.os.errorDetails
import no.nav.sokos.os.ekstern.api.os.errorMessage
import no.nav.sokos.os.ekstern.api.os.osHttpClient

@OptIn(ExperimentalTime::class)
class KravgrunnlagService(
    private val osConfig: PropertiesConfig.OsConfiguration = PropertiesConfig.OsConfiguration(),
    private val httpClient: HttpClient = osHttpClient(osConfig),
    endpointUrl: String = PropertiesConfig.OsConfiguration().endpointUrl,
) {
    private val url = "$endpointUrl/kravgrunnlagHentListe"

    suspend fun postListe(request: KravgrunnlagRequest): KravgrunnlagResponse {
        val response: HttpResponse =
            httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request.toOsRequest())
            }

        return when {
            response.status.isSuccess() -> {
                val result = response.body<PostOsHentKravgrunnlagResponse200>()
                val responsKravgrunnlagListe = result.osHentKravgrunnlagOperationResponse?.kravgrunnlagListe?.responsKravgrunnlagListe
                val response =
                    KravgrunnlagResponse(
                        status = responsKravgrunnlagListe?.status!!,
                        melding = responsKravgrunnlagListe.statusMelding!!,
                        kravgrunnlagListe = responsKravgrunnlagListe.kravgrunnlag?.map { it.toKravgrunnlag() } ?: emptyList(),
                    )

                if (responsKravgrunnlagListe.status != 0) {
                    throw OsException(
                        ApiError(
                            Clock.System.now(),
                            HttpStatusCode.BadRequest.value,
                            HttpStatusCode.BadRequest.description,
                            responsKravgrunnlagListe.statusMelding,
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

    fun KravgrunnlagRequest.toOsRequest(): PostOsHentKravgrunnlagRequest =
        PostOsHentKravgrunnlagRequest(
            osHentKravgrunnlagOperation =
                PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperation(
                    kravgrunnlag =
                        PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperationKravgrunnlag(
                            requestTilbakekrevingsgrunnlag =
                                PostOsHentKravgrunnlagRequestOsHentKravgrunnlagOperationKravgrunnlagRequestTilbakekrevingsgrunnlag(
                                    kodeAksjon = kodeAksjon,
                                    gjelderId = gjelderId,
                                    typeGjelder = typeGjelder,
                                    utbetalesTilId = utbetalesTilId,
                                    typeUtbetalesTil = typeUtbetalesTil,
                                    enhetAnsvarlig = enhetAnsvarlig,
                                    kodeFaggruppe = kodeFaggruppe,
                                    kodeFagomraade = kodeFagomraade,
                                    fagsystemId = fagsystemId,
                                    kravgrunnlagId = kravgrunnlagId,
                                    saksbehandlerId = saksbehandlerId,
                                ),
                        ),
                ),
        )

    private fun PostOsHentKravgrunnlagResponse200OsHentKravgrunnlagOperationResponseKravgrunnlagListeResponsKravgrunnlagListeKravgrunnlagInner.toKravgrunnlag(): Kravgrunnlag =
        Kravgrunnlag(
            kravgrunnlagId =
                kravgrunnlagId?.toLong()
                    ?: throw Exception("OS response mangler kravgrunnlagId i kravgrunnlag liste element"),
            kodeStatusKrav = kodeStatusKrav.orEmpty(),
            gjelderId = gjelderId.orEmpty(),
            typeGjelder = typeGjelder.orEmpty(),
            utbetalesTilId = utbetalesTilId.orEmpty(),
            typeUtbetalesTil = typeUtbetalesTil.orEmpty(),
            kodeFagomraade = kodeFagomraade.orEmpty(),
            fagsystemId = fagsystemId.orEmpty(),
            datoVedtakFagsystem = datoVedtakFagsystem.orEmpty(),
            enhetBosted = enhetBosted.orEmpty(),
            enhetAnsvarlig = enhetAnsvarlig.orEmpty(),
            datoKravDannet = datoKravDannet.orEmpty(),
            datoPeriodeFom = datoPeriodeFom.orEmpty(),
            datoPeriodeTom = datoPeriodeTom.orEmpty(),
            belopSumFeilutbetalt = belopSumFeilutbetalt ?: 0.0,
        )
}
