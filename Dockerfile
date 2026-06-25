FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY build/libs/actual-bot-kotlin.jar /app/bot.jar
ENTRYPOINT ["java", "-jar", "/app/bot.jar"]