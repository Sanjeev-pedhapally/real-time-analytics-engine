# Production Dockerfile for Kafka Producer
FROM openjdk:17-jdk
WORKDIR /app
COPY target/kafka-producer-1.0.0.jar /app/kafka-producer.jar
COPY src/main/resources/application.properties /app/application.properties
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
CMD ["sh", "-c", "java $JAVA_OPTS -jar kafka-producer.jar --spring.config.location=application.properties"]
