# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# 1. Start PostgreSQL (Docker)
cd docker && docker compose up -d

# 2. Run the app
mvn spring-boot:run
```

Application: `http://localhost:8080` | Swagger UI: `http://localhost:8080/swagger-ui.html`

## Test Commands

```bash
mvn test                              # Run all tests
mvn test -Dtest=SomeTest              # Run single test class
mvn test -Dtest=SomeTest#methodName   # Run single test method
```

## Tech Stack

Java 21, Spring Boot 3.3, Spring Security + JWT (access + refresh tokens), Spring Data JPA, PostgreSQL, Liquibase, springdoc-openapi, Lombok.

## Architecture

```
controller   - REST endpoints (@RestController)
service      - business logic (@Service, @Transactional)
repository   - Spring Data JPA (findBy*, save, delete)
mapper       - entity ↔ DTO conversion
entity       - JPA entities (@Entity, @OneToMany, @ManyToOne)
dto/request  - input (validated with jakarta.validation)
dto/response - output (never return entities directly)
exception    - custom unchecked exceptions
security     - JWT provider, auth filter, SecurityConfig
```

## Entity Relationships

```
Festival 1:N Food
Festival 1:N Drink
User     N:M Festival (via Favorite)
User     1:N RefreshToken
```

## Known Bug: Lazy Loading in FestivalService

`FestivalService.getById()` lacks `@Transactional`. `FestivalMapper.toDetailResponse()` accesses `festival.getFoods()` and `festival.getDrinks()` (LAZY collections). With `spring.jpa.open-in-view: false`, this causes `LazyInitializationException`.

- **Location:** `service/FestivalService.java:35` — method `getById(Long id)`
- **Fix:** Add `@Transactional(readOnly = true)` or use a JPQL `fetch join` in the repository.

## Project State

### Completed
- Auth: register, login, refresh, logout (JWT + httpOnly cookie)
- Festival CRUD (read public, write ADMIN only)
- Food CRUD (read public, write ADMIN only) — **reference implementation**
- User profile: `GET /users/me`

### Student Exercises (TODO)
- **Drink CRUD** — implement `DrinkMapper`, complete `DrinkService` (throws `UnsupportedOperationException`), implement `DrinkController`. Reference: `FoodService.java`, `FoodController.java`, `FoodMapper.java`
- **Favorites** — complete `FavoriteService`, implement `FavoriteController`
- **Security rules** — add role-based rules for Drink and Favorites in `SecurityConfig.java`

### Test Users
| username | password | role       |
|----------|----------|------------|
| admin    | admin123 | ROLE_ADMIN |
| marko    | user123  | ROLE_USER  |

### Token Config
Access token lifetime: **1 minute** (intentional for testing refresh flow). `jwt.expiration-ms: 60000` in `application.yaml`.