# HoneyAI Product Requirements Document (PRD)

**Version:** 1.0
**Date:** 2026-01-17
**Author:** Mary - Business Analyst
**Status:** Draft for Review

---

## Goals and Background Context

### Goals

Les objectifs à atteindre si ce PRD est implémenté avec succès:

- Remplacer complètement le système papier (cahier de orders) par une solution numérique intuitive
- Réduire de 50% le temps passé sur les tâches administratives (étiquettes, recherche orders)
- Générer des étiquettes réglementaires conformes en moins de 2 minutes pour 10 étiquettes
- Fournir une visibilité financière claire (CA, dépenses, bénéfices) sans complexité comptable
- Permettre aux parents apiculteurs (utilisateurs non-techniques) d'être autonomes après 2 démonstrations
- Assurer une adoption complète avec abandon du cahier papier dans les 2 mois suivant le déploiement
- Garantir 0 perte de données avec backups automatiques et robustesse absolue
- Simplifier le workflow existant sans imposer de nouvelles méthodes de travail

### Background Context

HoneyAI répond au besoin d'une exploitation apicole familiale (~40 ruches) actuellement gérée via un système papier (cahier de orders) qui présente des limites critiques: gestion fragmentée des orders, préparation chronophage d'étiquettes réglementaires, suivi financier approximatif, et absence de centralisation des données clients et production.

L'application vise à transformer ces processus tout en préservant la simplicité du workflow actuel. Contrairement aux solutions professionnelles existantes (trop complexes et coûteuses) ou aux alternatives génériques (Excel, applications cloud), HoneyAI est conçue sur mesure pour des utilisateurs non-techniques (50-65 ans, compétences informatiques basiques) qui ont besoin d'une solution standalone, offline-first, avec une courbe d'apprentissage nulle. Le développement est assuré par un membre de la famille expert Spring Boot, garantissant maintenance et support à long terme. La décision technique (Spring Boot + interface web locale Bootstrap) permet un développement rapide (~40h) tout en offrant une interface moderne sans nécessiter d'expertise frontend avancée.

### Change Log

| Date | Version | Description | Author |
|------|---------|-------------|--------|
| 2026-01-17 | 1.0 | PRD initial créé à partir du Project Brief v1.0 | Mary (Business Analyst) |

---

## Requirements

### Functional Requirements

**FR1:** Le système doit permettre de créer, modifier, consulter et supprimer (soft delete) des fiches clients contenant: nom (obligatoire), téléphone, email (optionnel), adresse, et notes libres.

**FR2:** Le système doit fournir une fonction de recherche de clients par nom ou numéro de téléphone avec résultats instantanés (<1 seconde).

**FR3:** Le système doit afficher automatiquement l'historique complet des orders pour chaque client sur sa fiche détaillée.

**FR4:** Le système doit permettre de créer une order en sélectionnant un client et en ajoutant des produits avec leurs quantités.

**FR5:** Le système doit gérer trois statuts de order avec transitions simples via boutons: "Commandée" → "Récupérée" → "Payée".

**FR6:** Le système doit mettre à jour automatiquement le stock de produits finis lors du passage d'une order au statut "Récupérée".

**FR7:** Le système doit permettre de filtrer et rechercher les orders par client, statut, et date avec vue par année (année en cours par défaut).

**FR8:** Le système doit gérer un catalogue de produits prédéfinis: Miel (500g/1kg), Cire avec miel, Reines avec types de miel (Toutes fleurs, Forêt, Châtaignier).

**FR9:** Le système doit permettre de définir des tarifs par produit et par année, avec application automatique des tarifs de l'année en cours lors de la création de orders.

**FR10:** Le système doit générer des étiquettes réglementaires pour pots de miel au format PDF téléchargeable via un formulaire simple (type miel, format pot, date récolte, quantité).

**FR11:** Les étiquettes générées doivent contenir automatiquement: nom exploitation, adresse, SIRET, téléphone, prix, poids, DLUO calculée (date récolte + 2 ans), et numéro de lot automatique.

**FR12:** Le système doit afficher un tableau de bord financier présentant: CA total (filtrable par mois/année), dépenses totales, bénéfice brut (CA - dépenses), et top 3 produits vendus.

**FR13:** Le système doit permettre d'enregistrer des achats de fournitures avec: date, désignation, montant, catégorie (Cire, Pots, Couvercles, Nourrissement, Autre), et notes.

**FR14:** Le système doit afficher une liste chronologique des achats filtrable par année et catégorie avec calcul automatique du total des dépenses.

**FR15:** Le système doit effectuer un backup automatique quotidien de la base de données SQLite vers un dossier local avec horodatage.

**FR16:** Le système doit permettre l'export manuel de la base de données via un bouton "Sauvegarder mes données" créant un fichier .db daté.

**FR17:** Le système doit afficher des messages de confirmation clairs après chaque action importante (enregistrement, suppression, modification) pour rassurer l'utilisateur.

**FR18:** Le système doit permettre d'ajouter des notes libres sur les orders (ex: "livraison prévue 15/10") pour capturer des informations non structurées.

### Non-Functional Requirements

**NFR1:** L'application doit démarrer en moins de 5 secondes (démarrage Spring Boot + ouverture automatique du navigateur).

**NFR2:** Toutes les recherches et requêtes doivent retourner des résultats en moins de 1 seconde pour des volumes allant jusqu'à 1000 clients et 2000 orders.

**NFR3:** La génération d'une planche de 10 étiquettes PDF doit prendre moins de 5 secondes.

**NFR4:** Le chargement de chaque page web doit prendre moins de 1 seconde.

**NFR5:** L'interface utilisateur doit utiliser une police de taille minimale de 16px pour assurer la lisibilité pour des utilisateurs de 50-65 ans.

**NFR6:** L'application doit fonctionner entièrement en mode offline sans aucune dépendance réseau (localhost uniquement).

**NFR7:** L'application doit être compatible Windows 10+ (64 bits) avec un minimum de 4 GB RAM et 500 MB d'espace disque.

**NFR8:** L'application doit être distribuée sous forme d'un exécutable .exe autonome (JAR + wrapper launch4j) avec JRE 17 embarqué.

**NFR9:** L'application doit stocker toutes les données localement dans un fichier SQLite unique sans communication externe.

**NFR10:** L'interface doit utiliser de gros boutons et des formulaires simples pour minimiser les erreurs de manipulation par des utilisateurs non-techniques.

**NFR11:** Les suppressions de données doivent être des soft deletes (marquage supprimé, pas effacement physique) pour prévenir les pertes accidentelles.

**NFR12:** L'application doit afficher un splash screen "HoneyAI démarre..." pendant le chargement pour indiquer clairement que l'application se lance.

**NFR13:** L'application doit maintenir une disponibilité de 99.9% (aucun crash, gestion robuste des erreurs).

**NFR14:** Le code doit être maintenable avec une architecture Spring Boot standard (Controllers/Services/Repositories) et commenté pour faciliter le support long terme.

**NFR15:** Les backups automatiques doivent conserver les 30 derniers jours d'historique et supprimer automatiquement les backups plus anciens.

**NFR16:** L'application doit ouvrir automatiquement le navigateur par défaut sur localhost:8080 au démarrage sans intervention manuelle.

**NFR17:** Les étiquettes PDF générées doivent être conformes à la réglementation DGCCRF française pour l'étiquetage du miel (mentions obligatoires complètes).

---

## User Interface Design Goals

### Overall UX Vision

HoneyAI adopte une philosophie de "cahier numérique augmenté" - une interface qui disparaît derrière l'usage naturel, permettant aux parents apiculteurs d'accomplir leurs tâches sans y penser, exactement comme ils utilisaient leur cahier papier, mais avec tous les bénéfices du numérique.

**Principes directeurs:**
- **Zéro courbe d'apprentissage:** L'interface s'inspire du workflow papier existant plutôt que d'imposer de nouvelles méthodes
- **Simplicité radicale:** Aucune fonctionnalité inutile, navigation évidente, pas de menus cachés
- **Confiance et contrôle:** Messages de confirmation clairs, impossible de "casser" l'application, actions réversibles
- **Lisibilité optimale:** Police grande (16px minimum), contraste élevé, espacement généreux pour utilisateurs 50-65 ans
- **Efficacité immédiate:** Les tâches fréquentes (nouvelle order, génération étiquettes) accessibles en 2 clics maximum

### Key Interaction Paradigms

**Navigation principale:**
- Menu latéral persistant avec grandes icônes et labels textuels clairs (Clients, Commandes, Étiquettes, Tableau de bord, Achats)
- Pas de navigation hiérarchique complexe - toutes les fonctions principales au même niveau
- Fil d'Ariane simple pour indiquer la position actuelle

