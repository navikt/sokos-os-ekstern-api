package no.nav.sokos.os.ekstern.api.service

import kotlin.time.Clock

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import mu.KotlinLogging

import no.nav.sokos.os.ekstern.api.config.ApiError
import no.nav.sokos.os.ekstern.api.dto.annuler.KravgrunnlagAnnulerRequest
import no.nav.sokos.os.ekstern.api.dto.annuler.KravgrunnlagAnnulerResponse
import no.nav.sokos.os.ekstern.api.dto.detaljer.HentKravgrunnlagDetaljerRequest
import no.nav.sokos.os.ekstern.api.dto.detaljer.HentKravgrunnlagDetaljerResponse
import no.nav.sokos.os.ekstern.api.dto.kravgrunnlag.HentKravgrunnlagRequest
import no.nav.sokos.os.ekstern.api.dto.kravgrunnlag.HentKravgrunnlagResponse
import no.nav.sokos.os.ekstern.api.dto.toDto
import no.nav.sokos.os.ekstern.api.dto.toZosRequest
import no.nav.sokos.os.ekstern.api.os.OsHttpClient
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagDetaljerResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsHentKravgrunnlagResponse200
import no.nav.sokos.os.ekstern.api.os.PostOsKravgrunnlagAnnulerResponse200

private val logger = KotlinLogging.logger {}

class TilbakekrevingService(
    private val osHttpClient: OsHttpClient = OsHttpClient(),
) {
    suspend fun hentKravgrunnlagListe(request: HentKravgrunnlagRequest): HentKravgrunnlagResponse =
        executeOsCall(
            operasjon = "hente kravgrunnlag liste",
            call = { osHttpClient.postHentKravgrunnlag(request.toZosRequest()) },
            transform = { it.body<PostOsHentKravgrunnlagResponse200>().toDto() },
            logSuccess = { "Kravgrunnlag liste hentet, status: ${it.status}, antall: ${it.kravgrunnlagListe.size}" },
            extractStatus = { response ->
                response.status to response.melding
            },
        )

    suspend fun hentKravgrunnlagDetaljer(request: HentKravgrunnlagDetaljerRequest): HentKravgrunnlagDetaljerResponse =
        executeOsCall(
            operasjon = "hente kravgrunnlag detaljer",
            call = { osHttpClient.postHentKravgrunnlagDetaljer(request.toZosRequest()) },
            transform = { it.body<PostOsHentKravgrunnlagDetaljerResponse200>().toDto() },
            logSuccess = { "Kravgrunnlag detaljer hentet, status: ${it.status}, kravgrunnlagId: ${it.kravgrunnlag.kravgrunnlagId}" },
            extractStatus = { response ->
                response.status to response.melding
            },
        )

    suspend fun annulerKravgrunnlag(request: KravgrunnlagAnnulerRequest): KravgrunnlagAnnulerResponse =
        executeOsCall(
            operasjon = "annullere kravgrunnlag",
            call = { osHttpClient.postKravgrunnlagAnnuler(request.toZosRequest()) },
            transform = { it.body<PostOsKravgrunnlagAnnulerResponse200>().toDto() },
            logSuccess = { "Kravgrunnlag annullert, status: ${it.status}, vedtakId: ${it.vedtakId}" },
            extractStatus = { response ->
                response.status to response.melding
            },
        )

    private suspend fun <T> executeOsCall(
        operasjon: String,
        call: suspend () -> HttpResponse,
        transform: suspend (HttpResponse) -> T,
        logSuccess: (T) -> String,
        extractStatus: (T) -> Pair<Int, String?>,
    ): T {
        logger.info { "Starter operasjon: $operasjon" }
        val response = call()

        return when {
            response.status.isSuccess() -> {
                val result = transform(response)
                val (statusCode, errorMessage) = extractStatus(result)

                if (statusCode != 0) {
                    throw OsException(
                        ApiError(
                            Clock.System.now(),
                            HttpStatusCode.BadRequest.value,
                            HttpStatusCode.BadRequest.description,
                            errorMessage ?: "Ukjent feil",
                            response.request.url.toString(),
                        ),
                    )
                }

                logger.info { logSuccess(result) }
                result
            }
            else -> throw OsException(
                ApiError(
                    Clock.System.now(),
                    response.status.value,
                    response.status.description,
                    "Operasjon feilet: $operasjon",
                    response.request.url.toString(),
                ),
            )
        }
    }
}
