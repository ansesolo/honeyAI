# HoneyAI Source Tree Structure

**Version:** 1.0
**Date:** 2026-01-18
**Source:** Extracted from Architecture Document v1.0
**Status:** Complete - Project Structure Reference

---

## Overview

This document defines the complete source tree structure for HoneyAI. All developers must follow this structure when creating new files or organizing code.

---

## Complete Project Structure

```
honeyAI/
├── src/
│   ├── main/
│   │   ├── java/com/honeyai/
│   │   │   ├── HoneyAiApplication.java          # Main Spring Boot entry point
│   │   │   ├── config/                           # Configuration classes (@ConfigurationProperties)
│   │   │   │   └── EtiquetteConfig.java         # Label generation settings
│   │   │   ├── controller/                       # MVC Controllers (@Controller)
│   │   │   │   ├── ClientController.java
│   │   │   │   ├── CommandeController.java
│   │   │   │   ├── ProduitController.java
│   │   │   │   ├── EtiquetteController.java
│   │   │   │   ├── AchatController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   └── BackupController.java
│   │   │   ├── service/                          # Business logic layer (@Service)
│   │   │   │   ├── ClientService.java
│   │   │   │   ├── CommandeService.java
│   │   │   │   ├── ProduitService.java
│   │   │   │   ├── EtiquetteService.java
│   │   │   │   ├── AchatService.java
│   │   │   │   ├── DashboardService.java
│   │   │   │   ├── PdfService.java
│   │   │   │   └── BackupService.java
│   │   │   ├── repository/                       # Spring Data JPA repositories
│   │   │   │   ├── ClientRepository.java
│   │   │   │   ├── CommandeRepository.java
│   │   │   │   ├── LigneCommandeRepository.java
│   │   │   │   ├── ProduitRepository.java
│   │   │   │   ├── TarifRepository.java
│   │   │   │   ├── AchatRepository.java
│   │   │   │   ├── LotsEtiquettesRepository.java
│   │   │   │   └── HistoriqueEtiquettesRepository.java
│   │   │   ├── model/                            # JPA entities (@Entity)
│   │   │   │   ├── Client.java
│   │   │   │   ├── Commande.java
│   │   │   │   ├── LigneCommande.java
│   │   │   │   ├── Produit.java
│   │   │   │   ├── Tarif.java
│   │   │   │   ├── Achat.java
│   │   │   │   ├── LotsEtiquettes.java
│   │   │   │   └── HistoriqueEtiquettes.java
│   │   │   ├── enums/                            # Business enumerations
│   │   │   │   ├── TypeMiel.java                 # TOUTES_FLEURS, FORET, CHATAIGNIER
│   │   │   │   ├── FormatPot.java                # POT_500G, POT_1KG
│   │   │   │   ├── StatutCommande.java           # COMMANDEE, RECUPEREE, PAYEE
│   │   │   │   └── CategorieAchat.java           # CIRE, POTS, COUVERCLES, etc.
│   │   │   ├── dto/                              # Data Transfer Objects
│   │   │   │   ├── EtiquetteRequest.java
│   │   │   │   ├── EtiquetteData.java
│   │   │   │   └── TopProduitDto.java
│   │   │   └── exception/                        # Custom exceptions
│   │   │       ├── GlobalExceptionHandler.java   # @ControllerAdvice
│   │   │       ├── ClientNotFoundException.java
│   │   │       ├── InvalidStatusTransitionException.java
│   │   │       ├── PriceNotFoundException.java
│   │   │       └── PdfGenerationException.java
│   │   └── resources/
│   │       ├── application.yml                   # Main configuration
│   │       ├── application-dev.yml              # Development profile
│   │       ├── application-prod.yml             # Production profile
│   │       ├── static/                          # CSS, JS, images
│   │       │   ├── css/
│   │       │   │   └── custom.css               # Custom styles
│   │       │   ├── js/
│   │       │   │   └── forms.js                 # Dynamic form behaviors
│   │       │   └── images/
│   │       │       └── logo.png
│   │       └── templates/                       # Thymeleaf HTML templates
│   │           ├── fragments/
│   │           │   └── layout.html              # Base layout with navigation
│   │           ├── clients/
│   │           │   ├── list.html
│   │           │   ├── detail.html
│   │           │   └── form.html
│   │           ├── commandes/
│   │           │   ├── list.html
│   │           │   ├── detail.html
│   │           │   └── form.html
│   │           ├── produits/
│   │           │   └── list.html
│   │           ├── etiquettes/
│   │           │   ├── form.html
│   │           │   └── historique.html
│   │           ├── achats/
│   │           │   └── list.html
│   │           ├── dashboard.html
│   │           ├── backup/
│   │           │   └── manage.html
│   │           └── error/
│   │               ├── 404.html
│   │               ├── 500.html
│   │               └── error.html
│   └── test/
│       └── java/com/honeyai/                    # Unit & integration tests
│           ├── HoneyAiApplicationTests.java
│           ├── service/
│           │   ├── ClientServiceTest.java
│           │   ├── CommandeServiceTest.java
│           │   ├── EtiquetteServiceTest.java
│           │   └── DashboardServiceTest.java
│           └── repository/
│               ├── ClientRepositoryTest.java
│               └── CommandeRepositoryTest.java
├── data/                                         # Runtime: SQLite database
│   └── honeyai.db                                # Created automatically
├── backups/                                      # Runtime: Daily backups
│   └── honeyai-backup-YYYY-MM-DD-HHmmss.db
├── logs/                                         # Runtime: Application logs
│   └── honeyai.log
├── docs/                                         # Documentation
│   ├── prd.md
│   ├── architecture.md
│   ├── architecture/
│   │   ├── tech-stack.md
│   │   ├── source-tree.md
│   │   └── coding-standards.md
│   ├── GUIDE-UTILISATEUR.md
│   └── DEPLOYMENT-CHECKLIST.md
├── launcher/                                     # Packaging resources
│   ├── icon.ico                                  # Application icon
│   ├── splash.bmp                                # Startup splash screen
│   └── honeyai-launch4j.xml                     # launch4j configuration
├── .bmad-core/                                   # BMAD framework (not part of app)
├── .git/                                         # Git repository
├── .gitignore
├── pom.xml                                       # Maven dependencies
└── README.md                                     # Project documentation
```

