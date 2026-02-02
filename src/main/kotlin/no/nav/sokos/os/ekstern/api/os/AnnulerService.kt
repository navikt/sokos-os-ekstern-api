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

import no.nav.sokos.os.ekstern.api.api.models.annuler.AnnulerRequest
import no.nav.sokos.os.ekstern.api.api.models.annuler.AnnulerResponse
import no.nav.sokos.os.ekstern.api.config.ApiError
import no.nav.sokos.os.ekstern.api.config.PropertiesConfig

@OptIn(ExperimentalTime::class)
class AnnulerService(
    private val osConfig: PropertiesConfig.OsConfiguration = PropertiesConfig.OsConfiguration(),
    private val httpClient: HttpClient = osHttpClient(osConfig),
    endpointUrl: String = PropertiesConfig.OsConfiguration().endpointUrl,
) {
    private val url = "$endpointUrl/kravgrunnlagAnnuler"

    suspend fun postAnnuler(request: AnnulerRequest): AnnulerResponse {
        val response: HttpResponse =
            httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request.toOsRequest())
            }

        return when {
            response.status.isSuccess() -> {
                val result = response.body<PostOsKravgrunnlagAnnulerResponse200>()
                val responsKravgrunnlagAnnuler = result.osKravgrunnlagAnnulerOperationResponse?.kravgrunnlagAnnulert?.responsKravgrunnlagAnnuler
                val response =
                    AnnulerResponse(
                        status = responsKravgrunnlagAnnuler?.status!!,
                        melding = responsKravgrunnlagAnnuler.statusMelding!!,
                        vedtakId = responsKravgrunnlagAnnuler.vedtakId!!,
                        saksbehandlerId = responsKravgrunnlagAnnuler.saksbehandlerId!!,
                    )
                if (responsKravgrunnlagAnnuler.status != 0) {
                    throw OsException(
                        ApiError(
                            Clock.System.now(),
                            HttpStatusCode.BadRequest.value,
                            HttpStatusCode.BadRequest.description,
                            responsKravgrunnlagAnnuler.statusMelding,
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

    fun AnnulerRequest.toOsRequest(): PostOsKravgrunnlagAnnulerRequest =
        PostOsKravgrunnlagAnnulerRequest(
            osKravgrunnlagAnnulerOperation =
                PostOsKravgrunnlagAnnulerRequestOsKravgrunnlagAnnulerOperation(
                    kravgrunnlagAnnuler =
                        PostOsKravgrunnlagAnnulerRequestOsKravgrunnlagAnnulerOperationKravgrunnlagAnnuler(
                            requestKravgrunnlagAnnuler =
                                PostOsKravgrunnlagAnnulerRequestOsKravgrunnlagAnnulerOperationKravgrunnlagAnnulerRequestKravgrunnlagAnnuler(
                                    kodeAksjon = kodeAksjon,
                                    vedtakId = vedtakId.toInt(),
                                    enhetAnsvarlig = enhetAnsvarlig,
                                    saksbehandlerId = saksbehandlerId,
                                ),
                        ),
                ),
        )
}
