# Architecture

## System shape

NexusFlow is a modular monolith packaged as a single Spring Boot service. It is independently deployable and intentionally has clear domain boundaries so modules can be extracted only if operational evidence justifies doing so.

The request path is controller to service to repository to database. Background execution will use the same application-service layer through a job-processing interface. That keeps synchronous APIs and later message consumers consistent in authorization, idempotency, audit, and transaction behavior.

## Module boundaries

| Module | Responsibility |
| --- | --- |
| `tenant` | Tenant lifecycle, identity, status, and isolation policy |
| `connector` | Provider-neutral connector lifecycle and provider adapters |
| `sync` | Job orchestration, attempts, checkpoints, retry, and notifications |
| `webhook` | Signature verification, event deduplication, recovery, and dispatch |
| `audit` | Security-safe activity records |
| `security` | Authentication, roles, and tenant authorization |
| `monitoring` | Health, metrics, correlation, and structured operational signals |
| `common` | Small shared primitives with no domain ownership |

## Phase 1 decisions

- Java 21 is the build target for records, modern language features, and long-term support.
- Spring Boot supplies the application runtime, validation, HTTP layer, and Actuator.
- The public health endpoint proves the versioned API convention. Actuator remains the operational health endpoint.
- PostgreSQL, JPA, Flyway, Spring Security, messaging, and OpenAPI will be added only in their designated phases to keep this initial executable surface small.

## Persistence model

Phase 2 provides tenant-rooted tables using UUID keys and foreign keys: tenants, connectors, sync jobs, job attempts, checkpoints, webhook events, audit logs, and idempotency records. Tenant identifiers are indexed and included in ownership-aware queries. Optimistic locking guards mutable tenant, connector, and job resources. Details are in `docs/database-design.md`.
