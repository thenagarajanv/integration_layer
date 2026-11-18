# Testing Standards

Use JUnit 5 and Mockito for unit tests. Add integration tests for persistence and web boundaries as those layers are introduced. Use Testcontainers for PostgreSQL integration tests rather than embedded database substitutions. Test tenant isolation, idempotency, retries, and failure paths as first-class behavior.
