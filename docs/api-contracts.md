# API Contracts

All public APIs will use the `/api/v1` prefix and JSON request and response bodies. Versioned DTOs, validation errors, pagination, and correlation identifiers will be defined before the first business endpoint is implemented.

Phase 1 exposes `GET /api/v1/health` for a lightweight service availability response. Platform probes use `GET /actuator/health`.
