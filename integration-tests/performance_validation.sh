#!/bin/zsh
# Performance validation script for real-time analytics engine
set -e

# Generate high-rate events
for i in {1..10}; do
  curl -X POST http://localhost:8080/api/analytics/metrics || true
  sleep 1
end

# Measure API latency
start=$(date +%s%3N)
curl -w "API Latency: %{time_total}s\n" -o /dev/null -s http://localhost:8080/api/analytics/metrics
end=$(date +%s%3N)
latency=$((end-start))
echo "Measured API latency: $latency ms"

# Validate throughput (manual or via Prometheus/Grafana)
echo "Check Prometheus/Grafana dashboards for throughput > 10,000 events/sec and latency < 100ms."
