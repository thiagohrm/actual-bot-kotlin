FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY build/libs/*-all.jar app.jar
# O Cloud Run injeta a porta automaticamente
EXPOSE 8080 
ENTRYPOINT ["java", "-jar", "/app.jar"]
