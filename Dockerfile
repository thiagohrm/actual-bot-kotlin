FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# O asterisco (*) vai pegar qualquer arquivo .jar que estiver dentro da pasta
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]