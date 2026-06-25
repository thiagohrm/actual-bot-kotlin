plugins {
    kotlin("jvm") version "2.0.0"
    // Removemos o io.ktor.plugin temporariamente para evitar conflito
    id("com.github.johnrengelman.shadow") version "8.1.1" 
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.3.11")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.11")
    implementation("io.ktor:ktor-client-cio:2.3.11")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.11")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.11")
    implementation("org.slf4j:slf4j-simple:2.0.12")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.23.0")
}

// Configuração manual do Shadow (mais robusta)
tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    archiveBaseName.set("actual-bot")
    archiveClassifier.set("all")
    archiveVersion.set("")
}
