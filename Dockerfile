FROM eclipse-temurin:17-jdk

WORKDIR /app

# Αντιγράφουμε όλα τα JARs στον φάκελο /app
COPY build/libs/ /app/

# Μετονομάζουμε το JAR σε crypto.jar μέσα στο container
RUN mv /app/*.jar /app/crypto.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/crypto.jar"]
