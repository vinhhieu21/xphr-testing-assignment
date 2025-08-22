#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Must match docker-compose.yml
DB_CONTAINER="xphr"
DB_NAME="xphr"
DB_USER="admin"
SCHEMA_FILE="$SCRIPT_DIR/src/main/resources/database/schema.sql"
DATA_FILE="$SCRIPT_DIR/src/main/resources/database/data.sql"

# --- Preflight checks ---
if ! command -v docker >/dev/null 2>&1; then
  echo "Error: Docker is not installed or not in PATH." >&2
  echo "Install Docker Desktop/Engine: https://docs.docker.com/get-docker/" >&2
  exit 1
fi

if ! docker info >/dev/null 2>&1; then
  echo "Error: Docker daemon is not running. Start Docker Desktop/Engine and try again." >&2
  exit 1
fi

if ! docker compose version >/dev/null 2>&1; then
  if ! command -v docker-compose >/dev/null 2>&1; then
    echo "Error: Docker Compose is not installed." >&2
    echo "Install Docker Compose v2 (preferred) or docker-compose." >&2
    exit 1
  fi
fi

if [ ! -f "$SCHEMA_FILE" ]; then
  echo "Error: schema.sql not found at $SCHEMA_FILE" >&2
  exit 1
fi
if [ ! -f "$DATA_FILE" ]; then
  echo "Error: data.sql not found at $DATA_FILE" >&2
  exit 1
fi

# --- Start database service first ---
echo "Starting PostgreSQL container..."
if docker compose version >/dev/null 2>&1; then
  docker compose up -d --build db
else
  docker-compose up -d --build db
fi

# --- Wait for PostgreSQL readiness ---
echo "Waiting for database ($DB_CONTAINER) to be ready..."
ATTEMPTS=60
SLEEP=2
count=0
until docker exec "$DB_CONTAINER" pg_isready -U "$DB_USER" -d "$DB_NAME" >/dev/null 2>&1; do
  count=$((count+1))
  if [ "$count" -ge "$ATTEMPTS" ]; then
    echo "Error: Database not ready after $((ATTEMPTS*SLEEP)) seconds." >&2
    echo "Check logs: docker compose logs -f db" >&2
    exit 1
  fi
  sleep "$SLEEP"
  printf '.'
done
printf '\n'

# --- Apply schema and data ---
echo "Applying schema (this will DROP and CREATE tables)..."
docker exec -i "$DB_CONTAINER" psql -U "$DB_USER" -d "$DB_NAME" -v ON_ERROR_STOP=1 < "$SCHEMA_FILE"

echo "Applying data (sample records)..."
docker exec -i "$DB_CONTAINER" psql -U "$DB_USER" -d "$DB_NAME" -v ON_ERROR_STOP=1 < "$DATA_FILE"

echo

# --- Start the application locally ---
echo "Starting Spring Boot application locally with Maven..."
echo "Note: Disabling Spring SQL init (schema/data already applied by script)."
if [ -x "./mvnw" ]; then
  SPRING_SQL_INIT_MODE=never ./mvnw spring-boot:run
else
  SPRING_SQL_INIT_MODE=never mvn spring-boot:run
fi

# Note: spring-boot:run runs in the foreground. Stop with Ctrl+C.
