import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.serialization") version "2.3.21"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    id("org.jetbrains.kotlinx.kover") version "0.9.8"
    id("org.openapi.generator") version "7.23.0"

    application
}

group = "no.nav.sokos"

repositories {
    mavenCentral()
}

val ktorVersion = "3.4.3"
val logbackVersion = "1.5.34"
val logstashVersion = "9.0"
val micrometerVersion = "1.17.0"
val kotlinLoggingVersion = "3.0.5"
val janionVersion = "3.1.12"
val natpryceVersion = "1.6.10.0"
val kotestVersion = "6.1.11"
val kotlinxSerializationVersion = "1.11.0"
val mockOAuth2ServerVersion = "4.0.0"
val mockkVersion = "1.14.11"
val restAssuredVersion = "6.0.0"
val openApiValidatorVersion = "3.0.0"
val wireMockVersion = "3.13.2"

dependencies {

    // Ktor server
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-swagger:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")

    // Ktor client
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-apache5-jvm:$ktorVersion")

    // Security
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktorVersion")

    // Serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:$kotlinxSerializationVersion")

    // Monitorering
    implementation("io.ktor:ktor-server-metrics-micrometer-jvm:$ktorVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:$micrometerVersion")

    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingVersion")
    runtimeOnly("org.codehaus.janino:janino:$janionVersion")
    runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:$logstashVersion")

    // Config
    implementation("com.natpryce:konfig:$natpryceVersion")

    // Test
    testImplementation("io.ktor:ktor-server-test-host-jvm:$ktorVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("no.nav.security:mock-oauth2-server:$mockOAuth2ServerVersion")
    testImplementation("io.rest-assured:rest-assured:$restAssuredVersion")
    testImplementation("io.rest-assured:kotlin-extensions:$restAssuredVersion")
    testImplementation("com.atlassian.oai:swagger-request-validator-restassured:$openApiValidatorVersion")
    testImplementation("org.wiremock:wiremock-standalone:$wireMockVersion")
}

configurations.all {
    resolutionStrategy {
        eachDependency {
            if (requested.group == "tools.jackson.core" && requested.name == "jackson-core") {
                useVersion("3.1.1")
                because("Jackson Core: Document length constraint bypass in blocking, async, and DataInput parsers. Affected version >= 3.0.0, <= 3.1.0")
            }
            if (requested.group == "com.fasterxml.jackson.core" && requested.name == "jackson-core") {
                useVersion("2.21.1")
                because("jackson-core: Number Length Constraint Bypass in Async Parser Leads to Potential DoS Condition. Affected version >= 2.19.0, < 2.21.1")
            }
            if (requested.group == "io.netty" && requested.name == "netty-codec-http") {
                useVersion("4.2.13.Final")
                because(
                    "CVE-2026-42587: Netty HttpContentDecompressor maxAllocation bypass with br/zstd/snappy leads to decompression bomb DoS. Affected version = 4.2.11.Final, patched in >= 4.2.13.Final",
                )
            }
            if (requested.group == "io.netty" && requested.name == "netty-codec-http2") {
                useVersion("4.2.13.Final")
                because(
                    "CVE-2026-42587: Netty HttpContentDecompressor maxAllocation bypass with br/zstd/snappy leads to decompression bomb DoS. Affected version = 4.2.11.Final, patched in >= 4.2.13.Final",
                )
            }
            if (requested.group == "io.netty" && requested.name == "netty-transport-native-epoll") {
                useVersion("4.2.13.Final")
                because("CVE-2026-42577 >= 4.2.0.Alpha1, <= 4.2.12.Final")
            }
        }
    }
}

application {
    mainClass.set("no.nav.sokos.os.ekstern.api.ApplicationKt")
}

sourceSets {
    main {
        kotlin {
            srcDir("${layout.buildDirectory.get()}/generated/src/main/kotlin")
        }
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks {
    named("runKtlintCheckOverMainSourceSet").configure {
        dependsOn("openApiGenerate")
    }

    named("runKtlintFormatOverMainSourceSet").configure {
        dependsOn("openApiGenerate")
    }

    withType<KotlinCompile>().configureEach {
        dependsOn("ktlintFormat")
        dependsOn("openApiGenerate")
    }

    withType<Test>().configureEach {
        useJUnitPlatform()

        testLogging {
            showExceptions = true
            showStackTraces = true
            exceptionFormat = FULL
            events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }

        reports.forEach { report -> report.required.value(false) }

        finalizedBy(koverHtmlReport)
    }

    withType<GenerateTask>().configureEach {
        generatorName.set("kotlin")
        inputSpec.set("$rootDir/src/main/resources/spec/os-tilbakekreving-swagger.json")
        outputDir.set("${layout.buildDirectory.get()}/generated")
        modelPackage.set("no.nav.sokos.os.ekstern.api.os")
        configOptions.set(
            mapOf(
                "library" to "jvm-ktor",
                "serializationLibrary" to "kotlinx_serialization",
            ),
        )
        globalProperties.set(
            mapOf(
                "models" to "",
            ),
        )
    }

    ("build") {
        dependsOn("copyPreCommitHook")
    }

    register<Copy>("copyPreCommitHook") {
        from(".scripts/pre-commit")
        into(".git/hooks")
        filePermissions {
            user {
                execute = true
            }
        }
        doFirst {
            println("Installing git hooks...")
        }
        doLast {
            println("Git hooks installed successfully.")
        }
        description = "Copy pre-commit hook to .git/hooks"
        group = "git hooks"
        outputs.upToDateWhen { false }
    }
}
