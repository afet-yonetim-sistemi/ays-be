# Stage 1: Build stage
FROM maven:3.9.9-amazoncorretto-17-al2023 AS build

# Copy Maven files for dependency resolution
COPY pom.xml ./
COPY .mvn .mvn

# Copy application source code
COPY src src

# Copy settings.xml file for private repository access
COPY settings.xml settings.xml

# Build the project and create the executable JAR
RUN mvn --settings settings.xml clean install -DskipTests

# Stage 2: Run stage
FROM amazoncorretto:17.0.13-al2023-headless

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build target/*.jar /app/ays-be.jar

# Expose port 9790
EXPOSE 9790

# Set the entrypoint command for running the application
ENTRYPOINT ["java", "-jar", "/app/ays-be.jar"]
