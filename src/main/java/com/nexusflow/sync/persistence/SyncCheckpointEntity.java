package com.nexusflow.sync.persistence;

import com.nexusflow.connector.persistence.ConnectorEntity;
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
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "sync_checkpoints")
public class SyncCheckpointEntity {
	@Id
	@UuidGenerator
	private UUID id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "connector_id")
	private ConnectorEntity connector;
	@Column(name = "checkpoint_key", nullable = false)
	private String checkpointKey;
	@Column(name = "checkpoint_value", nullable = false)
	private String checkpointValue;
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	protected SyncCheckpointEntity() {
	}
}
