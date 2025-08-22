# Database-only Dockerfile
# Uses Postgres 16 (alpine) to avoid platform mismatch with prior volumes
FROM postgres:16-alpine

# --- Default credentials (override at build or run time) ---
ARG POSTGRES_DB=xphrdb
ARG POSTGRES_USER=admin
ARG POSTGRES_PASSWORD=admin

ENV POSTGRES_DB=${POSTGRES_DB} \
    POSTGRES_USER=${POSTGRES_USER} \
    POSTGRES_PASSWORD=${POSTGRES_PASSWORD}

# --- Optional: initialization scripts ---
# Place any *.sql or *.sh files under ./db-init/ and they will run on first init
# (Directory may be empty; create it in your repo if you want to seed data.)
# COPY db-init/ /docker-entrypoint-initdb.d/

# Persist data outside the container
VOLUME ["/var/lib/postgresql/data"]

# Healthcheck (works best when run via docker-compose, but safe here too)
HEALTHCHECK --interval=5s --timeout=3s --retries=20 CMD pg_isready -U "$POSTGRES_USER" -d "$POSTGRES_DB" || exit 1

EXPOSE 5432

# Entrypoint & CMD are inherited from the postgres base image