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

```python
Hi Team,

I’m happy to share that my paper “AI-Powered Intrusion Detection with SHAP Explainability and Feedback Loop: A Modular Pipeline for Cyber Threat Classification” has been accepted 🎉.

In this work, I developed a modular AI-driven pipeline that combines XGBoost/Random Forest classifiers, SHAP-based explainability, and a feedback loop for continuous retraining. The system reached 99.96% accuracy and a 0.92 Macro F1-score on the CICIDS2017 dataset, showing strong performance across diverse attack types
.

🔹 Why it matters for healthcare:

Protects sensitive EHR systems and hospital networks from intrusions.

Secures IoT-connected medical devices (infusion pumps, monitoring equipment, etc.).

Provides explainable alerts that support compliance with HIPAA/GDPR.

Adaptable to zero-day threats, ensuring long-term resilience in critical healthcare environments.

Excited about how this research can strengthen cybersecurity in healthcare systems while keeping patient data safe and operations reliable.
```
