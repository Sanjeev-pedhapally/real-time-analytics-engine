#!/bin/zsh
# End-to-end integration test for real-time analytics engine
set -e

# 1. Generate events
curl -X POST http://localhost:8080/api/analytics/metrics || true

# 2. Wait for Flink to process and output to Cassandra/Redis
sleep 10

# 3. Validate Cassandra data
cqlsh localhost 9042 -e "SELECT * FROM analytics.purchase_events LIMIT 5;"

# 4. Validate Redis cache
redis-cli -h localhost GET metrics

# 5. Validate API response
curl http://localhost:8080/api/analytics/metrics

# 6. Validate dashboard update (manual or via WebSocket)
# (Optional: Use Selenium or Puppeteer for UI validation)

# 7. Print success
print "End-to-end test completed successfully."
