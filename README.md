# DBZ Character API

This is a REST API for the Dragon Ball Z characters from my [Assignment 2 DBZ Gallery](https://github.com/CRoan3/CSC340-Assignment2-Static-Web-App-DBZ-Gallery). It supports full CRUD operations on characters, plus filtering by species and a partial name search. This is built with Spring Boot in a layered architecture, with data stored in PostgreSQL on Neon and deployed to Render with Docker.

## Live API

**Base/Example URL:** https://YOUR-SERVICE.onrender.com/api/characters

**My URL:** https://csc340-assignment-3-dbz-character.onrender.com/api/characters

> **Cold start:** the API runs on Render's free tier, which puts the service to sleep after about 15 minutes of inactivity. The first request after that can take up to a minute while it wakes up. Neon also scales idle databases to zero, which adds a short delay on the first query.

## Tech Stack

| Area       | Technology                              |
| ---------- | --------------------------------------- |
| Language   | Java 25                                 |
| Framework  | Spring Boot (Web, Data JPA, Validation) |
| Database   | PostgreSQL on Neon                      |
| Build      | Maven (wrapper)                         |
| Deployment | Docker on Render                        |

## Architecture

```
HTTP request
    |
DbzCharacterController  - maps URLs to methods, validates input, returns status codes
    |
DbzCharacterService     - business logic, combines filters, throws 404 for missing ids
    |
DbzCharacterRepository  - Spring Data JPA, generates the SQL
    |
DbzCharacter (entity)   - mapped to the `characters` table
    |
PostgreSQL (Neon)
```

```
src/main/java/com/csc340/dbz_api/
  DbzApiApplication.java
  characters/
    DbzCharacter.java
    DbzCharacterRepository.java
    DbzCharacterService.java
    DbzCharacterController.java
    DbzCharacterNotFoundException.java
```

## Data Model

**DbzCharacter** (table `characters`). The class is named `DbzCharacter` to avoid colliding with `java.lang.Character`.

| Attribute         | Type                     | Rules                                                 |
| ----------------- | ------------------------ | ----------------------------------------------------- |
| `characterId`     | Long                     | Unique, auto-generated                                |
| `name`            | String                   | Required (not null, not blank)                        |
| `description`     | String                   | Required (not null, not blank), up to 1000 characters |
| `species`         | String                   | Optional, e.g. `Saiyan`, `Human`, `Namekian`          |
| `role`            | String                   | Optional, e.g. `Martial Artist / Hero`                |
| `affiliation`     | String                   | Optional, e.g. `Z Fighters`, `Capsule Corporation`    |
| `imageUrl`        | String                   | Optional, link to the character's image               |
| `firstAppearance` | LocalDate (`yyyy-MM-dd`) | Optional, date the character first appeared           |

## Endpoints

Base path: `/api/characters`

| Method | Path                             | Description                                                      | Success |
| ------ | -------------------------------- | ---------------------------------------------------------------- | ------- |
| GET    | `/api/characters`                | Get all characters                                               | 200     |
| GET    | `/api/characters/{id}`           | Get one character by id                                          | 200     |
| POST   | `/api/characters`                | Add a character                                                  | 201     |
| PUT    | `/api/characters/{id}`           | Update a character (replaces all fields)                         | 200     |
| DELETE | `/api/characters/{id}`           | Delete a character                                               | 204     |
| GET    | `/api/characters?species=Saiyan` | Filter by species (case-insensitive exact match)                 | 200     |
| GET    | `/api/characters?name=veg`       | Search by name (partial, case-insensitive: `veg` finds `Vegeta`) | 200     |

`name` and `species` can be combined, for example `?name=k&species=Human`. Blank or missing parameters are ignored.

### Error responses

| Code | When                                                                                                  |
| ---- | ----------------------------------------------------------------------------------------------------- |
| 400  | POST or PUT with a missing or blank `name` or `description`, or a malformed body (such as a bad date) |
| 404  | GET, PUT, or DELETE with an id that does not exist                                                    |

### Example: POST `/api/characters`

```http
POST /api/characters
Content-Type: application/json

{
  "name": "Goku",
  "description": "Goku is a Saiyan warrior raised on Earth. He trains relentlessly to become stronger and protect his family, friends, and planet Earth.",
  "species": "Saiyan",
  "role": "Martial Artist / Hero",
  "affiliation": "Z Fighters",
  "imageUrl": "images/goku.jpg",
  "firstAppearance": "1986-02-26"
}
```

Response `201 Created`:

```json
{
  "characterId": 1,
  "name": "Goku",
  "description": "Goku is a Saiyan warrior raised on Earth. He trains relentlessly to become stronger and protect his family, friends, and planet Earth.",
  "species": "Saiyan",
  "role": "Martial Artist / Hero",
  "affiliation": "Z Fighters",
  "imageUrl": "images/goku.jpg",
  "firstAppearance": "1986-02-26"
}
```

### Example: PUT `/api/characters/3`

PUT replaces every field, so it sends the full object.

```http
PUT /api/characters/3
Content-Type: application/json

{
  "name": "Krillin",
  "description": "Krillin is one of Goku's oldest friends and the strongest human in the Z Fighters.",
  "species": "Human",
  "role": "Martial Artist",
  "affiliation": "Z Fighters",
  "imageUrl": "images/krillin.jpg"
}
```

Response: `200 OK` with the updated character.

## Configuration

The database connection comes from an environment variable, so no secrets are stored in the repository.

| Variable                | Purpose                                                               |
| ----------------------- | --------------------------------------------------------------------- |
| `SPRING_DATASOURCE_URL` | Neon JDBC connection string (`jdbc:postgresql://...?sslmode=require`) |
| `PORT`                  | Set by Render; defaults to `8080` locally                             |

Hibernate creates the `characters` table automatically on first run (`spring.jpa.hibernate.ddl-auto=update`).

## Running Locally

Running this locally requires JDK 25.

1. Set the connection string in the same terminal you will run the app from:
   - PowerShell: `$env:SPRING_DATASOURCE_URL="jdbc:postgresql://..."`
   - Windows cmd: `set SPRING_DATASOURCE_URL=jdbc:postgresql://...`
   - Mac/Linux: `export SPRING_DATASOURCE_URL="jdbc:postgresql://..."`
2. Start the app:
   - Windows: `.\mvnw.cmd spring-boot:run`
   - Mac/Linux: `./mvnw spring-boot:run`
3. Open `http://localhost:8080/api/characters`. A fresh database returns `[]`. There should be nothing in the fresh database.

## Deployment (Render)

The `Dockerfile` uses a multi-stage build: a JDK 25 image builds the JAR with the Maven wrapper, and a smaller JRE 25 image runs it with `java -jar`. It contains no port or secrets.

1. Create a Render **Web Service** connected to this repository.
2. Choose the **Docker** runtime and the **Free** instance type.
3. Add `SPRING_DATASOURCE_URL` under **Environment** (value only, no quotes).
4. Deploy and open `https://YOUR-SERVICE.onrender.com/api/characters`.
