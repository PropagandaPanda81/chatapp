# Verwende ein Java-Basisimage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Kopiere das lokal gebaute JAR ins Docker-Image
COPY target/chat-app-0.0.1-SNAPSHOT.jar app.jar

# Exponiere Port 8080
EXPOSE 8080

# Starte die Anwendung
ENTRYPOINT ["java", "-jar", "app.jar"]
