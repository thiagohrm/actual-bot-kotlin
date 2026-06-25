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
import kotlinx.coroutines.runBlocking

// Configuração do Cliente HTTP que será usado para falar com o Actual
val client = HttpClient(CIO) {
    install(ContentNegotiation) { json() }
}

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    routing {
        post("/webhook") {
            // 1. Validação de segurança do Telegram
            val secretHeader = call.request.headers["X-Telegram-Bot-Api-Secret-Token"]
            if (secretHeader != System.getenv("TELEGRAM_SECRET_TOKEN")) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            val body = call.receiveText()
            
            // 2. Encaminhar para o Actual Budget com credenciais Cloudflare
            val success = sendToActualBudget(body)
            
            if (success) call.respond(HttpStatusCode.OK) else call.respond(HttpStatusCode.InternalServerError)
        }
    }
}

suspend fun sendToActualBudget(payload: String): Boolean {
    return try {
        val response = client.post("https://actual.thiagohrm.uk/api/transacao") {
            // Headers exigidos pelo Cloudflare Access para Service Tokens
            header("CF-Access-Client-Id", System.getenv("CF_ACCESS_CLIENT_ID"))
            header("CF-Access-Client-Secret", System.getenv("CF_ACCESS_CLIENT_SECRET"))
            
            contentType(ContentType.Application.Json)
            setBody(payload)
        }
        response.status.isSuccess()
    } catch (e: Exception) {
        false
    }
}
