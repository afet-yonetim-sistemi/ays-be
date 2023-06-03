FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar /app/ays-be.jar

EXPOSE 9790


ENTRYPOINT ["java", "-jar", "/app/ays-be.jar"]

LABEL maintainer="ays-be"