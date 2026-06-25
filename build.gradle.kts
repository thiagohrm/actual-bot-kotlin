plugins {
    kotlin("jvm") version "2.0.21"
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

sourceSets {
    main {
        kotlin.setSrcDirs(listOf("src/main/kotlin"))
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.thiagohrm.main.MainKt"
    }
    
    // Isso copia todas as dependências para dentro do JAR
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    
    // Isso resolve o conflito de arquivos duplicados no META-INF
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}