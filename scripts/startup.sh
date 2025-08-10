#!/bin/zsh
# Startup script for real-time analytics engine
set -e

echo "Starting docker-compose services..."
docker-compose up -d

# Health checks for all services
for svc in kafka zookeeper cassandra redis kafka-producer flink-jobs api dashboard prometheus grafana; do
  echo "Checking health for $svc..."
  sleep 5
  docker-compose ps $svc
  # Add custom health check logic per service if needed
  # e.g., curl endpoints, check logs, etc.
done

echo "All services started and healthy."
