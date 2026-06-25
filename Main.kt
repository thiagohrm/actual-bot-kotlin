import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.*
import com.google.auth.oauth2.GoogleCredentials
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenCommand
import com.google.auth.oauth2.ServiceAccountCredentials

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

fun generateIdToken(targetAudience: String): String {
    val credentials = GoogleCredentials.getApplicationDefault()
    
    if (credentials is ServiceAccountCredentials) {
        // O GCP permite trocar o token padrão por um assinado (ID Token)
        val idTokenProvider = credentials.toBuilder().build()
        // Solicita um token com o 'audience' (o público-alvo, que é o seu Actual Budget)
        return idTokenProvider.refreshIdToken(targetAudience).tokenValue
    }
    throw IllegalStateException("Credenciais não suportam OIDC")
}

// No seu roteamento, ao chamar o Actual:
val token = generateIdToken("https://seu-actual-budget.com")
val response = client.post("https://seu-actual-budget.com/api/transacao") {
    header(HttpHeaders.Authorization, "Bearer $token")
    setBody(suaTransacaoJson)
}
