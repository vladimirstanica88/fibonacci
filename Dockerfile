# ============================
# Stage 1 — BUILD
# ============================
FROM gradle:8.3-jdk17 AS build
WORKDIR /app

# Copy Gradle descriptors first for caching
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Download dependencies (cached layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy sources
COPY src ./src

# Run tests including integration tests
# -> preferably to be done in separate gradle task that can run on a separate pipeline but for demo reasons let's asume that we can cut corners
RUN ./gradlew clean test integrationTest --no-daemon

# Build jar
RUN ./gradlew bootJar --no-daemon


# ============================
# Stage 2 — RUNTIME
# ============================
FROM eclipse-temurin:17-jre
WORKDIR /app

# Create non-root user (security best practice)
RUN useradd -ms /bin/bash appuser
USER appuser

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
