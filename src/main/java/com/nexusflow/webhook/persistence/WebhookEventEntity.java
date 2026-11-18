package com.nexusflow.webhook.persistence;

import com.nexusflow.connector.persistence.ConnectorEntity;
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
@Table(name = "webhook_events")
public class WebhookEventEntity {
	@Id
	@UuidGenerator
	private UUID id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "connector_id")
	private ConnectorEntity connector;
	@Column(name = "provider_event_id", nullable = false)
	private String providerEventId;
	@Column(name = "event_type", nullable = false)
	private String eventType;
	@Enumerated(EnumType.STRING)
	@Column(name = "processing_status", nullable = false)
	private Status status;
	@Column(name = "received_at", nullable = false)
	private Instant receivedAt;
	@Column(name = "processed_at")
	private Instant processedAt;
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	protected WebhookEventEntity() {
	}
	public enum Status {
		RECEIVED, PROCESSING, PROCESSED, FAILED
	}
}
