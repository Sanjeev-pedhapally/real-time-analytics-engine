# Flink Job Dockerfile
FROM flink:1.19.0-java17
WORKDIR /app
COPY target/flink-jobs-1.0.0.jar /app/flink-jobs.jar
COPY flink-conf.yaml /opt/flink/conf/flink-conf.yaml
CMD ["flink", "run", "/app/flink-jobs.jar"]
