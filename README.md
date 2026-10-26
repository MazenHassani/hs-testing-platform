# HS Testing Platform

A Spring Boot web-based testing and assessment platform with [Sphere Engine](https://sphere-engine.com/) integration for automated code execution and grading. Built for Harbour.Space University.

**Built:** ~2021

---

## What It Does

- Manages programming assignments and tests for courses
- Submits student code to Sphere Engine (Problems + Compilers APIs) for sandboxed execution and grading
- Handles user authentication via Keycloak (SSO)
- Sends class invitation emails via SendGrid
- Exposes a REST API documented with Swagger/OpenAPI

---

## Setup

### 1. Clone the repo

```bash
git clone <repo-url>
cd hs-testing-platform
```

### 2. Provision external credentials

Follow [CREDENTIALS_SETUP.md](CREDENTIALS_SETUP.md) to supply database, mail and service credentials through the process environment or deployment secret store. The tracked application properties now reference environment variables and contain no deployable credential defaults. Do not overwrite them with a credentials file or commit real values.

The same requirements apply to integration-test service tokens. Existing database volumes retain their own credentials: changing source configuration alone does not rotate those accounts.

## Running with Docker

```bash
docker-compose -f docker-compose.dev.yml up
```

The API will be available at `http://localhost:8080/api`.

Swagger UI: `http://localhost:8080/api/private/swagger-ui/index.html?configUrl=/api/api-docs/swagger-config`

---

## Running Locally (without Docker)

Requires Java 11+ and Maven.

```bash
./mvnw spring-boot:run
```

---

## Tech Stack

- **Java / Spring Boot** — REST API backend
- **Sphere Engine** — sandboxed code execution and automated grading
- **Keycloak** — authentication and role-based access control
- **SendGrid** — transactional email (class invitations)
- **Docker** — containerized development and deployment
- **Springdoc / OpenAPI** — API documentation
