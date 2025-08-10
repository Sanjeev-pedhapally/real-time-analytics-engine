# Spring Boot WebFlux API Dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/api-1.0.0.jar /app/api.jar
COPY src/main/resources/application.properties /app/application.properties
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
CMD ["sh", "-c", "java $JAVA_OPTS -jar api.jar --spring.config.location=application.properties"]
EXPOSE 8080
