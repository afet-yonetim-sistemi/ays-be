FROM openjdk:17-jdk-slim AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x ./mvnw
RUN ./mvnw dependency:resolve
#RUN ./mvnw clean install -Dmaven.test.skip
COPY src src
RUN ./mvnw package -Dmaven.test.skip

FROM openjdk:17-jdk-slim
WORKDIR app
COPY --from=build target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
