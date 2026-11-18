package com.nexusflow.tenant;

import java.util.Optional;
import java.util.UUID;

public interface TenantContext {
	Optional<UUID> currentTenantId();
}
