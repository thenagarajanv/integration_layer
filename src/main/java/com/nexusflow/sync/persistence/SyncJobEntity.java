package com.nexusflow.sync.persistence;

import com.nexusflow.connector.persistence.ConnectorEntity;
import com.nexusflow.tenant.persistence.TenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "sync_jobs")
public class SyncJobEntity {
	@Id
	@UuidGenerator
	private UUID id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tenant_id")
	private TenantEntity tenant;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "connector_id")
	private ConnectorEntity connector;
	@Enumerated(EnumType.STRING)
	@Column(name = "job_type", nullable = false)
	private Type type;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	@Column(name = "idempotency_key", nullable = false)
	private String idempotencyKey;
	@Column(name = "scheduled_at")
	private Instant scheduledAt;
	@Column(name = "started_at")
	private Instant startedAt;
	@Column(name = "completed_at")
	private Instant completedAt;
	@Version
	private long version;
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	protected SyncJobEntity() {
	}
	public enum Type {
		FULL, INCREMENTAL
	}
	public enum Status {
		PENDING, QUEUED, RUNNING, COMPLETED, FAILED, RETRYING, CANCELLED
	}
}
