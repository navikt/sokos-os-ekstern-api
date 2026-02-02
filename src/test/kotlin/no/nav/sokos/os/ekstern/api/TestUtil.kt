package no.nav.sokos.os.ekstern.api

object TestUtil {
    fun String.readFromResource(): String =
        TestUtil::class.java.classLoader
            .getResource(this)
            ?.readText()
            ?: throw RuntimeException("Kunne ikke lese fil: $this")
}
