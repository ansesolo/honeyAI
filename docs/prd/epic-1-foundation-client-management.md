# Epic 1: Foundation & Client Management

**Epic Goal:** Établir l'infrastructure technique complète du projet HoneyAI (Spring Boot 3.2, SQLite, Maven, structure packages MVC) et livrer la première fonctionnalité métier end-to-end: la gestion des clients. Les utilisateurs pourront créer, consulter, modifier, rechercher et supprimer (soft delete) des fiches clients via une interface web Bootstrap moderne et accessible.

## Story 1.1: Project Bootstrap & Core Configuration

**As a** developer,
**I want** to initialize the Spring Boot Maven project with all necessary dependencies and configuration,
**so that** I have a solid foundation to build all application features with proper structure and tooling.

### Acceptance Criteria:

1. Maven project created with Spring Boot 3.2+ parent POM and Java 17 source/target configuration
2. Dependencies configured in pom.xml: spring-boot-starter-web, spring-boot-starter-data-jpa, spring-boot-starter-thymeleaf, spring-boot-starter-validation, spring-boot-devtools, sqlite-jdbc (3.45.0.0), hibernate-community-dialects, lombok, spring-boot-starter-test
3. Package structure created: com.honeyai with subpackages (config, controller, service, repository, model, enums, exception)
4. application.yml configured with: server.port=8080, SQLite datasource (jdbc:sqlite:./data/honeyai.db), Hibernate dialect (SQLiteDialect), ddl-auto=update, Thymeleaf settings (cache=false for dev)
5. HoneyAiApplication.java main class created with @SpringBootApplication annotation
6. Application starts successfully on localhost:8080 without errors
7. ./data/ directory created automatically at first run with honeyai.db file generated
8. DevTools enabled for hot reload during development
9. README.md created with: project description, tech stack, how to run (./mvnw spring-boot:run), and prerequisites (JDK 17+)

## Story 1.2: Client Entity & Repository with Soft Delete

**As a** developer,
**I want** to create the Client JPA entity with all required fields and a Spring Data repository,
**so that** I can persist and retrieve client data from SQLite with soft delete support to prevent accidental data loss.

### Acceptance Criteria:

1. Client.java entity created in model package with fields: id (Long, @Id @GeneratedValue), nom (String, @NotBlank), telephone (String), email (String), adresse (String), notes (String, @Column(length=1000)), createdAt (LocalDateTime, @CreatedDate), updatedAt (LocalDateTime, @LastModifiedDate), deletedAt (LocalDateTime, nullable for soft delete)
2. Client entity annotated with @Entity, @Table(name="clients"), @Data (Lombok)
3. Soft delete helper method isDeleted() returns true if deletedAt is not null
4. ClientRepository interface created extending JpaRepository<Client, Long> with custom query methods: findByDeletedAtIsNullOrderByNomAsc(), findByIdAndDeletedAtIsNull(Long id), searchClients(@Param("search") String search) using @Query searching nom and telephone
5. Hibernate creates clients table automatically in SQLite with all columns and proper types
6. Unit test for ClientRepository: save client, retrieve by id, verify soft delete (deletedAt set, record still in DB), verify search query returns matching clients
7. At least 3 test clients inserted successfully and retrieved without errors

## Story 1.3: Client Service with Business Logic

**As a** developer,
**I want** to create a ClientService layer that handles business logic for client operations including soft delete,
**so that** controllers remain thin and business rules are centralized and testable.

### Acceptance Criteria:

1. ClientService.java created in service package with @Service annotation and constructor injection of ClientRepository
2. Methods implemented: findAllActive() returns all non-deleted clients ordered by name, findById(Long id) returns Optional<Client> if not deleted, save(Client client) validates and persists (sets updatedAt), softDelete(Long id) sets deletedAt to now without physical deletion, searchClients(String search) delegates to repository search
3. Validation enforced: nom cannot be blank, throws custom ClientNotFoundException if client not found or soft-deleted when accessed by id
4. Unit tests with mocked repository: verify findAllActive excludes deleted clients, verify softDelete sets deletedAt and doesn't call repository.delete(), verify save updates updatedAt timestamp, verify searchClients handles empty/null search strings gracefully
5. Service methods transactional where needed (@Transactional on save/delete operations)
6. Test coverage: 80%+ on ClientService business logic

## Story 1.4: Base Layout & Navigation with Bootstrap

**As a** developer,
**I want** to create a Thymeleaf base layout with Bootstrap 5 styling and navigation menu,
**so that** all pages have consistent branding, navigation, and responsive design without duplicating HTML.

### Acceptance Criteria:

