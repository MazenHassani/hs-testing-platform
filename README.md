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

### 2. Configure application properties

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Then edit `application.properties` and fill in your credentials:

| Key | Description |
|-----|-------------|
| `sphere.endpoint` | Your Sphere Engine endpoint hash |
| `sphere.problems.token` | Sphere Engine Problems API token |
| `sphere.compilers.token` | Sphere Engine Compilers API token |
| `sendgrid.api.key` | SendGrid API key for invitation emails |
| `sendgrid.templateId` | SendGrid email template ID |
| `keycloak.auth-server-url` | Keycloak authentication server URL |

> ⚠️ **Never commit `application.properties`** — it's in `.gitignore`. Only `application.properties.example` (with placeholder values) is tracked.

---

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
