FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/console-service-1.0-SNAPSHOT.jar app.jar
ENV SPRING_APP_BASE_URL=http://business-service:8080/api/packages
ENTRYPOINT ["java", "-jar", "app.jar"]
