# XPHR Testing Assignment

A Spring Boot 3 (Java 17) application demonstrating a work hours report UI rendered with JSP and secured with Spring Security. Data is stored in PostgreSQL.

This project now runs PostgreSQL in Docker and the Spring Boot app locally.

## Quick start

Linux/macOS (recommended):
- Run the helper script from the project root:
  ```bash
  ./run.sh
  ```
  The script will start the PostgreSQL container, wait until it is ready, apply schema/data, and then launch the app locally via Maven.

Manual alternative:
1. Start only the database:
   ```bash
   docker compose up -d db
   ```
2. Start the app locally:
   ```bash
   ./mvnw spring-boot:run
   ```

Open the app: http://localhost:8080/login

Sign-in credentials:
- Admin: admin / admin
- Employee: tom / tom
- Employee: jerry / jerry

For more details, see part2-run-instructions.md

## Project structure
- `src/main/java/...` — Spring Boot application code
- `src/main/webapp/WEB-INF/jsp` — JSP views
- `src/main/resources/application.properties` — application configuration
- `src/main/resources/database` — Postgres init scripts (schema + sample data)
- `docker-compose.yml` — Docker Compose for PostgreSQL only

## Points to improve
- Add more unit and integration tests.
- Enhance error handling, logging, and security hardening.
- Improve UI/UX and responsiveness.
- Add API documentation (Swagger/OpenAPI).