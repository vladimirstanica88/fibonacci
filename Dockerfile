# 1. Build stage
FROM gradle:8.3-jdk17 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN ./gradlew build -x test --write-locks

COPY src ./src
# rulează toate testele
RUN ./gradlew test --no-daemon

RUN ./gradlew bootJar -x test

# 2. Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
