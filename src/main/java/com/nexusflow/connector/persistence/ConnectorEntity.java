package com.nexusflow.connector.persistence;

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
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "connectors")
public class ConnectorEntity {
	@Id
	@UuidGenerator
	private UUID id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tenant_id")
	private TenantEntity tenant;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Provider provider;
	@Column(name = "external_account_ref")
	private String externalAccountRef;
	@Column(name = "credential_reference")
	private String credentialReference;
	@Column(name = "display_name", nullable = false)
	private String displayName;
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
	protected ConnectorEntity() {
	}
	public ConnectorEntity(TenantEntity tenant, Provider provider, String externalAccountRef, String displayName,
			Status status) {
		this.tenant = tenant;
		this.provider = provider;
		this.externalAccountRef = externalAccountRef;
		this.displayName = displayName;
		this.status = status;
	}
	public UUID getId() {
		return id;
	}
	public TenantEntity getTenant() {
		return tenant;
	}
	public enum Provider {
		GOOGLE_DRIVE, MICROSOFT_365, GITHUB
	}
	public enum Status {
		PENDING_AUTH, ACTIVE, DISCONNECTED, ERROR
	}
}
