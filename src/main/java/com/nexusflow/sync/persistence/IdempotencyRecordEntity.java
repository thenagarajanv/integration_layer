package com.nexusflow.sync.persistence;

import com.nexusflow.tenant.persistence.TenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "idempotency_records")
public class IdempotencyRecordEntity {
	@Id
	@UuidGenerator
	private UUID id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tenant_id")
	private TenantEntity tenant;
	@Column(name = "idempotency_key", nullable = false)
	private String idempotencyKey;
	@Column(nullable = false)
	private String operation;
	@Column(name = "request_hash", nullable = false)
	private String requestHash;
	@Column(name = "response_reference")
	private String responseReference;
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	@Column(name = "expires_at", nullable = false)
	private Instant expiresAt;
	protected IdempotencyRecordEntity() {
	}
}
