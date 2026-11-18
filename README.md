# NexusFlow – Integration Layer

NexusFlow is the integration layer responsible for reliable communication, data synchronization, and workflow orchestration between the core application and external systems.

It provides a centralized backend for managing integrations, coordinating synchronization processes, handling failures, and maintaining consistency across connected services.

Built with Java 21 and Spring Boot, NexusFlow is designed as a modular, multi-tenant service that can evolve independently while integrating with the broader application ecosystem.

## Responsibilities

The integration layer is responsible for:

- **Connector Management:** Establishing and managing connections with external systems.
- **Data Synchronization:** Coordinating reliable data exchange between connected services.
- **Sync Orchestration:** Managing synchronization jobs, execution states, and checkpoints.
- **Retry & Recovery:** Handling transient failures and supporting recoverable operations.
- **Webhook Processing:** Receiving and processing external system events.
- **Tenant Isolation:** Maintaining tenant-level separation across integration data and operations.
- **Audit & Monitoring:** Tracking integration activities, execution history, and operational health.

## Current Development Status

The initial foundation and database persistence layer are in place.

Implemented foundations include:

- Java 21 compatible Maven Spring Boot application.
- Domain-first modular architecture.
- Versioned REST health endpoint.
- Spring Boot Actuator health monitoring.
- PostgreSQL persistence foundation.
- Flyway database migrations.
- JPA entities and repositories.
- Tenant context foundation.
- Testcontainers integration testing.
- Spotless formatting and Checkstyle validation.
- GitHub Actions CI pipeline.
- Maven Wrapper and contributor/AI-agent instructions.

Security implementation, complete integration workflows, synchronization orchestration, and external connectors will be developed in subsequent phases.

## Technology Stack

- **Language:** Java 21
- **Framework:** Spring Boot
- **Build Tool:** Maven
- **Database:** PostgreSQL
- **Persistence:** Spring Data JPA / Hibernate
- **Database Migrations:** Flyway
- **Testing:** JUnit 5 / Testcontainers
- **Code Quality:** Spotless / Checkstyle
- **CI:** GitHub Actions

## Architecture

The application follows a domain-first modular layered architecture.

Each domain owns its HTTP controllers, application services, persistence adapters, entities, and DTOs. Cross-cutting concerns are maintained in dedicated packages.

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

### Architectural Principles

- Controllers handle HTTP and transport concerns.
- Services own business use cases and transaction boundaries.
- Repositories isolate database operations.
- DTOs define API boundaries.
- JPA entities are never exposed directly through API endpoints.
- Tenant isolation is considered throughout the data and application layers.
- Integration workflows are designed for reliability, observability, and extensibility.

## Run Locally

**Prerequisites:**

- Java 21 JDK
- Docker (for PostgreSQL integration tests)
- PostgreSQL database

Run the following commands from the project root:

```powershell
.\mvnw.cmd spotless:apply
.\mvnw.cmd verify
.\mvnw.cmd spring-boot:run
```

Application health endpoint:

```text
http://localhost:8080/api/v1/health
```

Actuator health endpoint:

```text
http://localhost:8080/actuator/health
```

### Database Configuration

Configure the following environment variables before starting the application:

```env
NEXUSFLOW_DATABASE_URL=
NEXUSFLOW_DATABASE_USERNAME=
NEXUSFLOW_DATABASE_PASSWORD=
```

Flyway applies database migrations during startup. Hibernate validates entity mappings without modifying the database schema.

## Development Commands

```powershell
.\mvnw.cmd spotless:apply
.\mvnw.cmd spotless:check
.\mvnw.cmd test
.\mvnw.cmd verify
```

PostgreSQL integration tests use Testcontainers. They execute when Docker is available and are skipped with an explicit test result when it is unavailable.

## Configuration & Security

Copy `.env.example` to `.env` when local environment overrides are required.

Never commit `.env` files, database credentials, API keys, or other secrets.

## Documentation

- [Architecture](docs/architecture.md)
- [Database Design](docs/database-design.md)
- [Contributor Guidelines](AGENTS.md)
- [AI Agent Instructions](CLAUDE.md)

---

**NexusFlow – The integration layer connecting applications, services, and external systems.**
