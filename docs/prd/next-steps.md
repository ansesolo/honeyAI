# Next Steps

## UX Expert Prompt

To initiate the UX/design phase, please use the following prompt with the UX Expert agent:

```
I need you to create comprehensive UX documentation for HoneyAI, a beekeeping management application
for non-technical users (parents aged 50-65). The PRD is in docs/prd.md and Project Brief in docs/brief.md.

KEY CONTEXT:
- Target users: Beekeepers with basic computer skills, afraid of "breaking" things
- Current system: Paper notebook → Digital replacement must be SIMPLER, not more complex
- Killer feature: PDF label generation (saves 13 minutes per 10 labels)
- Critical success factor: Zero learning curve - must be intuitive from first use

DELIVERABLES NEEDED:

1. USER FLOW DIAGRAMS (Priority: Critical)
   - Primary workflow: Create client → Create order → Generate labels → View dashboard
   - Order status lifecycle: Commandée → Récupérée → Payée (with UI state changes)
   - Navigation map: Sidebar menu → 10 core screens relationships
   - Error recovery flows: What happens when user makes mistake?

2. WIREFRAMES (Priority: High)
   - 10 core screens from PRD Section "Core Screens and Views"
   - Focus on: Dashboard, Client list/detail, Order create/detail, Label generation form
   - Show form validation states, empty states, loading states
   - Desktop-first (1280x720 minimum), responsive considerations noted

3. COMPONENT SPECIFICATIONS (Priority: High)
   - Bootstrap 5 components to use for each pattern
   - Custom CSS needed for accessibility (16px min font, 44x44px buttons)
   - Color palette application: Amber #F4B942 (primary actions), Green #2D5016 (navigation)
   - Icon library: Font Awesome 6.4 - which icons for which actions

4. INTERACTION PATTERNS (Priority: Medium)
   - Form behavior: Validation timing, error display, success confirmations
   - Dynamic product lines in order form (add/remove with JavaScript)
   - Status transition buttons (show/hide based on current status)
   - Confirmation modals for destructive actions (delete client, delete purchase)

5. ACCESSIBILITY CHECKLIST (Priority: High)
   - WCAG AA compliance verification for all screens
   - Keyboard navigation flows (Tab order, Enter to submit, Esc to cancel)
   - Screen reader considerations (aria labels for icons, alt text)
   - Color contrast verification (4.5:1 minimum)

CONSTRAINTS:
- Use only Bootstrap 5.3 + minimal custom CSS (developer wants simplicity)
- No JavaScript frameworks (React/Vue) - vanilla JS only for dynamic forms
- Server-side rendering with Thymeleaf (not SPA)
- French language throughout (labels, messages, help text)

TONE & STYLE:
- Reassuring, warm, never technical
- Large text, generous spacing, no clutter
- Confirmation messages use encouraging language ("Bien enregistré ✓", "C'est fait!")
- Error messages explain problem AND solution clearly

OUTPUT FORMAT:
Create docs/ux-specifications.md with sections for each deliverable above, including
embedded diagrams (Mermaid), annotated wireframe descriptions, and component examples.
```

## Architect Prompt

To initiate the technical architecture phase, please use the following prompt with the Architect agent:

