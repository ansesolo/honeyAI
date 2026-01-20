# HoneyAI

Family beekeeping business management application built with Spring Boot.

## Tech Stack

- **Runtime:** Java 17+
- **Framework:** Spring Boot 3.2.x
- **Database:** SQLite (embedded)
- **Template Engine:** Thymeleaf
- **ORM:** Spring Data JPA with Hibernate
- **Build Tool:** Maven

## Prerequisites

- JDK 17 or higher
- Maven 3.8+ (or use included Maven wrapper)

## How to Run

```bash
# Using Maven wrapper (recommended)
./mvnw spring-boot:run

# Or with installed Maven
mvn spring-boot:run
```

The application will start on [http://localhost:8080](http://localhost:8080).

## Project Structure

```
src/main/java/com/honeyai/
├── config/        # Configuration classes
├── controller/    # MVC Controllers
├── service/       # Business logic
├── repository/    # Data access layer
├── model/         # JPA entities
├── enums/         # Business enumerations
├── dto/           # Data transfer objects
└── exception/     # Custom exceptions
```

## Database

SQLite database is stored at `./data/honeyai.db` and created automatically on first run.

## Development

DevTools is enabled for hot reload during development. Changes to Java classes and templates will automatically reload.