1. templates/fragments/layout.html created with Thymeleaf layout dialect containing: HTML5 boilerplate, Bootstrap 5.3 CDN links (CSS + JS), Font Awesome 6.4 CDN for icons, meta viewport for responsive, title block replaceable
2. Header fragment with logo "🍯 HoneyAI" and navigation menu (sidebar or top nav) with links: Tableau de bord, Clients, Commandes, Étiquettes, Achats - using Bootstrap nav components
3. Main content area with Thymeleaf th:block layout:fragment="content" for page-specific content
4. Footer fragment with simple text "HoneyAI © 2026" centered
5. CSS custom styles in static/css/custom.css for: minimum font-size 16px body text, primary color palette (amber #F4B942, forest green #2D5016), button sizes minimum 44x44px touch targets
6. Navigation highlights active page using Bootstrap .active class
7. Layout fully responsive: collapses navigation on mobile (<768px), readable on desktop (1280x720+)
8. Home page (templates/home.html) created using layout with placeholder content "Bienvenue sur HoneyAI" to verify layout works
9. Application accessible at localhost:8080 displaying home page with navigation functional

## Story 1.5: Client List & Search Interface

**As a** beekeeper (parent user),
**I want** to view a list of all my clients and search them by name or phone number,
**so that** I can quickly find and access client information without scrolling through a paper notebook.

### Acceptance Criteria:

1. ClientController.java created with @Controller, @RequestMapping("/clients"), GET endpoint /clients returns "clients/list" view with model containing list of active clients
2. templates/clients/list.html created using base layout with: page title "Mes Clients" with user icon, prominent search bar (large input, placeholder "Rechercher un client par nom ou téléphone..."), "Nouveau Client" button (green, large, top-right), table or card grid displaying clients with columns: Nom, Téléphone, Email, Nombre de orders (placeholder 0 for now), Actions (Voir, Modifier buttons)
3. Search bar submits GET request to /clients?search={query}, controller filters clients using ClientService.searchClients(), results displayed in same view
4. Empty state message displayed if no clients found: "Aucun client trouvé" with icon
5. Success flash message displayed if redirected from save/delete (e.g., "Client enregistré avec succès") using Bootstrap alerts dismissible
6. Table/cards responsive: stacks on mobile, readable on desktop
7. Minimum 3 seed clients visible in list for demo purposes
8. Search returns results instantly (<1s) and highlights search term (optional but nice UX)
9. Font size 16px minimum, buttons 44x44px minimum, WCAG AA contrast ratios respected

## Story 1.6: Client Detail View with History Placeholder

**As a** beekeeper (parent user),
**I want** to view detailed information about a specific client including their contact details and a placeholder for order history,
**so that** I can see all relevant information about a client in one place.

### Acceptance Criteria:

1. GET endpoint /clients/{id} in ClientController returns "clients/detail" view with Client object in model or throws ClientNotFoundException if not found/deleted
2. templates/clients/detail.html created displaying: client name as heading, contact information (telephone, email, address) in readable format with labels, notes section if present, creation/update timestamps (formatted DD/MM/YYYY HH:mm)
3. "Modifier" button links to /clients/{id}/edit (green, prominent)
4. "Supprimer" button triggers soft delete with confirmation modal ("Êtes-vous sûr de vouloir supprimer ce client?"), POST to /clients/{id}/delete, red button positioned away from primary actions
5. "Retour à la liste" link to /clients (secondary styling)
6. Order history section with placeholder message: "Historique des orders (à venir dans Epic 2)" or empty state "Aucune order pour ce client"
7. "Nouvelle order pour ce client" button disabled/greyed with tooltip "Disponible dans Epic 2"
8. 404 error page displayed (friendly message) if client id doesn't exist or is soft-deleted
9. Page layout clean, readable, no clutter, respects accessibility guidelines

## Story 1.7: Client Create & Edit Forms

**As a** beekeeper (parent user),
**I want** to create a new client or edit an existing one through a simple form,
**so that** I can maintain accurate and up-to-date information about my customers.

### Acceptance Criteria:

1. GET endpoint /clients/nouveau in ClientController returns "clients/form" view with empty Client object for creation
2. GET endpoint /clients/{id}/edit returns "clients/form" view with populated Client object for editing
3. POST endpoint /clients (for both create and update) validates Client object using @Valid, saves via ClientService, redirects to /clients with success flash message, or returns form with error messages if validation fails
4. templates/clients/form.html created with: form fields for nom (required, text input), telephone (text input), email (email input), adresse (textarea), notes (textarea large), clear labels above each field, Bootstrap form-control styling
5. Validation feedback displayed inline: red text below field showing error (e.g., "Le nom est obligatoire") if @NotBlank violated
6. Form buttons: "Enregistrer" (green, large, primary) and "Annuler" (grey, secondary) linking back to /clients or /clients/{id}
7. Form title dynamic: "Nouveau Client" for create, "Modifier Client - {nom}" for edit
8. Required fields marked with asterisk (*) next to label
9. Success message after save: "Client {nom} enregistré avec succès" displayed as dismissible Bootstrap alert on list page
10. Form accessible: keyboard navigation works (Tab through fields, Enter to submit), labels properly associated with inputs (for attribute)
11. Telephone field accepts French format (10 digits or international format), no strict validation but accepts any string for flexibility

## Story 1.8: Client Soft Delete with Confirmation

**As a** beekeeper (parent user),
**I want** to delete a client safely with confirmation and the ability to potentially restore later,
**so that** I don't permanently lose important customer data by accident.

### Acceptance Criteria:

1. POST endpoint /clients/{id}/delete in ClientController calls ClientService.softDelete(id), redirects to /clients with success message "Client supprimé"
2. Soft delete sets deletedAt timestamp without removing record from database
3. Confirmation modal implemented on detail and list pages: Bootstrap modal with title "Confirmer la suppression", message "Êtes-vous sûr de vouloir supprimer le client {nom}? Cette action peut être annulée par l'administrateur.", buttons "Annuler" (grey) and "Supprimer" (red)
4. Deleted clients no longer appear in /clients list or search results
5. Accessing /clients/{id} of deleted client returns 404 with message "Ce client n'existe pas ou a été supprimé"
6. Database verification: deletedAt column populated with timestamp, all other data intact (id, nom, telephone, etc. preserved)
7. Success flash message displayed: "Client {nom} supprimé avec succès"
8. Delete button styled red, positioned away from primary actions (bottom-right or separate section)
9. No cascade delete of related data (future orders) - just client marked deleted (Epic 2 will handle order relationships)

---
