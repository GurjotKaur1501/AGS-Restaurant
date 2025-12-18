#!/usr/bin/env bash
set -euo pipefail

BASE_URL="http://localhost:8082"
FRONTEND_URL="http://localhost:3000"

CLEAN=false
NO_SEED=false

for arg in "$@"; do
  case "$arg" in
    --clean) CLEAN=true ;;
    --no-seed) NO_SEED=true ;;
  esac
done

echo "Starting Restaurant Booking System..."

docker info >/dev/null 2>&1 || { echo "Docker is not running."; exit 1; }

if $CLEAN; then
  echo "Cleaning containers + volumes..."
  docker-compose down -v || true
fi

docker-compose up -d --build

echo -n "Waiting for restaurant-service "
for i in {1..60}; do
  if curl -fsS --connect-timeout 2 --max-time 3 "${BASE_URL}/api/restaurants" >/dev/null 2>&1; then
    echo ""
    echo "restaurant-service is up."
    break
  fi
  echo -n "."
  sleep 1
  if [[ $i -eq 60 ]]; then
    echo ""
    echo "restaurant-service did not become ready in time."
    exit 1
  fi
done

if ! $NO_SEED; then
  echo "Seeding demo data..."

  curl -fsS -X POST "${BASE_URL}/api/restaurants" -H "Content-Type: application/json" \
    -d '{"name":"La Petite Bistro","address":"Ostra Hamngatan 5, 411 10, Goteborg","cuisineType":"FRENCH"}' >/dev/null || true

  curl -fsS -X POST "${BASE_URL}/api/restaurants" -H "Content-Type: application/json" \
    -d '{"name":"Oceanview Grill","address":"Avenyn 12, 411 36, Goteborg","cuisineType":"ITALIAN"}' >/dev/null || true

  curl -fsS -X POST "${BASE_URL}/api/restaurants" -H "Content-Type: application/json" \
    -d '{"name":"Sakura Sushi","address":"Vasagatan 22, 411 24, Goteborg","cuisineType":"ASIAN"}' >/dev/null || true

  # These IDs only work if DB is fresh; kept simple by design.
  curl -fsS -X POST "${BASE_URL}/api/tables" -H "Content-Type: application/json" \
    -d '{"tableNumber":1,"seats":4,"available":true,"restaurantId":1}' >/dev/null || true
  curl -fsS -X POST "${BASE_URL}/api/tables" -H "Content-Type: application/json" \
    -d '{"tableNumber":2,"seats":2,"available":true,"restaurantId":1}' >/dev/null || true

  curl -fsS -X POST "${BASE_URL}/api/tables" -H "Content-Type: application/json" \
    -d '{"tableNumber":1,"seats":6,"available":true,"restaurantId":2}' >/dev/null || true
  curl -fsS -X POST "${BASE_URL}/api/tables" -H "Content-Type: application/json" \
    -d '{"tableNumber":2,"seats":4,"available":true,"restaurantId":2}' >/dev/null || true

  curl -fsS -X POST "${BASE_URL}/api/tables" -H "Content-Type: application/json" \
    -d '{"tableNumber":1,"seats":4,"available":true,"restaurantId":3}' >/dev/null || true
fi

echo "Done."
echo "Frontend: ${FRONTEND_URL}"
echo "API:      ${BASE_URL}/api/restaurants"
