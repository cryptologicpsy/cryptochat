FROM eclipse-temurin:17-jdk
WORKDIR /app

# Αντιγράφουμε όλα τα jars στο /app
COPY build/libs/*.jar /app/

# Ανοίγουμε το port
EXPOSE 8080

# Τρέχουμε το Spring Boot jar
ENTRYPOINT ["java", "-jar", "/app/crypto-0.0.1-SNAPSHOT.jar"]
