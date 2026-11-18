package com.nexusflow.connector.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectorRepository extends JpaRepository<ConnectorEntity, UUID> {
	Optional<ConnectorEntity> findByIdAndTenantId(UUID id, UUID tenantId);
	List<ConnectorEntity> findAllByTenantId(UUID tenantId);
}
