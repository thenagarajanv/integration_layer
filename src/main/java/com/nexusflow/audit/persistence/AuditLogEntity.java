package com.nexusflow.audit.persistence;

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
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "audit_logs")
public class AuditLogEntity {
	@Id
	@UuidGenerator
	private UUID id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tenant_id")
	private TenantEntity tenant;
	@Enumerated(EnumType.STRING)
	@Column(name = "actor_type", nullable = false)
	private ActorType actorType;
	@Column(name = "actor_id")
	private String actorId;
	@Column(nullable = false)
	private String action;
	@Column(name = "resource_type", nullable = false)
	private String resourceType;
	@Column(name = "resource_id")
	private UUID resourceId;
	@Column(columnDefinition = "jsonb", nullable = false)
	private String metadata = "{}";
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	protected AuditLogEntity() {
	}
	public enum ActorType {
		USER, SERVICE, SYSTEM
	}
}
