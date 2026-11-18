# NexusFlow

NexusFlow is a standalone, multi-tenant integration orchestration service for reliable data synchronization between external systems.

## Phase 1 status

The repository contains the Spring Boot foundation, Java 21 build target, health surfaces, PostgreSQL persistence foundations, formatting and style checks, CI, and instructions for human and AI contributors. Security, domain workflows, and external connectors are deliberately deferred to later phases.

## Run locally

Install a Java 21 JDK, then run:

```powershell
.\mvnw.cmd spotless:apply
.\mvnw.cmd verify
.\mvnw.cmd spring-boot:run
```

The application endpoint is `http://localhost:8080/api/v1/health`. Operational health is available at `http://localhost:8080/actuator/health`.

PostgreSQL is required. Configure `NEXUSFLOW_DATABASE_URL`, `NEXUSFLOW_DATABASE_USERNAME`, and `NEXUSFLOW_DATABASE_PASSWORD` before starting the application. Flyway applies migrations on startup; Hibernate validates mappings and does not modify the schema.

## Architecture

The application follows a domain-first modular layered design. Each domain owns its HTTP controller, service layer, persistence adapters, entities, and DTOs. Cross-cutting concerns remain in dedicated packages.

```text
com.nexusflow
├── common
├── config
├── security
├── tenant
├── connector
├── sync
├── webhook
├── audit
└── monitoring
```

Controllers coordinate transport concerns only. Services own use cases and transaction boundaries. Repositories isolate database access. DTOs form the public boundary; JPA entities will never be returned from API endpoints.

## Development commands

```powershell
.\mvnw.cmd spotless:apply
.\mvnw.cmd spotless:check
.\mvnw.cmd test
.\mvnw.cmd verify
```

PostgreSQL integration tests use Testcontainers. They run automatically where Docker is available and are skipped with an explicit test result where it is not.

## Configuration

Copy `.env.example` to `.env` when local environment overrides are needed. Do not commit `.env` files or credentials.

## Documentation

Architecture decisions and the roadmap are in [docs/architecture.md](docs/architecture.md). Contributor and agent constraints are in [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md).
