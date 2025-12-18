# ================================================================
# Dockerfile for Restaurant Service
# ================================================================
# This uses a multi-stage build:
# Stage 1: Build the application with Maven
# Stage 2: Create a lightweight runtime image with just the JAR
# ================================================================

# -------------------- BUILD STAGE --------------------
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml first (for Docker layer caching)
COPY pom.xml .

# Download dependencies (this layer is cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests for faster builds)
RUN mvn clean package -DskipTests

# -------------------- RUNTIME STAGE --------------------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the service port
EXPOSE 8082

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD wget --quiet --tries=1 --spider http://localhost:8082/api/restaurants || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]