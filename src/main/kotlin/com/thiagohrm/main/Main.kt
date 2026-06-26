package com.thiagohrm.main

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.statement.* // Importante para ler o corpo da resposta do Actual

val client = HttpClient(CIO) {
    install(ContentNegotiation) { json() }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    routing {
        post("/webhook") {
            // 1. Validação de segurança
            val secretHeader = call.request.headers["X-Telegram-Bot-Api-Secret-Token"]
            val envToken = System.getenv("TELEGRAM_SECRET_TOKEN") ?: ""
            
            if (secretHeader?.trim() != envToken.trim()) {
                println("AVISO: Tentativa de acesso não autorizada. Header: $secretHeader")
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            // 2. Recebimento e Log de Diagnóstico
            val body = call.receiveText()
            println("RECEBIDO TELEGRAM: $body") 
            
            // 3. Encaminhamento
            val success = sendToActualBudget(body)
            
            if (success) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Falha ao comunicar com Actual Budget")
            }
        }
    }
}

suspend fun sendToActualBudget(payload: String): Boolean {
    return try {
        val response = client.post("https://actual.thiagohrm.uk/api/transacao") {
            header("CF-Access-Client-Id", System.getenv("CF_ACCESS_CLIENT_ID"))
            header("CF-Access-Client-Secret", System.getenv("CF_ACCESS_CLIENT_SECRET"))
            contentType(ContentType.Application.Json)
            setBody(payload)
        }
        
        // Log para ver o que o Actual respondeu
        val responseBody = response.bodyAsText()
        println("RESPOSTA ACTUAL: ${response.status} - $responseBody")
        
        response.status.isSuccess()
    } catch (e: Exception) {
        println("ERRO CRÍTICO NA CHAMADA HTTP: ${e.stackTraceToString()}")
        false
    }
}