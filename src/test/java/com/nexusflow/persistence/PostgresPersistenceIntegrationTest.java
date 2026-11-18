package com.nexusflow.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.nexusflow.connector.persistence.ConnectorEntity;
import com.nexusflow.connector.persistence.ConnectorRepository;
import com.nexusflow.tenant.persistence.TenantEntity;
import com.nexusflow.tenant.persistence.TenantRepository;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class PostgresPersistenceIntegrationTest {
	@Container
	static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

	@DynamicPropertySource
	static void databaseProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
		registry.add("spring.datasource.username", POSTGRES::getUsername);
		registry.add("spring.datasource.password", POSTGRES::getPassword);
	}

	@Autowired
	private TenantRepository tenantRepository;
	@Autowired
	private ConnectorRepository connectorRepository;
	@Autowired
	private DataSource dataSource;

	@Test
	void migrationsCreateExpectedTables() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Integer tableCount = jdbcTemplate.queryForObject(
				"SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name IN ('tenants', 'connectors', 'sync_jobs', 'sync_job_attempts', 'sync_checkpoints', 'webhook_events', 'audit_logs', 'idempotency_records')",
				Integer.class);
		assertThat(tableCount).isEqualTo(8);
	}

	@Test
	void tenantScopedQueriesExcludeOtherTenantsAndConstraintsAreEnforced() {
		TenantEntity firstTenant = tenantRepository.save(new TenantEntity("First", TenantEntity.Status.ACTIVE));
		TenantEntity secondTenant = tenantRepository.save(new TenantEntity("Second", TenantEntity.Status.ACTIVE));
		ConnectorEntity connector = connectorRepository.save(new ConnectorEntity(firstTenant,
				ConnectorEntity.Provider.GITHUB, "account-1", "Primary", ConnectorEntity.Status.ACTIVE));

		assertThat(connectorRepository.findByIdAndTenantId(connector.getId(), firstTenant.getId())).isPresent();
		assertThat(connectorRepository.findByIdAndTenantId(connector.getId(), secondTenant.getId())).isEmpty();
		assertThatThrownBy(() -> connectorRepository.saveAndFlush(new ConnectorEntity(firstTenant,
				ConnectorEntity.Provider.GITHUB, "account-1", "Duplicate", ConnectorEntity.Status.ACTIVE)))
				.isInstanceOf(DataIntegrityViolationException.class);
		assertThat(UUID.randomUUID()).isNotEqualTo(connector.getId());
	}
}
