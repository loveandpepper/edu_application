FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/business-service-1.0-SNAPSHOT.jar app.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-container:5432/postgres
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV SPRING_CLOUD_STREAM_KAFKA_BINDER_BROKERS=kafka:9092
ENTRYPOINT ["java", "-jar", "app.jar"]
