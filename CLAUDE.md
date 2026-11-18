# NexusFlow Working Agreement

Read [AGENTS.md](AGENTS.md) first. The rules in `.claude/rules` provide the detailed standards for each concern.

Before editing, inspect the related domain package and existing tests. Make the smallest cohesive change that satisfies the active phase. Run formatting and verification after implementation. Report commands actually run and their outcome.

Do not use source-code comments, hardcoded secrets, field injection, direct entity serialization, cross-tenant lookups, or unbounded retries. Keep external provider work behind interfaces so mock implementations can be replaced without changing orchestration logic.
