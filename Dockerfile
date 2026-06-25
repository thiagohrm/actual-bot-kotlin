FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# O nome abaixo deve ser EXATAMENTE o que definimos no shadowJar acima
COPY build/libs/actual-bot-all.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