---

## Package Organization

### Java Package Structure

**Base Package:** `com.honeyai`

```
com.honeyai/
├── HoneyAiApplication.java           # @SpringBootApplication entry point
├── config/                            # Configuration classes
├── controller/                        # Web layer (HTTP endpoints)
├── service/                           # Business logic layer
├── repository/                        # Data access layer
├── model/                             # Domain entities
├── enums/                             # Business enumerations
├── dto/                               # Data transfer objects
└── exception/                         # Custom exceptions + global handler
```

### Resources Organization

```
resources/
├── application.yml                    # Main config
├── application-{profile}.yml         # Profile-specific config
├── static/                           # Static web assets
│   ├── css/                          # Stylesheets
│   ├── js/                           # JavaScript
│   └── images/                       # Images, icons
└── templates/                        # Thymeleaf templates
    ├── fragments/                    # Reusable fragments
    ├── {feature}/                    # Feature-specific views
    └── error/                        # Error pages
```

---

## Directory Purposes

### `/src/main/java/com/honeyai/`
Java source code following Spring Boot conventions.

### `/src/main/resources/`
Application configuration, static assets, and Thymeleaf templates.

### `/src/test/java/com/honeyai/`
Unit and integration tests mirroring main package structure.

### `/data/`
**Runtime directory** - Created automatically. Contains SQLite database file.

### `/backups/`
**Runtime directory** - Created by BackupService. Contains automated daily database backups.

### `/logs/`
**Runtime directory** - Created by Logback. Contains application logs with rolling policy.

### `/docs/`
Project documentation including PRD, architecture, and user guides.

### `/launcher/`
Windows packaging resources (icon, splash screen, launch4j config).

---

## File Naming Conventions

### Java Classes
- **Entities:** Singular noun, e.g., `Client.java`, `Commande.java`
- **Repositories:** `{Entity}Repository.java`, e.g., `ClientRepository.java`
- **Services:** `{Entity}Service.java`, e.g., `ClientService.java`
- **Controllers:** `{Entity}Controller.java`, e.g., `ClientController.java`
- **DTOs:** Purpose + `Dto.java`, e.g., `EtiquetteRequest.java`, `TopProduitDto.java`
- **Exceptions:** Descriptive + `Exception.java`, e.g., `ClientNotFoundException.java`

### Templates (Thymeleaf)
- **List views:** `list.html`
- **Detail views:** `detail.html`
- **Form views:** `form.html`
- **Feature prefix:** Use subdirectory, e.g., `clients/list.html`

