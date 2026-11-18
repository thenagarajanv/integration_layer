package com.nexusflow;

import com.nexusflow.monitoring.HealthController;
import org.junit.jupiter.api.Test;

class NexusFlowApplicationTest {
	@Test
	void healthControllerIsConstructible() {
		new HealthController();
	}
}
