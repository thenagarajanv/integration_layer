# Architecture Rules

Preserve domain-first modules. A domain controller may call only application services, never repositories directly. Keep persistence details inside the owning domain. Shared code belongs in `common` only when it is genuinely domain-neutral and has at least two consumers.

Do not introduce microservices, event brokers, or real external integrations before their planned phase. Design integration seams as interfaces owned by the consuming domain.
