FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY build/libs/actual-bot-kotlin.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]