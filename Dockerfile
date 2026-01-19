# Βασικό image με Java 17
FROM eclipse-temurin:17-jdk

# Ορίζουμε το working directory μέσα στο container
WORKDIR /app

# Αντιγράφουμε όλα τα JARs από το Gradle build στον container
# και μετονομάζουμε το πρώτο JAR σε crypto.jar
COPY build/libs/*.jar /app/crypto.jar

# Ανοίγουμε το port που τρέχει η Spring Boot app
EXPOSE 8080

# Τρέχουμε το Spring Boot JAR
ENTRYPOINT ["java", "-jar", "/app/crypto.jar"]
