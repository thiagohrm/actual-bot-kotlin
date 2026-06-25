plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.11"
    id("com.github.johnrengelman.shadow") version "8.1.1" // Adicione esta linha
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

// Garante que o build gere o jar com todas as dependências
tasks.shadowJar {
    archiveBaseName.set("actual-bot")
    archiveClassifier.set("all")
    archiveVersion.set("")
    manifest {
        attributes["Main-Class"] = "MainKt" // Verifique se o seu arquivo é Main.kt
    }
}
