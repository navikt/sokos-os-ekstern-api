package no.nav.sokos.os.ekstern.api.config

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import io.ktor.server.request.path
import io.ktor.server.response.respond
import mu.KotlinLogging

import no.nav.sokos.os.ekstern.api.os.OsException

private val logger = KotlinLogging.logger {}

fun StatusPagesConfig.statusPageConfig() {
    exception<Throwable> { call, cause ->
        val (responseStatus, apiError) =
            when (cause) {
                is OsException -> {
                    logger.warn { "OS feil på ${call.request.path()}: status=${cause.apiError.status}, melding=${cause.apiError.message}" }
                    Pair(HttpStatusCode.allStatusCodes.find { it.value == cause.apiError.status }!!, cause.apiError)
                }
                is BadRequestException -> {
                    val jsonException = cause.findCauseOfType<JsonConvertException>()
                    logger.warn { "Ugyldig request på ${call.request.path()}: ${jsonException?.message ?: cause.message}" }
                    createApiError(HttpStatusCode.BadRequest, jsonException?.message ?: cause.message, call)
                }
                else -> {
                    logger.error(cause) { "Uventet feil på ${call.request.path()}" }
                    createApiError(HttpStatusCode.InternalServerError, cause.message ?: "En teknisk feil har oppstått. Ta kontakt med utviklerne", call)
                }
            }
        call.respond(responseStatus, apiError)
    }
}

@OptIn(ExperimentalTime::class)
private fun createApiError(
    status: HttpStatusCode,
    message: String?,
    call: ApplicationCall,
): Pair<HttpStatusCode, ApiError> =
    Pair(
        status,
        ApiError(
            Clock.System.now(),
            status.value,
            status.description,
            message,
            call.request.path(),
        ),
    )

@OptIn(ExperimentalTime::class)
@Serializable
data class ApiError(
    val timestamp: @Contextual Instant,
    val status: Int,
    val error: String,
    val message: String?,
    val path: String,
)

private inline fun <reified T : Throwable> Throwable.findCauseOfType(): T? {
    var current: Throwable? = this
    while (current != null) {
        if (current is T) return current
        current = current.cause
    }
    return null
}
