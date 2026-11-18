# Security Standards

Treat every external request and webhook as untrusted. Do not hardcode or log secrets. Load sensitive values from environment variables or an approved secret store. Verify tenant ownership before returning or mutating a resource. Keep authorization decisions in the service boundary as well as endpoint policy.
