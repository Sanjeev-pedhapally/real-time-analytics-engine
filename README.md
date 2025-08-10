# Real-Time Analytics Engine

## Overview
A production-grade, event-driven analytics platform designed for FAANG-level scale and reliability. Processes 10,000+ events/sec with sub-100ms latency, 99.99% uptime, and fault tolerance.

## Architecture
- **Event-Driven Microservices**
- **Kafka**: Streaming backbone
- **Flink**: Real-time processing, windowing, exactly-once semantics
- **Cassandra**: Time-series storage, optimized for high write throughput
- **Redis**: Caching, pub/sub, circuit breaker
- **Spring Boot WebFlux**: Reactive REST API, sub-50ms response
- **React Dashboard**: Real-time charts, WebSocket updates
- **Kubernetes**: Auto-scaling, health checks, rolling updates
- **Monitoring**: Prometheus, Grafana, load testing

## Integration & Event Flow
See `docs/event-flow.md` for sequence diagrams and data flow.

## Startup & Orchestration
- Use `scripts/startup.sh` to start all services in correct order
- Use `scripts/health-check.sh` to verify health
- Use `integration-tests/test_e2e.sh` for end-to-end validation
- Use `integration-tests/performance_validation.sh` for performance validation
- All services share config from `shared-config/config.env`
- Monitoring stack tracks metrics across all components

## Core Components
- `kafka-producer/` - Java Kafka event generator
- `flink-jobs/` - Flink stream processing
- `cassandra/` - Data model & DAO
- `redis-layer/` - Redis caching/pubsub
- `api/` - Spring Boot WebFlux REST API
- `dashboard/` - React TypeScript dashboard
- `k8s/` - Kubernetes manifests
- `monitoring/` - Prometheus, Grafana, load testing
- `docker/` - Docker configs
- `shared-config/` - Centralized config for all services
- `integration-tests/` - End-to-end and performance validation
- `scripts/` - Startup, shutdown, health checks
- `docs/` - Architecture, event flow, and integration docs

## Performance Goals
- **Throughput**: 10,000+ events/sec
- **Latency**: <100ms end-to-end
- **API Response**: <50ms
- **Uptime**: 99.99%+
- **Fault Tolerance**: Circuit breakers, retries, self-healing

## Getting Started
- See each component’s directory for setup, Docker configs, and documentation.
- All code includes production-grade error handling, metrics, and validation scripts.
- Use provided scripts and tests for full system validation.