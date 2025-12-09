# Stage 1: Build
FROM gradle:8.3-jdk17 AS build
WORKDIR /app

# Copy gradle files
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Build dependencies only (caching)
RUN ./gradlew build -x test --write-locks

# Copy source code
COPY src ./src

# Build the jar
RUN ./gradlew bootJar -x test

# Stage 2: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Create non-root user
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
USER appuser

# Copy jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
