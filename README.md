# Zagreb Open Festivals - Backend

Spring Boot backend za aplikaciju koja prikazuje festivale na otvorenom u Zagrebu.

## Tehnologije

- Java 21, Spring Boot 3.3
- Spring Security + JWT (access + refresh token)
- Spring Data JPA + PostgreSQL
- Liquibase (upravljanje shemom baze)
- springdoc-openapi (Swagger UI)

## Pokretanje

### 1. Pokreni bazu (PostgreSQL kroz Docker)

```bash
cd docker
docker compose up -d
```

Baza će biti dostupna na `localhost:5432`, s korisnikom `zof_user` / `zof_password` i
bazom `zagreb_open_festivals` (vidi `docker/docker-compose.yml`).

### 2. Pokreni aplikaciju

```bash
mvn spring-boot:run
```

(Ako preferirate Maven wrapper, pokrenite `mvn -N io.takari:maven:wrapper` jednom da
generirate `mvnw`/`mvnw.cmd`, ili jednostavno importajte projekt u IntelliJ i pokrenite
`ZagrebOpenFestivalsApplication` iz IDE-a.)

Liquibase će automatski kreirati shemu i ubaciti test podatke pri prvom pokretanju.

Aplikacija radi na `http://localhost:8080`.

### 3. Swagger UI

`http://localhost:8080/swagger-ui.html`

## Test korisnici (već u bazi)

| username | password  | rola        |
|----------|-----------|-------------|
| admin    | admin123  | ROLE_ADMIN  |
| marko    | user123   | ROLE_USER   |

## Autentifikacija

1. `POST /auth/register` - registracija (uvijek kao ROLE_USER)
2. `POST /auth/login` - vraća `accessToken` u body-ju, `refreshToken` postavlja kao
   `httpOnly` cookie
3. Svaki daljnji zahtjev šalje `Authorization: Bearer <accessToken>` header
4. Access token vrijedi **1 minutu** (namjerno kratko, da se refresh flow odmah vidi)
5. `POST /auth/refresh` - koristi refresh cookie da izda novi access + refresh token
6. `POST /auth/logout` - poništava refresh token

## Struktura paketa

```
controller   - REST endpointi
dto/request  - ulazni podaci (nikad se ne vraća entitet direktno)
dto/response - izlazni podaci
entity       - JPA entiteti
exception    - custom iznimke
mapper       - entitet <-> DTO
repository   - Spring Data JPA repozitoriji
security     - JWT, Spring Security konfiguracija
service      - poslovna logika
```

## Entiteti i relacije

```
Festival 1:N Food
Festival 1:N Drink
User     N:M Festival   (preko Favorite entiteta)
User     1:N RefreshToken
```

## Što je gotovo

- Registracija, login, refresh, logout (JWT access + refresh)
- Festival CRUD (čitanje javno, pisanje samo ADMIN)
- `GET /festivals?search=...` - jednostavni search po imenu festivala
- Food CRUD (čitanje javno, pisanje samo ADMIN) - **referentni primjer**
- Korisnički profil (`GET /users/me`)

## Vaš zadatak (feature branch po funkcionalnosti!)

1. **Drink CRUD** - po uzoru na `FoodController` / `FoodService` (vidi TODO komentare
   u `DrinkController.java` i `DrinkService.java`)
2. **Favorites** - dodavanje/micanje festivala iz omiljenih (vidi TODO komentare u
   `FavoriteController.java` i `FavoriteService.java`)
3. **Role-based zaštita** - u `SecurityConfig.java` nedostaju pravila pristupa za
   Drink i Favorites endpointe (vidi TODO komentare unutar `securityFilterChain`)

Predloženi git workflow: `feature/drink-crud`, `feature/favorites`,
`feature/security-rules` - svaki kroz commit → push → Pull Request → merge u `develop`.

## Napomena

Negdje u kodu postoji namjerna, realna greška vezana uz Hibernate lazy loading i
transakcije - dio je vježbe da je pronađete i popravite. 🙂
