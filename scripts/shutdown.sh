#!/bin/zsh
# Shutdown script for real-time analytics engine
set -e

echo "Stopping all services..."
docker-compose down

echo "All services stopped."