```
I need you to create a comprehensive technical architecture document for HoneyAI, a Spring Boot
desktop application for beekeeping management. Review docs/prd.md and docs/brief.md for full context.

PROJECT CONTEXT:
- Developer: Expert Spring Boot developer (uses it professionally daily)
- Users: Non-technical parents (50-65 years), single PC, Windows 10+
- Architecture: Monolith web app, localhost:8080, offline-first, SQLite database
- Distribution: .exe wrapper (launch4j) with embedded JRE, no internet required
- Timeline: 68-91 hours development (5 epics, 40 stories with detailed AC in PRD)

STACK DECISIONS (ALREADY VALIDATED):
- Spring Boot 3.2+, Java 17 LTS
- Spring Data JPA + Hibernate + SQLite (xerial driver + community dialect)
- Thymeleaf server-side rendering + Bootstrap 5.3 + Vanilla JavaScript
- Apache PDFBox 3.0 for label generation
- Maven build, launch4j packaging
- JUnit 5 + Spring Boot Test (unit + integration, NO E2E)

DELIVERABLES NEEDED:

1. SYSTEM ARCHITECTURE DOCUMENT (docs/architecture.md)

   A. Package Structure & Layering
      - Complete com.honeyai package breakdown (config, controller, service, repository, model, enums, exception)
      - MVC pattern: Controller → Service → Repository → Database flow
      - Cross-cutting concerns: GlobalExceptionHandler, BackupService (@Scheduled), logging strategy
      - Resource organization: static/ (CSS, JS), templates/ (Thymeleaf HTML)

   B. Entity Relationship Model
      - 6 core entities: Client, Commande, LigneCommande, Produit, Tarif, Achat
      - Plus: LotsEtiquettes (lot number tracking), HistoriqueEtiquettes (label history)
      - JPA relationships: @OneToMany, @ManyToOne, cascade rules, orphanRemoval
      - Soft delete strategy: deletedAt timestamp on Client (preserve data integrity)
      - Indexes needed: clients.nom, commandes.date_commande, commandes.statut

   C. Service Layer Design
      - ClientService: CRUD + soft delete + search
      - CommandeService: create (auto-fill prices), updateStatut (enforce transitions COMMANDEE→RECUPEREE→PAYEE), calculateTotal
      - ProduitService: getCurrentYearTarif (auto-apply to new orders)
      - DashboardService: financial aggregations (CA from PAYEE orders only, expenses, profit, top products)
      - PdfService: label generation with PDFBox (coordinate calculations, French accents)
      - EtiquetteService: DLUO calculation (dateRecolte + 730 days), lot number generation (YYYY-TYPE-NNN sequence)
      - BackupService: @Scheduled daily 2 AM, copy honeyai.db to ./backups/, retention 30 days
      - Transaction boundaries (@Transactional on write operations)

   D. Controller & Endpoint Design
      - RESTful-style endpoints (even though server-side rendered):
        - GET /clients, POST /clients, GET /clients/{id}, POST /clients/{id}/delete
        - GET /commandes, POST /commandes, GET /commandes/{id}, POST /commandes/{id}/statut
        - GET /produits, POST /produits/{id}/tarif
        - GET /etiquettes, POST /etiquettes/generer (returns PDF byte[])
        - GET /achats, POST /achats
        - GET / or GET /dashboard (home page)
      - Flash messages via RedirectAttributes for success/error feedback
      - Form validation with @Valid and BindingResult

   E. Database Schema & Configuration
      - SQLite file: ./data/honeyai.db (created automatically first run)
      - Hibernate ddl-auto: update (simple for MVP, no Flyway/Liquibase)
      - Connection pool: Not needed (SQLite single connection)
      - Enum storage: @Enumerated(EnumType.STRING) for readability
      - Date handling: LocalDate for dates, LocalDateTime for timestamps
      - Decimal precision: BigDecimal scale 2 for money (euros)

   F. Configuration Management (application.yml)
      - Server config: port 8080, shutdown graceful
      - Datasource: jdbc:sqlite:./data/honeyai.db
      - Hibernate: dialect SQLiteDialect, ddl-auto update, show-sql false (prod)
      - Thymeleaf: cache false (dev), cache true (prod)
      - Custom section: honeyai.etiquettes (SIRET, nomApiculteur, adresse, tel, DLUO days, label dimensions)
      - Custom section: honeyai.backup (enabled, directory, schedule, retention days)
      - Profiles: dev vs. prod (logging levels, cache settings)

   G. Security & Error Handling
      - NO authentication (justified: mono-user familial, local only)
      - Input validation: Spring Validation (@NotBlank, @Min, @Positive)
      - GlobalExceptionHandler (@ControllerAdvice): catch 404, validation errors, business exceptions
      - Custom exceptions: ClientNotFoundException, InvalidStatusTransitionException, PdfGenerationException
      - Error pages: templates/error/404.html, 500.html with friendly French messages
      - Logging: Logback INFO level, file ./logs/honeyai.log, rotation daily, keep 7 days

   H. Testing Strategy
      - Unit tests: Services (80%+ coverage), focus on calculations (CA, DLUO, lot numbers, status transitions)
      - Integration tests: Controllers (@WebMvcTest), Repositories (@DataJpaTest)
      - PDF generation: Manual visual verification (AC requirement in Story 3.4)
      - NO E2E automated tests (cost/benefit not justified for family project)
      - User acceptance testing: 2-4 weeks with parents (manual)

2. BUILD & PACKAGING ARCHITECTURE

   A. Maven Configuration (pom.xml essentials)
      - Spring Boot parent 3.2.x
      - Dependencies: web, data-jpa, thymeleaf, validation, devtools, sqlite-jdbc, hibernate-community-dialects, pdfbox, lombok, test
      - spring-boot-maven-plugin with executable:true
      - Java 17 source/target

   B. launch4j Packaging Strategy
      - Configuration: launcher/honeyai-launch4j.xml
      - Output: HoneyAI.exe wrapper around honeyai-{version}.jar
      - Splash screen: launcher/splash.bmp (400×300px, "HoneyAI - Démarrage...")
      - Icon: launcher/icon.ico (honey/bee theme, 256×256 with embedded sizes)
      - JVM options: -Xms128m -Xmx512m -Dfile.encoding=UTF-8
      - JRE bundling: Look for ./jre/ first (bundled JRE 17), fallback to system Java
      - Error handling: Display "Java 17 required" if JRE not found

   C. Distribution Package Structure
      ```
      HoneyAI-v1.0/
      ├── HoneyAI.exe          (launch4j wrapper)
      ├── honeyai-1.0.0.jar    (Spring Boot fat JAR)
      ├── jre/                 (Optional: JRE 17 portable, ~200MB)
      ├── data/                (Created at runtime, contains honeyai.db)
      ├── backups/             (Created at runtime)
      ├── logs/                (Created at runtime)
      ├── README.txt           (Installation instructions)
      └── LICENSE.txt
      ```

3. TECHNICAL RISKS & MITIGATIONS

   Identify and document mitigation strategies for:
   - SQLite + Hibernate compatibility (test early in Epic 1.1)
   - PDF French accents rendering (prototype in Epic 3.1)
   - Dynamic form JavaScript (vanilla JS add/remove product lines in Story 2.6)
   - launch4j on multiple Windows versions (test on Win10 Home, Win11 in Story 5.6)
   - Performance with 1000+ records (<1s search per NFR2 - verify indexes work)

4. DEVELOPMENT WORKFLOW GUIDANCE

   - Epic sequencing: MUST follow order (1→2→3→4→5), dependencies documented in PRD
   - Each story deliverable: Working code + tests + documentation (acceptance criteria are detailed)
   - Dev environment: IntelliJ IDEA recommended, Maven wrapper (./mvnw)
   - Hot reload: Spring Boot DevTools active in dev profile
   - Testing approach: Write unit tests alongside feature code (TDD encouraged)
   - Commit strategy: Small commits per story, clear messages

OUTPUT FORMAT:
Create docs/architecture.md with sections for each deliverable above. Include:
- Mermaid diagrams for: package structure, entity relationships, service dependencies
- Code snippets for: key JPA entity examples, service method signatures, controller endpoints
- Configuration examples: application.yml sections, launch4j XML
- Decision rationale: Why this approach over alternatives (trace back to PRD constraints)

CRITICAL REMINDERS:
- Simplicity over cleverness (developer must maintain long-term as family support)
- Offline-first, no network calls (even localhost is isolated)
- Non-technical users (error messages must be friendly, no stack traces shown)
- French language (comments, logs, error messages all in French)
```

---

**Document Status:** Draft for Review
**Next Action:** Execute PM Checklist and generate handoff prompts
**Generated By:** Mary - Business Analyst (BMAD™ Core)
**Date:** 2026-01-17
