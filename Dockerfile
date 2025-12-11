# Stage 1: Build
FROM gradle:8.3-jdk17 AS build
WORKDIR /app

# Stage 1: Runtime-only image
FROM eclipse-temurin:17-jre
WORKDIR /app

RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
USER appuser

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

