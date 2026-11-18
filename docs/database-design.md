# Database Design

## Tenant ownership

NexusFlow uses shared-schema, shared-database multi-tenancy. A tenant is the root boundary for customer-owned data. Every independently queried tenant-owned table stores `tenant_id` and references `tenants(id)`. Connector-owned records link through `connector_id`, and connectors themselves belong to a tenant, preserving the ownership chain without duplicating tenant IDs.

Application-level isolation is not complete in Phase 2 because authenticated identity resolution arrives in Phase 3. The foundation consists of a `TenantContext` contract, tenant-explicit repository methods, and tests proving those methods do not return another tenant's records. Future authenticated requests will populate the context from a verified principal, never from an arbitrary request-body value.

## Schema strategy

Flyway owns the schema through ordered, immutable migrations. Hibernate validates mappings with `ddl-auto: validate`; it never creates or alters production tables. This makes database changes reviewable and reproducible across environments.

UUID primary keys make identifiers safe to generate independently. Foreign keys protect ownership relationships and prevent orphaned records. Check constraints restrict persisted state values. Unique constraints and partial unique indexes prevent duplicate attempts, checkpoints, webhook deliveries, idempotency requests, and active external connector identities.

## Deliberate additions

`connectors.external_account_ref` identifies the provider account without storing tokens. `credential_reference` is an opaque pointer for a later secret-store integration; no credential data belongs in this table. Mutable tenant, connector, and job rows include `version` for optimistic locking. `audit_logs.metadata` is JSONB because audit metadata has variable shape but must remain queryable.

## Relationships

```text
tenants 1 --- * connectors 1 --- * sync_jobs 1 --- * sync_job_attempts
                              |             
                              + --- * sync_checkpoints
                              + --- * webhook_events
tenants 1 --- * audit_logs
tenants 1 --- * idempotency_records
```

## Local configuration

Set `NEXUSFLOW_DATABASE_URL`, `NEXUSFLOW_DATABASE_USERNAME`, and `NEXUSFLOW_DATABASE_PASSWORD`. The application requires PostgreSQL and has no in-memory fallback. Run migrations at startup or explicitly with `./mvnw.cmd flyway:migrate` after exporting those variables.
