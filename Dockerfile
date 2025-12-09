FROM gradle:8.3-jdk17 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src

# Run all tests (unit + integration)
RUN ./gradlew clean test --no-daemon

# Build jar
RUN ./gradlew bootJar -x test --no-daemon

# Runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# create non-root user
RUN useradd -ms /bin/bash appuser
USER appuser

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
