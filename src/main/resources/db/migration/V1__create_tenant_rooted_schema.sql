CREATE TABLE tenants (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(32) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_tenants_status CHECK (status IN ('ACTIVE', 'SUSPENDED', 'DEACTIVATED'))
);

CREATE TABLE connectors (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    provider VARCHAR(32) NOT NULL,
    external_account_ref VARCHAR(255),
    credential_reference VARCHAR(255),
    display_name VARCHAR(255) NOT NULL,
    status VARCHAR(32) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_connectors_provider CHECK (provider IN ('GOOGLE_DRIVE', 'MICROSOFT_365', 'GITHUB')),
    CONSTRAINT ck_connectors_status CHECK (status IN ('PENDING_AUTH', 'ACTIVE', 'DISCONNECTED', 'ERROR')),
    CONSTRAINT uq_connectors_id_tenant UNIQUE (id, tenant_id)
);

CREATE UNIQUE INDEX uq_connectors_active_account ON connectors (tenant_id, provider, external_account_ref)
    WHERE external_account_ref IS NOT NULL AND status <> 'DISCONNECTED';
CREATE INDEX idx_connectors_tenant_id ON connectors (tenant_id);

CREATE TABLE sync_jobs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    connector_id UUID NOT NULL,
    job_type VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,
    idempotency_key VARCHAR(255) NOT NULL,
    scheduled_at TIMESTAMPTZ,
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_sync_jobs_type CHECK (job_type IN ('FULL', 'INCREMENTAL')),
    CONSTRAINT ck_sync_jobs_status CHECK (status IN ('PENDING', 'QUEUED', 'RUNNING', 'COMPLETED', 'FAILED', 'RETRYING', 'CANCELLED')),
    CONSTRAINT ck_sync_jobs_completion CHECK (completed_at IS NULL OR started_at IS NOT NULL),
    CONSTRAINT fk_sync_jobs_connector_tenant FOREIGN KEY (connector_id, tenant_id) REFERENCES connectors (id, tenant_id)
);

CREATE UNIQUE INDEX uq_sync_jobs_tenant_idempotency ON sync_jobs (tenant_id, idempotency_key);
CREATE INDEX idx_sync_jobs_tenant_created_at ON sync_jobs (tenant_id, created_at DESC);
CREATE INDEX idx_sync_jobs_connector_status ON sync_jobs (connector_id, status);

CREATE TABLE sync_job_attempts (
    id UUID PRIMARY KEY,
    sync_job_id UUID NOT NULL REFERENCES sync_jobs(id),
    attempt_number INTEGER NOT NULL,
    status VARCHAR(32) NOT NULL,
    error_code VARCHAR(100),
    error_message VARCHAR(2048),
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_sync_job_attempts_job_attempt UNIQUE (sync_job_id, attempt_number),
    CONSTRAINT ck_sync_job_attempts_number CHECK (attempt_number > 0),
    CONSTRAINT ck_sync_job_attempts_status CHECK (status IN ('RUNNING', 'SUCCEEDED', 'FAILED', 'CANCELLED')),
    CONSTRAINT ck_sync_job_attempts_completion CHECK (completed_at IS NULL OR started_at IS NOT NULL)
);

CREATE TABLE sync_checkpoints (
    id UUID PRIMARY KEY,
    connector_id UUID NOT NULL REFERENCES connectors(id),
    checkpoint_key VARCHAR(255) NOT NULL,
    checkpoint_value TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_sync_checkpoints_connector_key UNIQUE (connector_id, checkpoint_key)
);

CREATE TABLE webhook_events (
    id UUID PRIMARY KEY,
    connector_id UUID NOT NULL REFERENCES connectors(id),
    provider_event_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    processing_status VARCHAR(32) NOT NULL,
    received_at TIMESTAMPTZ NOT NULL,
    processed_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_webhook_events_connector_provider_event UNIQUE (connector_id, provider_event_id),
    CONSTRAINT ck_webhook_events_status CHECK (processing_status IN ('RECEIVED', 'PROCESSING', 'PROCESSED', 'FAILED'))
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    actor_type VARCHAR(32) NOT NULL,
    actor_id VARCHAR(255),
    action VARCHAR(255) NOT NULL,
    resource_type VARCHAR(255) NOT NULL,
    resource_id UUID,
    metadata JSONB NOT NULL DEFAULT '{}'::jsonb,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_audit_logs_actor_type CHECK (actor_type IN ('USER', 'SERVICE', 'SYSTEM'))
);

CREATE INDEX idx_audit_logs_tenant_created_at ON audit_logs (tenant_id, created_at DESC);

CREATE TABLE idempotency_records (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    idempotency_key VARCHAR(255) NOT NULL,
    operation VARCHAR(255) NOT NULL,
    request_hash VARCHAR(128) NOT NULL,
    response_reference VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uq_idempotency_records_tenant_operation_key UNIQUE (tenant_id, operation, idempotency_key),
    CONSTRAINT ck_idempotency_records_expiry CHECK (expires_at > created_at)
);

CREATE INDEX idx_idempotency_records_expiry ON idempotency_records (expires_at);
