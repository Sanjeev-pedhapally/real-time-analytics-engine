# Redis Layer
Connection pooling, pub/sub for real-time updates, and circuit breaker patterns. Production-grade error handling.

# Redis Caching Layer

## Overview
Production-grade Redis caching with connection pooling, pub/sub, clustering, and cache-aside pattern. Includes error handling and circuit breaker support.

## Setup
1. Build and run Redis with clustering:
   ```sh
   docker build -t analytics-redis .
   docker run -d --name redis -p 6379:6379 analytics-redis
   ```
2. Configure `redis.conf` for clustering and performance.
3. Integrate `RedisService` and `CacheManager` in your Java services.

## Features
- Connection pooling (Jedis)
- Pub/Sub for real-time updates
- Cache-aside pattern for fast reads
- Clustering and LRU eviction
- Error handling, logging

## Performance Notes
- Pool size: configurable
- Sub-ms cache latency, 10K+ ops/sec
- Circuit breaker: fallback on errors