**Formulaires:**
- Champs larges avec labels au-dessus (pas à côté) pour clarté
- Validation en temps réel avec messages encourageants ("✓ Bien enregistré")
- Boutons d'action principaux en vert/large, secondaires en gris/plus petits
- Ordre des champs suit la logique conversationnelle (comme remplir le cahier papier)

**Listes et recherche:**
- Barre de recherche proéminente en haut avec placeholder explicite ("Rechercher un client par nom ou téléphone...")
- Résultats affichés en cartes ou tableaux avec alternance de couleurs pour lisibilité
- Pas de pagination complexe - scroll infini ou "Voir plus" simple

**Actions destructives:**
- Confirmation modale explicite ("Êtes-vous sûr de vouloir supprimer ce client?")
- Boutons de suppression en rouge, positionnés à l'écart des actions principales
- Toujours possibilité d'annuler ou de restaurer (soft delete)

**Feedback utilisateur:**
- Toast notifications en haut à droite pour confirmations rapides
- Messages d'erreur en rouge clair, expliquant clairement le problème et la solution
- Indicateurs de chargement pour opérations >1 seconde (spinner discret)

### Core Screens and Views

1. **Tableau de bord (Home/Dashboard)** - Résumé financier en cartes visuelles (CA, dépenses, bénéfice), orders récentes/en cours, accès rapide aux actions fréquentes

2. **Liste Clients** - Barre de recherche proéminente, vue tableau ou cartes avec nom, téléphone, nombre de orders, bouton "Nouveau Client"

3. **Fiche Client (Détail)** - Informations client éditables, historique complet des orders en dessous, bouton "Nouvelle order pour ce client"

4. **Formulaire Client (Création/Édition)** - Formulaire vertical simple (Nom, Téléphone, Email, Adresse, Notes), boutons "Enregistrer" et "Annuler"

5. **Liste Commandes** - Filtres simples par année/statut en haut, vue tableau avec client, date, statut, montant total, badges colorés pour statuts

6. **Fiche Commande (Détail)** - Informations client + produits commandés, boutons de transition de statut visibles, montant total calculé automatiquement

7. **Formulaire Commande (Création/Édition)** - Sélection client (autocomplete), ajout de lignes produits (sélecteur produit + quantité), prix calculés automatiquement, zone notes libres

8. **Génération Étiquettes** - Formulaire simple: Type miel, Format pot, Date récolte, Quantité étiquettes, aperçu visuel d'une étiquette exemple, bouton "Générer PDF"

9. **Catalogue Produits & Tarifs** - Vue tableau produits avec prix par année, possibilité de modifier tarifs année future, prix année en cours mis en évidence

10. **Achats Fournitures** - Liste chronologique avec filtres année/catégorie, total dépenses affiché en haut, formulaire d'ajout rapide

### Accessibility

**Niveau ciblé:** WCAG AA (minimum)

**Mesures spécifiques:**
- Contraste minimum 4.5:1 pour tout le texte
- Taille de police: 16px minimum, 18px pour texte principal
- Tous les boutons et zones interactives: minimum 44x44px (facile à cliquer)
- Navigation entièrement possible au clavier (Tab, Enter, Esc)
- Labels explicites sur tous les champs de formulaire (pas de placeholders seuls)
- Messages d'erreur associés aux champs via aria-describedby
- Pas de dépendance uniquement sur la couleur (icônes + texte pour statuts)

### Branding

