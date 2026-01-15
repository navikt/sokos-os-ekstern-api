package no.nav.sokos.os.ekstern.api

internal const val API_BASE_PATH = "/api/v1"

object TestUtil {
    fun String.readFromResource(): String =
        TestUtil::class.java.classLoader
            .getResource(this)
            ?.readText()
            ?: throw RuntimeException("Kunne ikke lese fil: $this")
}
