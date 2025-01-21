FROM openjdk:21-jdk-slim

WORKDIR /app

COPY ./build/libs/*.jar devarium.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "devarium.jar"]