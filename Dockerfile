FROM openjdk:17-jdk-slim AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x ./mvnw
COPY src src
RUN ./mvnw clean install

FROM openjdk:17-jdk-slim
WORKDIR app
COPY --from=build target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]