**Style visuel:**
- **Palette simple et naturelle:** Tons miel/ambre (#F4B942) pour éléments positifs, vert forêt (#2D5016) pour navigation, blanc cassé (#FAFAF8) pour fond
- **Typographie:** Sans-serif moderne et lisible (Roboto ou Inter), pas de polices décoratives
- **Iconographie:** Font Awesome pour cohérence, icônes simples reconnaissables
- **Ton:** Chaleureux, rassurant, jamais technique ou corporate
- **Logo:** Simple mention "🍯 HoneyAI" en haut de page

### Target Device and Platforms

**Plateforme principale:** Web Responsive (Desktop-First)

**Spécifications:**
- **Desktop (prioritaire):** Optimisé pour écrans 1280x720 minimum (résolution Windows standard)
- **Tablette (futur):** Responsive design permet consultation sur tablette en mode lecture
- **Mobile (hors scope MVP):** Non optimisé pour mobile dans MVP (usage bureau uniquement)

**Navigateurs supportés:** Chrome 90+, Microsoft Edge 90+, Firefox 88+

---

## Technical Assumptions

### Repository Structure

**Type:** Monorepo

**Structure validée:**
```
honeyAI/
├── src/main/java/com/honeyai/
│   ├── HoneyAiApplication.java          # Main Spring Boot
│   ├── config/                          # Configuration classes
│   ├── controller/                      # MVC Controllers
│   ├── service/                         # Business logic layer
│   ├── repository/                      # Spring Data JPA repositories
│   ├── model/                           # JPA entities
│   ├── enums/                           # Enumerations
│   └── exception/                       # Exception handling
├── src/main/resources/
│   ├── application.yml                  # Spring configuration
│   ├── static/                          # CSS, JS, images
│   └── templates/                       # Thymeleaf HTML templates
├── src/test/java/                       # Tests unitaires
├── data/                                # SQLite database (runtime)
├── docs/                                # Documentation
├── launcher/                            # Packaging (launch4j config)
└── pom.xml                              # Maven dependencies
```

### Service Architecture

**Architecture:** Monolith (Spring Boot standalone application)

**Rationale:** Pas de microservices (complexité inutile pour application mono-poste), pattern MVC classique (Controllers → Services → Repositories → Database), server-side rendering (Thymeleaf génère HTML côté serveur).

**Technologies stack:**

| Composant | Technologie | Version | Rationale |
|-----------|-------------|---------|-----------|
| **Runtime** | Java (JRE) | 17 LTS | Support long terme, stabilité |
| **Framework** | Spring Boot | 3.2+ | Framework maîtrisé, productivité maximale |
| **Web** | Spring Web + Thymeleaf | Inclus SB | MVC server-side rendering |
| **ORM** | Spring Data JPA + Hibernate | Inclus SB | Abstraction CRUD automatique |
| **Database** | SQLite | 3.45+ | Fichier unique, zéro configuration |
| **JDBC Driver** | xerial sqlite-jdbc | 3.45.0.0 | Driver JDBC standard |
| **PDF Generation** | Apache PDFBox | 3.0+ | Open source, gratuit, API Java simple |
| **Frontend CSS** | Bootstrap | 5.3+ | Composants UI modernes, responsive |
| **Frontend JS** | JavaScript Vanilla | ES6+ | Interactions légères |
| **Icons** | Font Awesome | 6.4+ | Iconographie cohérente |
| **Build** | Maven | 3.8+ | Gestion dépendances standard |
| **Packaging** | launch4j | 1.7+ | Wrapper .exe Windows |
| **Testing** | Spring Boot Test + JUnit 5 | Inclus SB | Tests unitaires et d'intégration |

### Testing Requirements

**Stratégie:** Unit + Integration (Testing Pyramid partiel)

**Couverture cible:**
- **Unit Tests (JUnit 5 + Mockito):** Services layer avec 80%+ couverture, focus sur calculs critiques (financiers, DLUO, transitions statuts)
- **Integration Tests (Spring Boot Test):** Controllers principaux, requêtes custom repositories, contraintes database
- **Tests manuels critiques:** Génération PDF (conformité visuelle), workflow complet orders, backup automatique, performance, UX avec parents (2-4 semaines)
- **Pas de tests E2E automatisés:** Coût/maintenance vs. bénéfice défavorable pour projet familial

### Additional Technical Assumptions and Requests

**Database & Data Management:**
- Hibernate ddl-auto: update (pas de migrations Flyway/Liquibase pour MVP)
- Indexes SQLite sur colonnes recherchées fréquemment (clients.nom, orders.date_commande)
- Transactions @Transactional Spring pour opérations multi-tables
- Isolation niveau READ_COMMITTED (mono-utilisateur)

**Security:**
- Pas d'authentification (application mono-utilisateur familial)
- Pas de chiffrement données (locales uniquement)
- Validation inputs via Spring Validation (@NotBlank, @Min, etc.)

**Logging & Monitoring:**
- Logback niveau INFO en production, DEBUG en dev
- Logs vers ./logs/honeyai.log avec rotation quotidienne (max 7 jours)
- Simple endpoint /actuator/health pour health check

**Error Handling:**
- @ControllerAdvice global exception handler pour pages erreur conviviales
- Try-catch larges pour éviter plantages
- Exceptions custom (ClientNotFoundException, InvalidCommandeStateException, etc.)

**PDF Generation:**
- Apache PDFBox 3.x
- Dimensions étiquettes configurables dans application.yml
- Police Arial ou système (pas de fonts custom)
- Format papier A4 standard

**Backup Strategy:**
- @Scheduled(cron = "0 0 2 * * ?") pour backup quotidien 2h du matin
- Destination ./backups/honeyai-backup-YYYY-MM-DD-HHmmss.db
- Rétention 30 jours, suppression automatique backups anciens
- Export manuel via bouton UI

**Internationalization:**
- Français uniquement pour MVP
- Format dates DD/MM/YYYY
- Nombres: séparateur décimal virgule (1 234,56 €)
- Devise Euro (€) hardcodé

**Browser Compatibility:**
- Pas de support IE11 (navigateurs modernes uniquement)
- Bootstrap 5 + CSS custom minimal
- JavaScript ES6+ (pas de transpilation Babel)

**Packaging & Distribution:**
- JAR exécutable via Spring Boot Maven Plugin
- Wrapper .exe avec launch4j (icon, splash screen, console cachée)
- JRE 17 embarqué (optionnel mais recommandé) dans distribution
- Pas d'installeur Windows complexe pour MVP - copie dossier suffit
- Pas de système mise à jour auto - mise à jour manuelle

---

## Epic List

### Epic 1: Foundation & Client Management
Établir l'infrastructure projet (Spring Boot, SQLite, structure Maven) et livrer la première fonctionnalité complète: gestion des clients avec CRUD, recherche, et interface Bootstrap fonctionnelle.

### Epic 2: Order Management & Product Catalog
Implémenter le système de orders avec workflow de statuts (Commandée → Récupérée → Payée) et le catalogue produits avec gestion des tarifs par année, permettant la création et le suivi complet des orders clients.

### Epic 3: Label Generation (Killer Feature)
Développer la fonctionnalité de génération d'étiquettes réglementaires PDF avec Apache PDFBox, incluant calcul automatique DLUO, numéro de lot, et conformité DGCCRF - la fonctionnalité "coup de cœur" qui apporte une valeur immédiate.

### Epic 4: Financial Dashboard & Purchases
Créer le tableau de bord financier avec calculs de CA, dépenses, bénéfices, et top produits, ainsi que le module de suivi des achats de fournitures avec filtres et totaux automatiques.

### Epic 5: Backup, Packaging & Production Readiness
Implémenter le système de backup automatique quotidien, créer le packaging .exe avec launch4j, finaliser le polish UX (confirmations, messages d'erreur), et préparer la distribution pour les tests utilisateurs finaux avec les parents.

---

## Epic 1: Foundation & Client Management

**Epic Goal:** Établir l'infrastructure technique complète du projet HoneyAI (Spring Boot 3.2, SQLite, Maven, structure packages MVC) et livrer la première fonctionnalité métier end-to-end: la gestion des clients. Les utilisateurs pourront créer, consulter, modifier, rechercher et supprimer (soft delete) des fiches clients via une interface web Bootstrap moderne et accessible.

### Story 1.1: Project Bootstrap & Core Configuration

**As a** developer,
**I want** to initialize the Spring Boot Maven project with all necessary dependencies and configuration,
**so that** I have a solid foundation to build all application features with proper structure and tooling.

#### Acceptance Criteria:

1. Maven project created with Spring Boot 3.2+ parent POM and Java 17 source/target configuration
2. Dependencies configured in pom.xml: spring-boot-starter-web, spring-boot-starter-data-jpa, spring-boot-starter-thymeleaf, spring-boot-starter-validation, spring-boot-devtools, sqlite-jdbc (3.45.0.0), hibernate-community-dialects, lombok, spring-boot-starter-test
3. Package structure created: com.honeyai with subpackages (config, controller, service, repository, model, enums, exception)
4. application.yml configured with: server.port=8080, SQLite datasource (jdbc:sqlite:./data/honeyai.db), Hibernate dialect (SQLiteDialect), ddl-auto=update, Thymeleaf settings (cache=false for dev)
5. HoneyAiApplication.java main class created with @SpringBootApplication annotation
6. Application starts successfully on localhost:8080 without errors
7. ./data/ directory created automatically at first run with honeyai.db file generated
8. DevTools enabled for hot reload during development
9. README.md created with: project description, tech stack, how to run (./mvnw spring-boot:run), and prerequisites (JDK 17+)

### Story 1.2: Client Entity & Repository with Soft Delete

**As a** developer,
**I want** to create the Client JPA entity with all required fields and a Spring Data repository,
**so that** I can persist and retrieve client data from SQLite with soft delete support to prevent accidental data loss.

#### Acceptance Criteria:

1. Client.java entity created in model package with fields: id (Long, @Id @GeneratedValue), nom (String, @NotBlank), telephone (String), email (String), adresse (String), notes (String, @Column(length=1000)), createdAt (LocalDateTime, @CreatedDate), updatedAt (LocalDateTime, @LastModifiedDate), deletedAt (LocalDateTime, nullable for soft delete)
2. Client entity annotated with @Entity, @Table(name="clients"), @Data (Lombok)
3. Soft delete helper method isDeleted() returns true if deletedAt is not null
4. ClientRepository interface created extending JpaRepository<Client, Long> with custom query methods: findByDeletedAtIsNullOrderByNomAsc(), findByIdAndDeletedAtIsNull(Long id), searchClients(@Param("search") String search) using @Query searching nom and telephone
5. Hibernate creates clients table automatically in SQLite with all columns and proper types
6. Unit test for ClientRepository: save client, retrieve by id, verify soft delete (deletedAt set, record still in DB), verify search query returns matching clients
7. At least 3 test clients inserted successfully and retrieved without errors

### Story 1.3: Client Service with Business Logic

**As a** developer,
**I want** to create a ClientService layer that handles business logic for client operations including soft delete,
**so that** controllers remain thin and business rules are centralized and testable.

#### Acceptance Criteria:

1. ClientService.java created in service package with @Service annotation and constructor injection of ClientRepository
2. Methods implemented: findAllActive() returns all non-deleted clients ordered by name, findById(Long id) returns Optional<Client> if not deleted, save(Client client) validates and persists (sets updatedAt), softDelete(Long id) sets deletedAt to now without physical deletion, searchClients(String search) delegates to repository search
3. Validation enforced: nom cannot be blank, throws custom ClientNotFoundException if client not found or soft-deleted when accessed by id
4. Unit tests with mocked repository: verify findAllActive excludes deleted clients, verify softDelete sets deletedAt and doesn't call repository.delete(), verify save updates updatedAt timestamp, verify searchClients handles empty/null search strings gracefully
5. Service methods transactional where needed (@Transactional on save/delete operations)
6. Test coverage: 80%+ on ClientService business logic

### Story 1.4: Base Layout & Navigation with Bootstrap

**As a** developer,
**I want** to create a Thymeleaf base layout with Bootstrap 5 styling and navigation menu,
**so that** all pages have consistent branding, navigation, and responsive design without duplicating HTML.

#### Acceptance Criteria:

1. templates/fragments/layout.html created with Thymeleaf layout dialect containing: HTML5 boilerplate, Bootstrap 5.3 CDN links (CSS + JS), Font Awesome 6.4 CDN for icons, meta viewport for responsive, title block replaceable
2. Header fragment with logo "🍯 HoneyAI" and navigation menu (sidebar or top nav) with links: Tableau de bord, Clients, Commandes, Étiquettes, Achats - using Bootstrap nav components
3. Main content area with Thymeleaf th:block layout:fragment="content" for page-specific content
4. Footer fragment with simple text "HoneyAI © 2026" centered
5. CSS custom styles in static/css/custom.css for: minimum font-size 16px body text, primary color palette (amber #F4B942, forest green #2D5016), button sizes minimum 44x44px touch targets
6. Navigation highlights active page using Bootstrap .active class
7. Layout fully responsive: collapses navigation on mobile (<768px), readable on desktop (1280x720+)
8. Home page (templates/home.html) created using layout with placeholder content "Bienvenue sur HoneyAI" to verify layout works
9. Application accessible at localhost:8080 displaying home page with navigation functional

### Story 1.5: Client List & Search Interface

**As a** beekeeper (parent user),
**I want** to view a list of all my clients and search them by name or phone number,
**so that** I can quickly find and access client information without scrolling through a paper notebook.

#### Acceptance Criteria:

1. ClientController.java created with @Controller, @RequestMapping("/clients"), GET endpoint /clients returns "clients/list" view with model containing list of active clients
2. templates/clients/list.html created using base layout with: page title "Mes Clients" with user icon, prominent search bar (large input, placeholder "Rechercher un client par nom ou téléphone..."), "Nouveau Client" button (green, large, top-right), table or card grid displaying clients with columns: Nom, Téléphone, Email, Nombre de orders (placeholder 0 for now), Actions (Voir, Modifier buttons)
3. Search bar submits GET request to /clients?search={query}, controller filters clients using ClientService.searchClients(), results displayed in same view
4. Empty state message displayed if no clients found: "Aucun client trouvé" with icon
5. Success flash message displayed if redirected from save/delete (e.g., "Client enregistré avec succès") using Bootstrap alerts dismissible
6. Table/cards responsive: stacks on mobile, readable on desktop
7. Minimum 3 seed clients visible in list for demo purposes
8. Search returns results instantly (<1s) and highlights search term (optional but nice UX)
9. Font size 16px minimum, buttons 44x44px minimum, WCAG AA contrast ratios respected

### Story 1.6: Client Detail View with History Placeholder

**As a** beekeeper (parent user),
**I want** to view detailed information about a specific client including their contact details and a placeholder for order history,
**so that** I can see all relevant information about a client in one place.

#### Acceptance Criteria:

1. GET endpoint /clients/{id} in ClientController returns "clients/detail" view with Client object in model or throws ClientNotFoundException if not found/deleted
2. templates/clients/detail.html created displaying: client name as heading, contact information (telephone, email, address) in readable format with labels, notes section if present, creation/update timestamps (formatted DD/MM/YYYY HH:mm)
3. "Modifier" button links to /clients/{id}/edit (green, prominent)
4. "Supprimer" button triggers soft delete with confirmation modal ("Êtes-vous sûr de vouloir supprimer ce client?"), POST to /clients/{id}/delete, red button positioned away from primary actions
5. "Retour à la liste" link to /clients (secondary styling)
6. Order history section with placeholder message: "Historique des orders (à venir dans Epic 2)" or empty state "Aucune order pour ce client"
7. "Nouvelle order pour ce client" button disabled/greyed with tooltip "Disponible dans Epic 2"
8. 404 error page displayed (friendly message) if client id doesn't exist or is soft-deleted
9. Page layout clean, readable, no clutter, respects accessibility guidelines

### Story 1.7: Client Create & Edit Forms

**As a** beekeeper (parent user),
**I want** to create a new client or edit an existing one through a simple form,
**so that** I can maintain accurate and up-to-date information about my customers.

#### Acceptance Criteria:

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

### Story 1.8: Client Soft Delete with Confirmation

**As a** beekeeper (parent user),
**I want** to delete a client safely with confirmation and the ability to potentially restore later,
**so that** I don't permanently lose important customer data by accident.

#### Acceptance Criteria:

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

## Epic 2: Order Management & Product Catalog

**Epic Goal:** Implémenter le cœur métier de HoneyAI en livrant le système complet de gestion des orders et du catalogue produits. Les utilisateurs pourront créer et suivre des orders clients à travers les trois statuts métier (Commandée → Récupérée → Payée), gérer un catalogue de produits miel avec leurs tarifs évolutifs par année, et bénéficier d'une application automatique des prix lors de la création des orders.

### Story 2.1: Product & Tarif Entities with Year-based Pricing

**As a** developer,
**I want** to create Product and Tarif JPA entities with their repositories to support the product catalog with historical year-based pricing,
**so that** I can store honey products (types and formats) and track price changes across years while automatically applying current year prices to new orders.

#### Acceptance Criteria:

1. Produit.java entity created with fields: id (Long, @Id @GeneratedValue), nom (String, "Miel" / "Cire avec miel" / "Reine"), type (TypeMiel enum: TOUTES_FLEURS, FORET, CHATAIGNIER - nullable for non-honey products), unite (String: "pot 500g", "pot 1kg", "unité")
2. TypeMiel enum created in enums package with values: TOUTES_FLEURS, FORET, CHATAIGNIER, with display labels in French
3. Tarif.java entity created with fields: id (Long), produitId (Long, @ManyToOne foreign key to Produit), annee (Integer, year like 2024), prix (BigDecimal, 2 decimal places for euros), @UniqueConstraint(produitId, annee) to prevent duplicate prices for same product/year
4. Relation: Produit has @OneToMany List<Tarif> tarifs, Tarif has @ManyToOne Produit produit
5. ProduitRepository interface extending JpaRepository<Produit, Long> with method: findAllByOrderByNomAsc()
6. TarifRepository interface extending JpaRepository<Tarif, Long> with methods: findByProduitIdAndAnnee(Long produitId, Integer annee), findByAnnee(Integer annee) for getting all prices of a specific year
7. Hibernate creates produits and tarifs tables in SQLite with proper foreign key constraints
8. Unit tests: create products (Miel 500g Toutes Fleurs, Miel 1kg Forêt, Cire avec miel, Reine), assign tarifs for years 2024 and 2025, verify retrieval by product and year, verify unique constraint violation if duplicate tarif inserted
9. Seed data: At least 6 products created (3 honey types × 2 formats + Cire + Reine) with 2024 prices populated

### Story 2.2: Commande & LigneCommande Entities with Status Workflow

**As a** developer,
**I want** to create Commande and LigneCommande JPA entities with status management and order line items,
**so that** I can persist complete orders linking clients to products with quantities and prices, tracking their progression through the business workflow.

#### Acceptance Criteria:

1. StatutCommande enum created in enums package with values: COMMANDEE, RECUPEREE, PAYEE with French display labels ("Commandée", "Récupérée", "Payée")
2. Commande.java entity created with fields: id (Long), clientId (Long, @ManyToOne to Client), dateCommande (LocalDate, @NotNull), statut (StatutCommande, @Enumerated(STRING), default COMMANDEE), notes (String, @Column(length=1000)), createdAt (LocalDateTime), updatedAt (LocalDateTime)
3. LigneCommande.java entity created with fields: id (Long), commandeId (Long, @ManyToOne to Commande), produitId (Long, @ManyToOne to Produit), quantite (Integer, @Min(1)), prixUnitaire (BigDecimal, price at time of order for historical accuracy)
4. Relation: Commande has @OneToMany(cascade=ALL, orphanRemoval=true) List<LigneCommande> lignes, LigneCommande has @ManyToOne Commande order and @ManyToOne Produit produit
5. Relation: Client has @OneToMany List<Commande> orders (update Client.java from Epic 1)
6. CommandeRepository interface extending JpaRepository<Commande, Long> with methods: findByClientIdOrderByDateCommandeDesc(Long clientId), findByStatut(StatutCommande statut), findByDateCommandeBetween(LocalDate start, LocalDate end) for filtering
7. LigneCommandeRepository interface extending JpaRepository<LigneCommande, Long> (standard CRUD sufficient)
8. Hibernate creates orders and lignes_commande tables with proper foreign keys and cascade rules
9. Unit tests: create order with 2-3 lignes, verify cascade save (lignes auto-saved with order), verify orphan removal (delete ligne removes it), verify status enum persists as string, verify client.orders bidirectional relation works

### Story 2.3: CommandeService & ProduitService with Business Logic

**As a** developer,
**I want** to create service layers for Commande and Produit with business logic for order management and pricing,
**so that** controllers can rely on clean APIs for creating orders with automatic price lookup and status transitions.

#### Acceptance Criteria:

1. ProduitService.java created with methods: findAll() returns all products ordered by name, findById(Long id) returns Optional<Produit>, getCurrentYearTarif(Long produitId) returns current year price or throws PriceNotFoundException, updateTarif(Long produitId, Integer annee, BigDecimal prix) saves/updates tarif for product/year
2. CommandeService.java created with methods: findAll(), findById(Long id), findByClientId(Long clientId), findByStatut(StatutCommande statut), create(Commande order) validates and saves, updateStatut(Long commandeId, StatutCommande newStatut) transitions status with validation, calculateTotal(Long commandeId) sums ligne.quantite * ligne.prixUnitaire for all lignes
3. Business rules in CommandeService.create(): auto-populate dateCommande with today if null, set statut to COMMANDEE if null, for each LigneCommande auto-fetch prixUnitaire from ProduitService.getCurrentYearTarif() if not provided, validate at least one ligne exists
4. Business rules in updateStatut(): only allow forward transitions (COMMANDEE→RECUPEREE→PAYEE), throw InvalidStatusTransitionException if invalid (e.g., PAYEE→COMMANDEE), log status change with timestamp
5. Unit tests with mocked repositories: verify create() auto-fills prices from current year tarifs, verify calculateTotal() sums correctly, verify updateStatut() enforces transition rules and throws exception for invalid transitions, verify findByClientId() returns orders sorted by date descending
6. Integration test: create real order with 2 lignes, verify prices auto-populated, calculate total, transition through all statuses successfully

### Story 2.4: Product Catalog & Tarif Management UI

**As a** beekeeper (parent user),
**I want** to view and manage my product catalog with prices for each year,
**so that** I can prepare pricing for upcoming seasons and ensure accurate pricing on orders.

#### Acceptance Criteria:

1. ProduitController created with GET /produits endpoint returning "produits/list" view with all products and their current year tarifs
2. templates/produits/list.html created displaying: page title "Catalogue Produits", table with columns: Nom produit, Type (if miel), Unité, Prix 2024, Prix 2025, Actions (Modifier prix)
3. Current year (2026) prices highlighted or bolded to distinguish from future years
4. "Modifier Prix" button per product opens inline form or modal to edit tarif for selected year (dropdown: 2024, 2025, 2026, 2027), input for price (€), save updates TarifRepository
5. POST /produits/{id}/tarif endpoint accepts produitId, annee, prix, calls ProduitService.updateTarif(), redirects back to /produits with success message "Prix mis à jour pour {produit} en {annee}"
6. Products grouped or color-coded by type (Miel Toutes Fleurs, Miel Forêt, etc.) for readability
7. Empty state if no products: "Aucun produit dans le catalogue" (should not occur with seed data)
8. Price formatting: display as "12,50 €" (French format with comma decimal separator)
9. Navigation link "Produits" added to sidebar menu (update layout.html from Epic 1)
10. Responsive: table scrolls horizontally on mobile, readable on desktop

### Story 2.5: Order List with Filters and Status Badges

**As a** beekeeper (parent user),
**I want** to view a list of all orders with filtering by year and status,
**so that** I can quickly find orders that need attention (e.g., all "Commandée" orders to prepare) and review historical orders.

#### Acceptance Criteria:

1. CommandeController created with GET /orders endpoint returning "orders/list" view with all orders, model includes: list of orders, current year, list of years (distinct from orders.dateCommande for filter dropdown), list of statuts (enum values)
2. templates/orders/list.html created displaying: page title "Commandes", filter bar with dropdowns: Année (default: current year), Statut (default: Tous), "Filtrer" button, "Nouvelle Commande" button (green, prominent, top-right)
3. Orders table with columns: N° (id), Client (nom), Date order (DD/MM/YYYY), Statut (badge colored: COMMANDEE=blue, RECUPEREE=orange, PAYEE=green), Montant total (calculated via CommandeService.calculateTotal()), Actions (Voir)
4. Filtering: GET /orders?annee=2024&statut=COMMANDEE filters results server-side using CommandeService methods, results update in same view
5. Status badges styled with Bootstrap badge component and appropriate colors for quick visual scanning
6. Orders sorted by date descending (most recent first) within filtered results
7. Empty state if no orders match filters: "Aucune order trouvée pour les filtres sélectionnés"
8. "Tous" option in statut filter shows all statuses
9. Navigation link "Commandes" added to sidebar menu (update layout.html)
10. Montant total formatted as French currency "123,45 €"

### Story 2.6: Create Order Form with Dynamic Product Lines

**As a** beekeeper (parent user),
**I want** to create a new order by selecting a client and adding product lines with quantities,
**so that** I can record customer orders quickly as they come in, replacing my paper notebook workflow.

#### Acceptance Criteria:

1. GET /orders/nouvelle returns "orders/form" view with: empty Commande object, list of all active clients (for dropdown), list of all products with current year prices (for product selection)
2. templates/orders/form.html created with: title "Nouvelle Commande", client selection dropdown (searchable/autocomplete preferred, or simple select), date order field (datepicker, default today), notes textarea, dynamic product lines section
3. Product lines: initial empty row with: Produit dropdown (showing "Miel 500g Toutes Fleurs - 12,50 €"), Quantité input (number, min=1), Prix unitaire (readonly, auto-filled from product selection), Sous-total (readonly, calculated quantite × prix), "Supprimer ligne" button (icon), "+ Ajouter un produit" button to add new line
4. JavaScript (vanilla) handles: add/remove product lines dynamically, auto-fill prix unitaire when product selected, calculate and display sous-total per line, calculate and display total order at bottom (sum of all sous-totals)
5. POST /orders endpoint accepts: clientId, dateCommande, notes, List<LigneCommandeDto> (produitId, quantite, prixUnitaire), calls CommandeService.create(), redirects to /orders/{id} (detail view) with success message "Commande créée avec succès"
6. Validation: at least one product line required, client selection required, quantite must be ≥1, prix unitaire auto-populated but editable (for special discounts)
7. Form buttons: "Enregistrer" (green), "Annuler" (grey, back to /orders)
8. Price override: allow user to manually edit prix unitaire if needed (e.g., discount), with visual indicator it differs from catalog price
9. Mobile responsive: form usable on tablet (future consideration), stacks vertically
10. Minimum 1 product line pre-filled on load (empty dropdown) for UX clarity

### Story 2.7: Order Detail View with Status Transitions

**As a** beekeeper (parent user),
**I want** to view order details and transition it through workflow statuses (Commandée → Récupérée → Payée),
**so that** I can track the lifecycle of each order from creation to payment completion.

#### Acceptance Criteria:

1. GET /orders/{id} returns "orders/detail" view with Commande object including: client info, dateCommande, current statut, notes, list of lignes (produit, quantite, prix, sous-total), total calculated
2. templates/orders/detail.html created displaying: page title "Commande #{id}", client info section (name, phone with link to client detail), date and current status badge (large, prominent), notes if present, table of ordered products (columns: Produit, Quantité, Prix unitaire, Sous-total), Total row (bold, larger font)
3. Status transition buttons displayed based on current status: if COMMANDEE show "Marquer comme Récupérée" (orange button), if RECUPEREE show "Marquer comme Payée" (green button), if PAYEE show "Payée ✓" (disabled green badge, no button)
4. POST /orders/{id}/statut endpoint accepts newStatut parameter, calls CommandeService.updateStatut(), redirects back to /orders/{id} with success message "Statut mis à jour: {newStatut}"
5. Status transition validation: invalid transitions display error message "Transition invalide" and do not update status
6. "Modifier" button to edit order (link to /orders/{id}/edit, grey button) - edit form similar to create form but pre-populated
7. "Retour à la liste" link to /orders
8. Future actions section (placeholder): "Imprimer bon de livraison" button disabled with tooltip "Disponible prochainement"
9. Timestamps: display "Créée le {date}", "Modifiée le {date}" at bottom
10. Responsive: readable on mobile/tablet, buttons stack vertically if needed

### Story 2.8: Client Detail - Display Order History

**As a** beekeeper (parent user),
**I want** to see a client's complete order history on their detail page,
**so that** I can understand their purchasing patterns and reference past orders when they contact me.

#### Acceptance Criteria:

1. Update ClientController GET /clients/{id} endpoint to include client.orders in model (fetched via Client entity @OneToMany relation or CommandeService.findByClientId())
2. Update templates/clients/detail.html to replace placeholder "Historique des orders (à venir)" with actual order list table
3. Order history table displays: Date order, Statut (badge), Montant total, Actions (Voir link to /orders/{id})
4. Orders sorted by date descending (most recent first)
5. Empty state if client has no orders: "Aucune order pour ce client" with encouragement "Créez la première order"
6. "Nouvelle order pour ce client" button (previously disabled in Story 1.6) now active, links to /orders/nouvelle?clientId={id} (pre-selects client in form)
7. Summary stats above table: Total orders ({count}), Total dépensé ({sum of all PAYEE orders}), Dernière order le {date}
8. Clicking order row or "Voir" navigates to order detail page
9. Table responsive: scrolls horizontally on mobile, stacks on very small screens
10. Performance: limit display to last 50 orders with "Voir toutes les orders" link if more exist (pagination or "show more")

---

## Epic 3: Label Generation (Killer Feature)

**Epic Goal:** Développer la fonctionnalité de génération automatique d'étiquettes réglementaires pour pots de miel au format PDF, éliminant le processus manuel chronophage actuel (15 minutes pour 10 étiquettes réduit à moins de 2 minutes).

### Story 3.1: PDF Service Foundation with Apache PDFBox

**As a** developer,
**I want** to set up Apache PDFBox library and create a PdfService foundation for generating PDF documents,
**so that** I have the technical infrastructure to generate label sheets with proper page layout and text rendering.

#### Acceptance Criteria:

1. Apache PDFBox dependency added to pom.xml (version 3.0.1 or latest stable 3.x)
2. PdfService.java created in service package with @Service annotation
3. Basic test method generateTestPdf() creates a simple PDF with text "HoneyAI Test PDF" and saves to ./test-output.pdf
4. PDF generation verified: file created, opens without errors in Adobe Reader/browser, displays text correctly
5. Helper methods created in PdfService: createDocument() returns PDDocument, createPage(PDRectangle pageSize) returns PDPage with A4 size, addTextToPage(PDPage page, String text, float x, float y, float fontSize) writes text at coordinates
6. Font loading: standard PDType1Font (Helvetica or Times) configured, or embedded TrueType font if needed for special characters
7. Unit test: generate PDF with multiple text blocks at different positions, verify file size >0 and no exceptions thrown
8. Error handling: try-catch around PDF operations with custom PdfGenerationException thrown if errors occur
9. Logging: log PDF generation start/completion with INFO level
10. Resources cleanup: PDDocument properly closed after generation (try-with-resources or finally block)

### Story 3.2: Etiquette Configuration & Data Model

**As a** developer,
**I want** to create a configuration model for label parameters and exploitation information,
**so that** the PDF service can access label dimensions, exploitation details (SIRET, address), and layout settings without hardcoding values.

#### Acceptance Criteria:

1. EtiquetteConfig.java created as @ConfigurationProperties(prefix = "honeyai.etiquettes") class with fields: siret (String), nomApiculteur (String), adresse (String), telephone (String), dluoDureeJours (Integer, default 730), labelWidthMm (Float, default 60), labelHeightMm (Float, default 40), labelsPerRow (Integer, default 3), labelsPerColumn (Integer, default 7)
2. application.yml updated with section honeyai.etiquettes containing: siret: "XXXXXXXXXXX", nomApiculteur: "Exploitation Familiale", adresse: "123 Rue de la Ruche, 12345 Village", telephone: "06 XX XX XX XX", dluoDureeJours: 730, labelWidthMm: 60.0, labelHeightMm: 40.0, labelsPerRow: 3, labelsPerColumn: 7
3. EtiquetteRequest DTO created with fields: typeMiel (TypeMiel enum), formatPot (FormatPot enum: POT_500G, POT_1KG), dateRecolte (LocalDate), quantite (Integer @Min(1)), with validation annotations
4. FormatPot enum created with POT_500G("500g", 0.5), POT_1KG("1kg", 1.0) containing display label and weight in kg
5. EtiquetteData model class created with all computed fields for one label: typeMiel (String display), formatPot (String display), dateRecolte (String formatted), dluo (LocalDate computed), numeroLot (String generated), nomApiculteur, adresse, siret, telephone, prixUnitaire (BigDecimal from current year tarif)
6. Configuration validated at startup: @Validated annotation on EtiquetteConfig, application fails to start if required fields missing
7. Unit test: load EtiquetteConfig from test application.yml, verify all fields populated correctly
8. PdfService constructor-injected with EtiquetteConfig for accessing exploitation details

### Story 3.3: DLUO Calculation & Lot Number Generation Logic

**As a** developer,
**I want** to implement business logic for calculating DLUO (Best Before Date) and generating unique batch numbers,
**so that** labels display correct regulatory information automatically without manual calculation errors.

#### Acceptance Criteria:

1. EtiquetteService.java created with method calculateDluo(LocalDate dateRecolte, Integer dureeDays) returns LocalDate = dateRecolte.plusDays(dureeDays), formatted as MM/YYYY (month and year only per French regulation)
2. DLUO calculation uses dluoDureeJours from config (default 730 days = 2 years exactly)
3. Method generateNumeroLot(TypeMiel typeMiel, LocalDate dateRecolte) generates format: "YYYY-{TYPE}-NNN" where YYYY = harvest year, TYPE = miel type abbreviation (TF=Toutes Fleurs, FOR=Forêt, CHA=Châtaignier), NNN = sequential number per year/type
4. Lot number sequencing: query database (new table lots_etiquettes or counter in config) to get next sequential number for year+type combination, increment and store
5. LotsEtiquettes entity (optional simple approach): fields id, annee (Integer), typeMiel (String), dernierNumero (Integer), with unique constraint on (annee, typeMiel)
6. LotsEtiquettesRepository with method: findByAnneeAndTypeMiel(Integer annee, String typeMiel)
7. EtiquetteService.generateNumeroLot() logic: fetch or create LotsEtiquettes for current year+type, increment dernierNumero, save, return formatted lot number
8. Unit tests: calculateDluo(2024-06-15, 730) returns 2026-06-15 formatted "06/2026", generateNumeroLot(TOUTES_FLEURS, 2024-06-15) returns "2024-TF-001", second call returns "2024-TF-002", different type returns "2024-FOR-001"
9. Edge case tests: DLUO calculation across year boundaries, lot number rollover to new year (2025-TF-001 after 2024-TF-999)
10. DLUO display format: French regulation requires "À consommer de préférence avant fin: MM/YYYY" (month/year, not full date)

### Story 3.4: Single Label PDF Layout & Rendering

**As a** developer,
**I want** to create the PDF layout logic for rendering a single honey label with all regulatory information,
**so that** I can generate compliant labels with proper text positioning, sizing, and formatting.

#### Acceptance Criteria:

1. PdfService method renderLabel(PDPage page, EtiquetteData data, float x, float y, float widthMm, float heightMm) renders one label at specified position
2. Label layout structure: Border rectangle (1pt black line), Logo/icon area (optional, top 10%), Product name section (type miel + format, bold, 20% height), Mandatory info section (nom, adresse, SIRET, tel, 40% height), DLUO line (15% height), Lot number + price (bottom 15% height)
3. Font sizes: product name 12pt bold, mandatory info 8pt regular, DLUO 9pt, lot/price 8pt - all adjusted to fit within label dimensions
4. Text content per label: "Miel de {type}" or "Miel Toutes Fleurs" (line 1), "Poids net: {formatPot}" (line 2), "{nomApiculteur}" (line 3), "{adresse}" (line 4), "SIRET: {siret}" (line 5), "Tél: {telephone}" (line 6), "À consommer avant fin: {dluo MM/YYYY}" (line 7), "Lot: {numeroLot}" (line 8), "Prix: {prix} €" (line 9)
5. Coordinate conversion: millimeters to PDF points (1mm = 2.83465 points), position calculations relative to page margins
6. Text alignment: centered for product name, left-aligned for contact info, right-aligned for price
7. Text wrapping: long addresses truncated or wrapped to fit label height
8. Unit test (visual verification): generate single label PDF with mock data, manually verify layout, spacing, readability
9. Border and spacing: 2mm padding inside label border, 1mm margin between text lines
10. Special characters: ensure French accents (é, è, ô, etc.) render correctly in PDF fonts

### Story 3.5: Multi-Label Sheet Generation

**As a** developer,
**I want** to generate a full A4 sheet with multiple labels arranged in a grid,
**so that** users can print labels efficiently on standard label sheets (e.g., 3 columns × 7 rows).

#### Acceptance Criteria:

1. PdfService method generateEtiquetteSheet(EtiquetteRequest request, EtiquetteConfig config) returns byte[] of PDF file
2. Sheet layout: A4 page (210mm × 297mm), labels arranged in grid based on config (labelsPerRow × labelsPerColumn, default 3×7 = 21 labels per page)
3. Label positioning: calculate x,y coordinates for each label in grid, accounting for margins (10mm top/left/right, 15mm bottom for printer safety)
4. Spacing between labels: 2mm horizontal gap, 2mm vertical gap between adjacent labels
5. Repeat label data: same EtiquetteData used for all labels on sheet (user requested N copies, generate full sheets + partial last sheet)
6. Multi-page support: if quantite > labels per sheet (e.g., 50 labels requested, 21 per sheet), generate 3 pages (21 + 21 + 8)
7. Partial sheet: last page contains only remaining labels (8 in example), positioned in grid starting top-left
8. Method signature: generateEtiquettePdf(EtiquetteRequest request) returns ResponseEntity<byte[]> with PDF content type and filename header
9. Integration test: request 25 labels, verify PDF has 2 pages, page 1 has 21 labels, page 2 has 4 labels, manually verify print preview looks correct
10. Performance: generation of 100-label PDF (5 pages) completes in <5 seconds per NFR3

### Story 3.6: Label Generation Form UI

**As a** beekeeper (parent user),
**I want** to fill a simple form to generate honey labels by selecting miel type, pot format, harvest date, and quantity,
**so that** I can quickly create printable labels without manual data entry for each label.

#### Acceptance Criteria:

1. EtiquetteController created with GET /etiquettes endpoint returning "etiquettes/form" view with empty EtiquetteRequest model
2. templates/etiquettes/form.html created with: page title "Générer des Étiquettes", form fields: Type de miel (dropdown: Toutes Fleurs, Forêt, Châtaignier), Format pot (radio buttons: 500g, 1kg with large clickable labels), Date de récolte (datepicker, default today), Quantité d'étiquettes (number input, min=1, default=10, max=500)
3. Form layout: simple, vertical, large labels (18px), generous spacing, fields grouped logically
4. Visual preview section (optional): placeholder image or text showing "Aperçu d'une étiquette" with sample label mockup (static for now)
5. Submit button: "Générer PDF" (large, green, prominent, icon: download), positioned prominently at bottom
6. Help text: below quantity field "Nombre d'étiquettes à imprimer (21 par feuille A4)"
7. Navigation: "Étiquettes" link added to sidebar menu (update layout.html)
8. Form responsive: usable on desktop primarily, stacks on tablet
9. Validation feedback: required field indicators (*), client-side validation (HTML5 required, min/max), friendly error messages if validation fails
10. Accessibility: labels properly associated with inputs, keyboard navigation works, WCAG AA contrast

### Story 3.7: PDF Download Endpoint & File Response

**As a** beekeeper (parent user),
**I want** to download the generated PDF file immediately after submitting the form,
**so that** I can open it and print the labels on my printer without additional steps.

#### Acceptance Criteria:

1. POST /etiquettes/generer endpoint in EtiquetteController accepts @Valid EtiquetteRequest, returns ResponseEntity<byte[]> with PDF content
2. Endpoint logic: call EtiquetteService.prepareEtiquetteData(request) to compute DLUO, lot number, fetch price, then call PdfService.generateEtiquettePdf(data, config, request.quantite)
3. Response headers: Content-Type: application/pdf, Content-Disposition: attachment; filename="etiquettes-{type}-{date}-{lot}.pdf" (e.g., "etiquettes-toutes-fleurs-2024-06-15-2024-TF-001.pdf")
4. Browser behavior: download prompt appears (or PDF opens in browser tab depending on browser settings)
5. Error handling: if PDF generation fails, return to form with error message "Erreur lors de la génération du PDF. Veuillez réessayer."
6. Success flow: form submit → loading indicator (optional spinner) → PDF downloads → form reset with success message "PDF généré avec succès ({quantite} étiquettes)"
7. Validation: if form validation fails (e.g., quantité <1), return to form with field-level error messages
8. Price lookup: fetch current year tarif for selected product (type + format), if not found use default or display warning
9. Logging: log each PDF generation with parameters (type, format, quantite, user/timestamp) for audit trail
10. Performance: PDF generation completes in <5 seconds for up to 100 labels per NFR3

### Story 3.8: Label Generation History & Lot Number Tracking

**As a** beekeeper (parent user),
**I want** to see a history of recently generated label batches with their lot numbers,
**so that** I can reference past generations and understand which lot numbers have been used for traceability.

#### Acceptance Criteria:

1. HistoriqueEtiquettes entity created with fields: id, typeMiel, formatPot, dateRecolte, dluo, numeroLot, quantite, dateGeneration (timestamp), prixUnitaire
2. HistoriqueEtiquettesRepository with method: findTop20ByOrderByDateGenerationDesc() for recent history
3. EtiquetteService saves history record after each successful PDF generation
4. GET /etiquettes/historique endpoint returns "etiquettes/historique" view with list of recent generations
5. templates/etiquettes/historique.html displaying: page title "Historique des Étiquettes", table with columns: Date génération, Type miel, Format, Date récolte, DLUO, Lot, Quantité, Actions (Re-générer)
6. "Re-générer" button: links back to /etiquettes form pre-filled with same parameters (type, format, date, quantite) allowing quick regeneration with new lot number
7. Navigation: link "Historique" added below "Étiquettes" in sidebar or as tab on /etiquettes page
8. Empty state: "Aucune étiquette générée pour le moment" if no history
9. History limited to last 20-50 generations (pagination not needed for MVP, just recent history)
10. Display formatted dates (DD/MM/YYYY HH:mm) and French labels for all fields

---

## Epic 4: Financial Dashboard & Purchases

**Epic Goal:** Créer le système de suivi financier de l'exploitation apicole en développant un tableau de bord visuel présentant les indicateurs clés (chiffre d'affaires, dépenses, bénéfice brut, top produits vendus) et un module de gestion des achats de fournitures.

### Story 4.1: Achat Entity & Repository for Purchase Tracking

**As a** developer,
**I want** to create an Achat JPA entity and repository for tracking supply purchases,
**so that** I can persist and retrieve expense data needed for financial calculations and reporting.

#### Acceptance Criteria:

1. CategorieAchat enum created in enums package with values: CIRE, POTS, COUVERCLES, NOURRISSEMENT, AUTRE with French display labels
2. Achat.java entity created in model package with fields: id (Long), dateAchat (LocalDate, @NotNull), designation (String, @NotBlank), montant (BigDecimal, @NotNull @Positive), categorie (CategorieAchat, @Enumerated(STRING)), notes (String), createdAt (LocalDateTime)
3. Achat entity annotated with @Entity, @Table(name="achats"), @Data (Lombok)
4. AchatRepository interface created extending JpaRepository<Achat, Long> with custom query methods: findByDateAchatBetween(LocalDate start, LocalDate end), findByCategorie(CategorieAchat categorie), findAllByOrderByDateAchatDesc()
5. Hibernate creates achats table automatically in SQLite
6. Unit test for AchatRepository: save achat, retrieve by id, filter by date range, filter by category
7. At least 5 test achats inserted successfully covering different categories and date ranges
8. BigDecimal precision: 2 decimal places for montant (euros and cents)

### Story 4.2: AchatService with Expense Calculation Logic

**As a** developer,
**I want** to create an AchatService layer with business logic for purchase management and expense calculations,
**so that** controllers can retrieve purchases and calculate total expenses for specified periods.

#### Acceptance Criteria:

1. AchatService.java created in service package with @Service annotation
2. Methods implemented: findAll(), findById(Long id), save(Achat achat), delete(Long id), findByPeriod(LocalDate start, LocalDate end), findByCategorie(CategorieAchat categorie)
3. Calculation method: calculateTotalDepenses(LocalDate start, LocalDate end) sums montant of all achats in period
4. Calculation method: calculateDepensesByCategorie(LocalDate start, LocalDate end) returns Map<CategorieAchat, BigDecimal>
5. Validation enforced: montant must be positive, designation cannot be blank
6. Unit tests with mocked repository: verify calculateTotalDepenses() sums correctly, verify calculateDepensesByCategorie() groups correctly
7. Service methods transactional where needed

### Story 4.3: DashboardService with Financial Aggregations

**As a** developer,
**I want** to create a DashboardService that aggregates financial data from orders and purchases,
**so that** I can provide dashboard metrics (CA, expenses, profit, top products) efficiently.

#### Acceptance Criteria:

1. DashboardService.java created with constructor injection of CommandeRepository, AchatRepository
2. Method calculateChiffreAffaires(LocalDate start, LocalDate end) returns BigDecimal: sum of amounts from orders with statut=PAYEE in date range
3. Method calculateTotalDepenses(LocalDate start, LocalDate end) delegates to AchatService
4. Method calculateBenefice(LocalDate start, LocalDate end) returns CA - dépenses
5. Method getTopProduits(LocalDate start, LocalDate end, int limit) returns List<TopProduitDto>: aggregates from paid orders, groups by product, sorts by quantity
6. TopProduitDto class with fields: produitNom, typeMiel, quantiteTotale, chiffreAffaires
7. Custom query in CommandeRepository to calculate CA for period
8. Unit tests: verify calculateChiffreAffaires() only counts PAYEE orders, verify calculateBenefice() subtracts correctly
9. All monetary values returned as BigDecimal with 2 decimal places

### Story 4.4: Purchase List & Create Form

**As a** beekeeper (parent user),
**I want** to view a chronological list of my supply purchases and add new expenses,
**so that** I can track where my money is going and maintain accurate records.

#### Acceptance Criteria:

1. AchatController created with GET /achats endpoint returning "achats/list" view
2. templates/achats/list.html created with filter bar (year, category), purchase table, total displayed
3. Quick-add form at top with fields: Date, Désignation, Montant, Catégorie, Notes
4. POST /achats endpoint validates and saves
5. Empty state: "Aucun achat enregistré"
6. Category badges color-coded
7. Navigation: "Achats" link in sidebar

### Story 4.5: Purchase Edit & Delete

**As a** beekeeper (parent user),
**I want** to edit or delete a purchase record if I made a mistake,
**so that** my expense tracking remains accurate.

#### Acceptance Criteria:

1. GET /achats/{id}/edit returns edit form
2. POST /achats/{id} updates existing achat
3. DELETE /achats/{id} hard deletes after confirmation
4. Confirmation modal for delete
5. Success flash messages

### Story 4.6: Financial Dashboard Main View

**As a** beekeeper (parent user),
**I want** to see a visual dashboard with my key financial metrics at a glance,
**so that** I can quickly understand my business performance.

#### Acceptance Criteria:

1. HomeController GET / or DashboardController GET /dashboard returns dashboard view
2. templates/dashboard.html with period selector, metric cards (CA, Dépenses, Bénéfice, Commandes Payées)
3. Cards display large number, label, icon, optional comparison
4. Period filtering: current year default, filters apply to all metrics
5. Responsive: cards stack on mobile, 2×2 grid on desktop
6. Navigation: "Tableau de bord" first in sidebar
7. Performance: loads in <1 second

### Story 4.7: Top Products Widget & Simple Charts

**As a** beekeeper (parent user),
**I want** to see my top-selling products on the dashboard,
**so that** I can identify which honey types are most popular.

#### Acceptance Criteria:

1. Dashboard updated with "Top 3 Produits" section
2. Ranked list showing product, quantity sold, revenue
3. Data from DashboardService.getTopProduits()
4. Optional: Chart.js bar chart or simple CSS bars
5. Empty state if no sales
6. Period filter applies

### Story 4.8: Expense Breakdown by Category

**As a** beekeeper (parent user),
**I want** to see a breakdown of my expenses by category,
**so that** I can understand where most of my money is spent.

#### Acceptance Criteria:

1. Dashboard updated with "Répartition des Dépenses" section
2. List or chart showing each category with amount and percentage
3. Data from DashboardService or AchatService
4. Optional: pie/donut chart with Chart.js or CSS progress bars
5. Categories sorted by amount descending
6. Empty state if no expenses

---

## Epic 5: Backup, Packaging & Production Readiness

**Epic Goal:** Finaliser HoneyAI pour la production en implémentant le système de backup automatique quotidien, créer le packaging Windows .exe avec launch4j, polir l'UX avec messages de confirmation et gestion d'erreurs conviviale, et préparer la distribution complète pour le déploiement.

### Story 5.1: Backup Service with Scheduled Daily Backups

**As a** developer,
**I want** to implement an automated daily backup service that copies the SQLite database file,
**so that** user data is protected against corruption or hardware failure.

#### Acceptance Criteria:

1. BackupService.java created with method performBackup()
2. Backup copies ./data/honeyai.db to ./backups/honeyai-backup-YYYY-MM-DD-HHmmss.db
3. Backup directory ./backups/ created automatically if not exists
4. @Scheduled annotation with cron "0 0 2 * * ?" (daily at 2:00 AM)
5. @EnableScheduling on main application class
6. Backup retention: delete files older than 30 days
7. Logging: log backup start/success/failure
8. Error handling: log error but don't crash app
9. Unit test: verify backup file created with correct name
10. Integration test: trigger backup, verify file is valid SQLite database

### Story 5.2: Manual Backup Export UI

**As a** beekeeper (parent user),
**I want** to manually export a backup of my data,
**so that** I have an extra safety copy I control.

#### Acceptance Criteria:

1. BackupController with GET /backup endpoint
2. templates/backup/manage.html with info, recent backups list, manual backup button
3. "Créer une sauvegarde maintenant" button triggers POST /backup/manual
4. Download recent backup files
5. "Exporter la base de données" downloads current honeyai.db
6. Success messages, help text about USB backup
7. Navigation: "Sauvegarde" link in sidebar

### Story 5.3: Global Exception Handler & User-Friendly Error Pages

**As a** developer,
**I want** to implement a global exception handler with friendly error pages,
**so that** technical errors don't confuse non-technical users.

#### Acceptance Criteria:

1. GlobalExceptionHandler.java with @ControllerAdvice
2. @ExceptionHandler methods for common exceptions
3. Custom error pages: 404.html, 500.html, error.html
4. Error pages with friendly messages, no stack traces, "Retour à l'accueil" button
5. Validation errors return to form with field-level messages
6. All exceptions logged with stack trace for debugging
7. Test scenarios: 404, validation error, simulated 500

### Story 5.4: Confirmation Messages & Toast Notifications

**As a** beekeeper (parent user),
**I want** to see clear confirmation messages after important actions,
**so that** I feel confident my actions succeeded.

#### Acceptance Criteria:

1. Flash message system using RedirectAttributes
2. Layout template displays flash messages at top
3. Success messages for all CRUD operations
4. Messages auto-dismiss after 5 seconds
5. Encouraging tone with icons
6. Consistent across all features
7. Accessibility: role="alert", dismissible with keyboard

### Story 5.5: Application Launcher Script & Icon

**As a** developer,
**I want** to create a Windows launcher script and icon,
**so that** the application starts with a double-click.

#### Acceptance Criteria:

1. Icon file launcher/icon.ico with honey/bee theme
2. Launcher script lancer-honeyai.bat starts app and opens browser
3. README-INSTALLATION.txt with instructions
4. Test: double-click .bat, no console window, browser opens after 4s
5. Error handling if Java not found
6. Documentation with screenshots

### Story 5.6: launch4j Configuration & .exe Wrapper

**As a** developer,
**I want** to configure launch4j to create a Windows .exe wrapper,
**so that** the application launches like a native Windows app.

#### Acceptance Criteria:

1. launch4j config file launcher/honeyai-launch4j.xml
2. Splash screen configured: splash.bmp with "HoneyAI" and "Démarrage..."
3. JVM options: Xms128m, Xmx512m, UTF-8 encoding
4. Build script to generate .exe
5. Test .exe: splash appears, browser opens, app functions
6. Optional bundled JRE in dist/jre/
7. Error if JRE not found
8. Distribution package structure
9. Tested on multiple Windows machines

### Story 5.7: Final UX Polish & Accessibility Review

**As a** beekeeper (parent user),
**I want** the application interface to be polished and consistent,
**so that** I feel confident using it.

#### Acceptance Criteria:

1. Font size audit: body 16px min, headings 20px min, WCAG AA compliance
2. Button size audit: 44×44px min, proper spacing
3. Consistent terminology throughout
4. Navigation consistency: sidebar works everywhere, active page highlighted
5. Form labels: visible above fields, required marked with *
6. Loading indicators for operations >1s
7. Empty states on all lists
8. Color consistency with honey palette
9. Responsive check at 1280×720 and 1920×1080
10. Accessibility checklist: keyboard nav, screen reader, no color-only info

### Story 5.8: Production Configuration & Deployment Checklist

**As a** developer,
**I want** to finalize production configuration and create deployment checklist,
**so that** the application is properly configured for end-user deployment.

#### Acceptance Criteria:

1. application.yml production profile: cache=true, logging INFO, real SIRET/address
2. Configuration validation: verify all config has real data
3. Database path relative ./data/honeyai.db
4. Deployment checklist: docs/DEPLOYMENT-CHECKLIST.md
5. User manual: docs/GUIDE-UTILISATEUR.md with screenshots
6. Troubleshooting guide: docs/DEPANNAGE.md
7. Version number in pom.xml (1.0.0), displayed in footer
8. Release notes: docs/RELEASE-NOTES-v1.0.md
9. Support contact in app footer
10. Final smoke test on clean machine

---

## Checklist Results Report

**Validation Date:** 2026-01-17
**Validator:** Mary - Business Analyst
**Checklist:** PM Requirements Checklist (BMAD™ Core)

### Executive Summary

**Overall Assessment:**
- **PRD Completeness:** 95% ✅
- **MVP Scope Appropriateness:** Just Right ✅
- **Readiness for Architecture Phase:** **READY** ✅
- **Overall Grade:** A (Excellent)

### Category Status Summary

| Category | Status | Completion | Critical Issues |
|----------|--------|------------|-----------------|
| Problem Definition & Context | **PASS** | 100% | None |
| MVP Scope Definition | **PASS** | 100% | None |
| User Experience Requirements | **PASS** | 95% | Missing: Visual flow diagrams (non-blocking) |
| Functional Requirements | **PASS** | 100% | None |
| Non-Functional Requirements | **PASS** | 100% | None |
| Epic & Story Structure | **PASS** | 100% | None |
| Technical Guidance | **PASS** | 100% | None |
| Cross-Functional Requirements | **PASS** | 100% | None |
| Clarity & Communication | **PASS** | 90% | Missing: Visual diagrams (non-blocking) |

**Result:** 9/9 Categories PASS - Zero blockers identified

### Key Findings

**Strengths:**
- Exceptionally detailed requirements: 18 FR + 17 NFR covering all aspects
- Outstanding story quality: 40 stories with 280+ acceptance criteria total
- Clear technical direction: Spring Boot stack fully specified with rationale
- Strong user focus: Non-technical users (50-65 years) considerations throughout
- Excellent scope discipline: Clear MVP boundaries, Phase 2 features deferred

**Minor Improvements (Non-blocking):**
- Could benefit from visual flow diagrams for key workflows (UX Expert will create)
- Time estimate 68-91h exceeds initial 40-45h target (requires stakeholder discussion)

### Recommendations

1. **✅ PROCEED TO ARCHITECTURE PHASE** - No blockers, architect has all needed information
2. **Discuss Time Estimate** - Realistic 68-91h vs. original 40-45h target (quality vs. speed trade-off)
3. **UX Expert to create flow diagrams** - Visual user journeys for complex workflows
4. **Architect to validate technical assumptions early** - Test SQLite + Hibernate integration in Epic 1

### Final Decision

**✅ READY FOR ARCHITECT**

The PRD and epics are comprehensive, properly structured, and ready for architectural design. Zero blockers identified. Confidence level: Very High.

---

## Next Steps

### UX Expert Prompt

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

### Architect Prompt

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
      - Indexes needed: clients.nom, orders.date_commande, orders.statut

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
        - GET /orders, POST /orders, GET /orders/{id}, POST /orders/{id}/statut
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
