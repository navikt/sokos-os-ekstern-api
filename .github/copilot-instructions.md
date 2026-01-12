# Copilot Instructions

## General Guidelines

- **Write clean, readable code** - avoid adding comments unless absolutely necessary for complex logic
- **Do not create markdown files** after completing tasks
- Follow Kotlin and Ktor best practices
- Use functional programming patterns where appropriate

## Code Style

### Imports
- Group imports by package, separated by blank lines
- Order: standard library, third-party libraries, project imports
- Use explicit imports, avoid wildcard imports except for common DSLs

### Naming Conventions
- Use `camelCase` for variables and functions
- Use `PascalCase` for classes and data classes
- Use `SCREAMING_SNAKE_CASE` for constants
- Prefix private properties with underscore only when needed for clarity
- Use descriptive names that reflect business domain

### File Structure
- One public class per file
- File name matches the primary class name
- Package structure: `no.nav.[app].[feature].[layer]`
- Layers: `api`, `service`, `repository`, `domain`, `dto`, `config`

## Kotlin Conventions

### Data Classes
- Use `data class` for DTOs and domain models
- Add `@Serializable` annotation for serialization
- Prefer immutability (`val` over `var`)
- Use default parameters when appropriate

### Functions
- Keep functions focused and single-purpose
- Use expression syntax for simple functions
- Prefer extension functions for utility methods
- Use `suspend` functions for async operations

### Null Safety
- Use nullable types explicitly when needed
- Prefer safe call operator `?.` over null checks
- Use elvis operator `?:` for default values
- Avoid `!!` operator unless absolutely necessary

### Error Handling
- Use custom exception classes with meaningful error data
- Wrap exceptions with context-specific information
- Use `Result` type for operations that may fail

## Ktor Patterns

### Routing
- Define routes in separate API files per feature
- Use `route()` for grouping related endpoints
- Keep route handlers concise, delegate logic to services
- Use path parameters and query parameters appropriately

### Request/Response
- Use `call.receive<T>()` for request body deserialization
- Use `call.respond()` for responses
- Validate requests early in the handler
- Return consistent response structures

### Dependency Injection
- Use constructor injection with default parameters
- Create services with default implementations
- Allow injecting test doubles through constructor parameters

### Configuration
- Centralize configuration in dedicated config files
- Use extension functions on `Application` for config setup
- Group related configurations together

## Architecture Patterns

### Layered Architecture
- **API Layer**: Handle HTTP concerns, validation, routing
- **Service Layer**: Business logic, orchestration
- **Repository Layer**: Data access, database queries
- **Domain Layer**: Business entities, domain models

### Service Pattern
- Services contain business logic
- Services orchestrate repositories and external clients
- Use dependency injection for testability
- Keep services stateless when possible

### Repository Pattern
- Repositories handle data access
- Use coroutines for async database operations (`withContext(Dispatchers.IO)`)
- Return domain objects, not raw database results
- Use connection pooling (HikariCP)

## Database

### Query Style
- Use KotliQuery for database access
- Write SQL queries in raw strings with proper formatting
- Use parameter maps for SQL parameters
- Map results to domain objects using mapper functions

### Transactions
- Use `sessionOf()` for database sessions
- Wrap transactions in `using()` blocks
- Handle exceptions at appropriate levels

## Testing

- Write tests for business logic
- Use descriptive test names
- Mock external dependencies
- Test edge cases and error conditions

## Security

- Validate all user inputs
- Use authentication/authorization checks
- Audit log sensitive operations
- Never log sensitive data (passwords, tokens)

## Performance

- Use caching where appropriate
- Prefer immutable collections
- Use lazy evaluation when beneficial
- Profile before optimizing

## Dependencies

- Keep dependencies up to date
- Use stable versions for production
- Document why specific versions are used if pinned

## Project-Specific Conventions

### HTTP Clients
- Use Ktor HttpClient for external calls
- Configure clients in dedicated config files
- Add proper timeout and retry configuration
- Use correlation IDs for tracing

### Serialization
- Use kotlinx.serialization for JSON
- Annotate serializable classes with `@Serializable`
- Use custom serializers for complex types

### Logging
- Use structured logging with markers when needed
- Log at appropriate levels (info, warn, error)
- Include correlation IDs in logs
- Use KotlinLogging for logger instances

### Error Responses
- Use consistent error response structure
- Include timestamp, status, error message, and path
- Return appropriate HTTP status codes
- Provide actionable error messages

