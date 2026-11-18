package com.nexusflow.tenant.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tenants")
public class TenantEntity {
	@Id
	@UuidGenerator
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	@Version
	private long version;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	protected TenantEntity() {
	}

	public TenantEntity(String name, Status status) {
		this.name = name;
		this.status = status;
	}

	public UUID getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public Status getStatus() {
		return status;
	}
	public enum Status {
		ACTIVE, SUSPENDED, DEACTIVATED
	}
}
