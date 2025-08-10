#!/bin/zsh
# Health check script for all services
set -e

for svc in kafka zookeeper cassandra redis kafka-producer flink-jobs api dashboard prometheus grafana; do
  echo "Checking health for $svc..."
  docker-compose ps $svc
  # Add custom health check logic per service if needed
  # e.g., curl endpoints, check logs, etc.
done
