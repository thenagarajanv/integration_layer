# NexusFlow Agent Guide

## Scope

NexusFlow is an independently deployable Java service. Keep every change scoped to the active phase and do not introduce unrequested provider integrations, credentials, queues, or persistence technologies.

## Architecture rules

- Organize code by domain: `tenant`, `connector`, `sync`, `webhook`, and `audit`.
- Keep controllers thin. Put business use cases and transaction boundaries in services.
- Use constructor injection only.
- Use request and response DTOs at API boundaries. Never expose JPA entities.
- Enforce tenant ownership in every resource lookup and query.
- Prefer explicit domain exceptions and central error translation.

## Security rules

- Never add secrets, tokens, private keys, or real provider credentials to the repository.
- Read sensitive configuration from environment variables.
- Do not log credentials, access tokens, webhook bodies, or personally sensitive content.
- Treat webhook payloads as untrusted until signature validation is complete.

## Quality rules

- Target Java 21 and Spring Boot 3.x.
- Do not add comments to source files.
- Keep methods focused and name behavior clearly.
- Add or update tests for business behavior.
- Run `./mvnw.cmd spotless:apply` and `./mvnw.cmd verify` before handoff.
- Do not edit generated build output or commit `.env` files.

