# Database Standards

Use PostgreSQL, Flyway, UUID primary keys, explicit foreign keys, and tenant-aware indexes. Every business record must identify its tenant. Use immutable migrations once applied. Model concurrency explicitly with version columns where a resource may be updated concurrently. Never construct cross-tenant queries without tenant ownership predicates.
