FROM eclipse-temurin:17-jdk
WORKDIR /app

# Αντιγράφουμε όλα τα JARs
COPY build/libs/ /app/

# Παίρνουμε μόνο το JAR που δεν έχει -plain
RUN sh -c 'mv /app/*SIGNAL.jar /app/crypto.jar'

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/crypto.jar"]
