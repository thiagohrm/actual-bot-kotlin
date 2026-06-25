plugins {
    kotlin("jvm") version "2.0.0"
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

// Criando o "Fat Jar" manualmente (nativa do Gradle)
tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    
    // Esta parte "embute" as dependências dentro do jar
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}