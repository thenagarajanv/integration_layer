# Development Setup

Install a Java 21 JDK and use the Maven Wrapper included with this repository. Java 26 can run Maven but the build compiles with the Java 21 release target.

```powershell
.\mvnw.cmd spotless:apply
.\mvnw.cmd verify
.\mvnw.cmd spring-boot:run
```

Set environment variables only when overrides are required. The default port is 8080. Use `.env.example` as the list of supported local variables and keep local `.env` files untracked.

The CI workflow executes the same formatting check and verification lifecycle used locally.
