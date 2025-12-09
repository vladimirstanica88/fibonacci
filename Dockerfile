# Stage 1: Build
FROM gradle:8.3-jdk17 AS build
WORKDIR /app

# Copy full project
COPY . .

# Build the jar
RUN ./gradlew clean bootJar -x test

# Stage 2: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
USER appuser

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
