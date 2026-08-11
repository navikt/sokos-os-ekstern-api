package no.nav.sokos.os.ekstern.api.config

import kotlinx.serialization.Serializable

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.config.getAs
import io.ktor.server.config.withFallback

object PropertiesConfigNew {
    lateinit var config: ApplicationConfig
        private set

    val configuration by lazy {
        config.property("configuration").getAs<Configuration>()
    }

    val azureAdProperties by lazy {
        config.property("azureAdProperties").getAs<AzureAdProperties>()
    }

    val osConfiguration by lazy {
        config.property("osConfiguration").getAs<OsConfiguration>()
    }

    fun load(applicationConfig: ApplicationConfig) {
        if (!::config.isInitialized) {
            config = applicationConfig
        }
    }
}

fun ApplicationConfig.mergeWithEnv(): ApplicationConfig {
    val hoconConfig = HoconApplicationConfig(ConfigFactory.load())
    val environment =
        (System.getenv("NAIS_CLUSTER_NAME") ?: System.getProperty("NAIS_CLUSTER_NAME"))
            ?.lowercase()
            ?.substringBefore("-")
            ?: propertyOrNull("ktor.environment")?.getString()
            ?: "local"
    val environmentConfig = ApplicationConfig("application-$environment.conf")
    return this overriding environmentConfig overriding hoconConfig
}

infix fun ApplicationConfig.overriding(other: ApplicationConfig): ApplicationConfig = this.withFallback(other)

@Serializable
data class Configuration(
    val naisAppName: String,
    val useAuthentication: Boolean,
)

@Serializable
data class AzureAdProperties(
    val clientId: String,
    val wellKnownUrl: String,
)

@Serializable
data class OsConfiguration(
    val endpointUrl: String,
    val trustStore: String,
    val trustStorePassword: String,
)
