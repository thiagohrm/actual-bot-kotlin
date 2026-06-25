import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.*

fun Application.module() {
    routing {
        post("/webhook") {
            // 1. Validar se a requisição veio do Telegram (verificar token no header ou query)
            val secretHeader = call.request.headers["X-Telegram-Bot-Api-Secret-Token"]
            if (secretHeader != System.getenv("TELEGRAM_SECRET_TOKEN")) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }

            // 2. Receber o payload do Telegram
            val body = call.receiveText()
            
            // 3. Processar o comando (Ex: /gasto)
            val success = processTelegramMessage(body)
            
            if (success) call.respond(HttpStatusCode.OK) else call.respond(HttpStatusCode.BadRequest)
        }
    }
}
