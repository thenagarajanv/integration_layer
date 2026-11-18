# Database Schema

Database implementation begins in Phase 2. PostgreSQL and Flyway will manage immutable schema migrations. The planned tenant-rooted tables are `tenants`, `connectors`, `sync_jobs`, `sync_job_attempts`, `sync_checkpoints`, `webhook_events`, `audit_logs`, and `idempotency_records`.

Every business table will carry a tenant identifier, UUID primary key, timestamps, foreign-key relationships where applicable, and indexes that support tenant-scoped lookups.