### Static Assets
- **CSS:** `kebab-case.css`, e.g., `custom.css`
- **JavaScript:** `kebab-case.js`, e.g., `forms.js`
- **Images:** Descriptive name, e.g., `logo.png`

---

## Package Layering Rules

### Controller Layer
- **Depends on:** Service layer only
- **Never depends on:** Repository layer directly
- **Responsibilities:** HTTP routing, validation, model preparation

### Service Layer
- **Depends on:** Repository layer, other services
- **Never depends on:** Controller layer
- **Responsibilities:** Business logic, orchestration, transactions

### Repository Layer
- **Depends on:** Model layer only
- **Never depends on:** Service or Controller layers
- **Responsibilities:** Data access, queries

### Model Layer
- **Depends on:** Nothing (except JPA annotations)
- **Responsibilities:** Domain entities, relationships

---

## Special Directories

### Runtime Directories
These directories are created automatically at runtime and should NOT be committed to Git:
- `/data/` - SQLite database
- `/backups/` - Database backups
- `/logs/` - Application logs

**.gitignore entries:**
```
/data/
/backups/
/logs/
/target/
*.db
*.log
```

### Build Output
- `/target/` - Maven build output (JAR files, compiled classes)

---

## Template Organization Pattern

Each major feature follows this template structure:

```
templates/{feature}/
├── list.html       # List all entities
├── detail.html     # View single entity
└── form.html       # Create/edit entity
```

**Example:** Client feature
```
templates/clients/
├── list.html       # /clients - List all clients
├── detail.html     # /clients/{id} - View client details
└── form.html       # /clients/nouveau or /clients/{id}/edit
```

---

## Test Organization

Tests mirror the main package structure:

```
src/test/java/com/honeyai/
├── service/
│   └── ClientServiceTest.java      # Tests ClientService
├── repository/
│   └── ClientRepositoryTest.java   # Tests ClientRepository
└── controller/
    └── ClientControllerTest.java   # Tests ClientController (optional)
```

**Naming Convention:** `{ClassUnderTest}Test.java`

---

## Configuration Files Location

| File | Location | Purpose |
|------|----------|---------|
| `application.yml` | `src/main/resources/` | Main configuration |
| `application-dev.yml` | `src/main/resources/` | Development overrides |
| `application-prod.yml` | `src/main/resources/` | Production overrides |
| `pom.xml` | Project root | Maven dependencies |
| `logback-spring.xml` | `src/main/resources/` | Custom logging (optional) |

---

## Deployment Artifacts Location

| Artifact | Location | Generated By |
|----------|----------|--------------|
| `honeyai-{version}.jar` | `/target/` | Maven build |
| `HoneyAI.exe` | `/target/` or `/dist/` | launch4j |
| Distribution ZIP | `/dist/` | Manual packaging |

---

## Key Files Reference

### Entry Point
- **Main Class:** `com.honeyai.HoneyAiApplication`
- **Location:** `src/main/java/com/honeyai/HoneyAiApplication.java`

### Configuration
- **Database:** `application.yml` → `spring.datasource.url`
- **Server Port:** `application.yml` → `server.port: 8080`
- **Label Config:** `application.yml` → `honeyai.etiquettes`

### Base Layout
- **Template:** `src/main/resources/templates/fragments/layout.html`
- **Used by:** All page templates via Thymeleaf layout dialect

---

## Growth Guidelines

### Adding a New Feature
1. Create entity in `/model/`
2. Create repository in `/repository/`
3. Create service in `/service/`
4. Create controller in `/controller/`
5. Create templates in `/templates/{feature}/`
6. Add navigation link in `fragments/layout.html`

### Adding a New Template
- Location: `templates/{feature}/{purpose}.html`
- Extend base layout: `fragments/layout.html`
- Follow naming: `list.html`, `detail.html`, `form.html`

### Adding a New Configuration
- Simple values: Add to `application.yml`
- Complex config: Create `@ConfigurationProperties` class in `/config/`

---

## References

- Spring Boot Project Structure: https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.structuring-your-code
- Maven Standard Directory Layout: https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html
- Thymeleaf Layout Dialect: https://ultraq.github.io/thymeleaf-layout-dialect/

---

**Document Owner:** Architect Agent (Winston)
**Last Updated:** 2026-01-18
**Change Control:** Structural changes require architecture review
