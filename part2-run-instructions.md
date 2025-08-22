# Part 2 — Run Instructions

This project now uses Docker only for PostgreSQL. The Spring Boot app runs locally via Maven.

## Recommended: one-script run (Linux/macOS)

Prerequisites:
- Docker Desktop (Windows/Mac) or Docker Engine with Docker Compose v2 (Linux)
- JDK 17+, Maven Wrapper (included)

Steps:
1. From the project root, run:
   ```bash
   ./run.sh
   ```
   - The script will:
     - Start the PostgreSQL container.
     - Wait until the database is ready.
     - Apply `schema.sql` and `data.sql` to create tables and load sample data.
     - Start the Spring Boot application locally using Maven.

2. Open the login page in your browser:
   - http://localhost:8080/login

3. Sign in with one of the in-memory users:
   - Admin: `admin` / `admin`
   - Employee: `tom` / `tom`
   - Employee: `jerry` / `jerry`

You will be redirected to `/web/reports` after login.

## Manual alternative

Prerequisites:
- Docker + Docker Compose v2
- JDK 17+, Maven 3.9+

1. Start only the database with Docker:
   ```bash
   docker compose up -d db
   ```

2. (Optional) Initialize schema and data using psql from the host, or rely on Spring Boot's SQL init at startup:
   - Files are located at `src/main/resources/database/schema.sql` and `data.sql`.

3. Run the application locally:
   ```bash
   ./mvnw spring-boot:run
   ```

## Notes
- The application reads database settings from environment variables with safe defaults:
  - `SPRING_DATASOURCE_URL` (default: `jdbc:postgresql://localhost:5432/xphr`)
  - `SPRING_DATASOURCE_USERNAME` (default: `admin`)
  - `SPRING_DATASOURCE_PASSWORD` (default: `admin`)
- To stop the database container: `docker compose down` (add `-v` to remove data volume if you mounted one).