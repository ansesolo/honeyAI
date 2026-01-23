# Epic 4: Financial Dashboard & Purchases

**Epic Goal:** Créer le système de suivi financier de l'exploitation apicole en développant un tableau de bord visuel présentant les indicateurs clés (chiffre d'affaires, dépenses, bénéfice brut, top produits vendus) et un module de gestion des achats de fournitures.

## Story 4.1: Achat Entity & Repository for Purchase Tracking

**As a** developer,
**I want** to create an Achat JPA entity and repository for tracking supply purchases,
**so that** I can persist and retrieve expense data needed for financial calculations and reporting.

### Acceptance Criteria:

1. CategorieAchat enum created in enums package with values: CIRE, POTS, COUVERCLES, NOURRISSEMENT, AUTRE with French display labels
2. Achat.java entity created in model package with fields: id (Long), dateAchat (LocalDate, @NotNull), designation (String, @NotBlank), montant (BigDecimal, @NotNull @Positive), categorie (CategorieAchat, @Enumerated(STRING)), notes (String), createdAt (LocalDateTime)
3. Achat entity annotated with @Entity, @Table(name="achats"), @Data (Lombok)
4. AchatRepository interface created extending JpaRepository<Achat, Long> with custom query methods: findByDateAchatBetween(LocalDate start, LocalDate end), findByCategorie(CategorieAchat categorie), findAllByOrderByDateAchatDesc()
5. Hibernate creates achats table automatically in SQLite
6. Unit test for AchatRepository: save achat, retrieve by id, filter by date range, filter by category
7. At least 5 test achats inserted successfully covering different categories and date ranges
8. BigDecimal precision: 2 decimal places for montant (euros and cents)

## Story 4.2: AchatService with Expense Calculation Logic

**As a** developer,
**I want** to create an AchatService layer with business logic for purchase management and expense calculations,
**so that** controllers can retrieve purchases and calculate total expenses for specified periods.

### Acceptance Criteria:

1. AchatService.java created in service package with @Service annotation
2. Methods implemented: findAll(), findById(Long id), save(Achat achat), delete(Long id), findByPeriod(LocalDate start, LocalDate end), findByCategorie(CategorieAchat categorie)
3. Calculation method: calculateTotalDepenses(LocalDate start, LocalDate end) sums montant of all achats in period
4. Calculation method: calculateDepensesByCategorie(LocalDate start, LocalDate end) returns Map<CategorieAchat, BigDecimal>
5. Validation enforced: montant must be positive, designation cannot be blank
6. Unit tests with mocked repository: verify calculateTotalDepenses() sums correctly, verify calculateDepensesByCategorie() groups correctly
7. Service methods transactional where needed

## Story 4.3: DashboardService with Financial Aggregations

**As a** developer,
**I want** to create a DashboardService that aggregates financial data from orders and purchases,
**so that** I can provide dashboard metrics (CA, expenses, profit, top products) efficiently.

### Acceptance Criteria:

1. DashboardService.java created with constructor injection of CommandeRepository, AchatRepository
2. Method calculateChiffreAffaires(LocalDate start, LocalDate end) returns BigDecimal: sum of amounts from orders with statut=PAYEE in date range
3. Method calculateTotalDepenses(LocalDate start, LocalDate end) delegates to AchatService
4. Method calculateBenefice(LocalDate start, LocalDate end) returns CA - dépenses
5. Method getTopProduits(LocalDate start, LocalDate end, int limit) returns List<TopProduitDto>: aggregates from paid orders, groups by product, sorts by quantity
6. TopProduitDto class with fields: produitNom, typeMiel, quantiteTotale, chiffreAffaires
7. Custom query in CommandeRepository to calculate CA for period
8. Unit tests: verify calculateChiffreAffaires() only counts PAYEE orders, verify calculateBenefice() subtracts correctly
9. All monetary values returned as BigDecimal with 2 decimal places

## Story 4.4: Purchase List & Create Form

**As a** beekeeper (parent user),
**I want** to view a chronological list of my supply purchases and add new expenses,
**so that** I can track where my money is going and maintain accurate records.

### Acceptance Criteria:

1. AchatController created with GET /achats endpoint returning "achats/list" view
2. templates/achats/list.html created with filter bar (year, category), purchase table, total displayed
3. Quick-add form at top with fields: Date, Désignation, Montant, Catégorie, Notes
4. POST /achats endpoint validates and saves
5. Empty state: "Aucun achat enregistré"
6. Category badges color-coded
7. Navigation: "Achats" link in sidebar

## Story 4.5: Purchase Edit & Delete

**As a** beekeeper (parent user),
**I want** to edit or delete a purchase record if I made a mistake,
**so that** my expense tracking remains accurate.

### Acceptance Criteria:

1. GET /achats/{id}/edit returns edit form
2. POST /achats/{id} updates existing achat
3. DELETE /achats/{id} hard deletes after confirmation
4. Confirmation modal for delete
5. Success flash messages

## Story 4.6: Financial Dashboard Main View

**As a** beekeeper (parent user),
**I want** to see a visual dashboard with my key financial metrics at a glance,
**so that** I can quickly understand my business performance.

### Acceptance Criteria:

1. HomeController GET / or DashboardController GET /dashboard returns dashboard view
2. templates/dashboard.html with period selector, metric cards (CA, Dépenses, Bénéfice, Commandes Payées)
3. Cards display large number, label, icon, optional comparison
4. Period filtering: current year default, filters apply to all metrics
5. Responsive: cards stack on mobile, 2×2 grid on desktop
6. Navigation: "Tableau de bord" first in sidebar
7. Performance: loads in <1 second

## Story 4.7: Top Products Widget & Simple Charts

**As a** beekeeper (parent user),
**I want** to see my top-selling products on the dashboard,
**so that** I can identify which honey types are most popular.

### Acceptance Criteria:

1. Dashboard updated with "Top 3 Produits" section
2. Ranked list showing product, quantity sold, revenue
3. Data from DashboardService.getTopProduits()
4. Optional: Chart.js bar chart or simple CSS bars
5. Empty state if no sales
6. Period filter applies

## Story 4.8: Expense Breakdown by Category

**As a** beekeeper (parent user),
**I want** to see a breakdown of my expenses by category,
**so that** I can understand where most of my money is spent.

### Acceptance Criteria:

1. Dashboard updated with "Répartition des Dépenses" section
2. List or chart showing each category with amount and percentage
3. Data from DashboardService or AchatService
4. Optional: pie/donut chart with Chart.js or CSS progress bars
5. Categories sorted by amount descending
6. Empty state if no expenses

---
