package com.nexusflow.sync.persistence;

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
@Table(name = "sync_job_attempts")
public class SyncJobAttemptEntity {
	@Id
	@UuidGenerator
	private UUID id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sync_job_id")
	private SyncJobEntity syncJob;
	@Column(name = "attempt_number", nullable = false)
	private int attemptNumber;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	@Column(name = "error_code")
	private String errorCode;
	@Column(name = "error_message")
	private String errorMessage;
	@Column(name = "started_at")
	private Instant startedAt;
	@Column(name = "completed_at")
	private Instant completedAt;
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	protected SyncJobAttemptEntity() {
	}
	public enum Status {
		RUNNING, SUCCEEDED, FAILED, CANCELLED
	}
}
