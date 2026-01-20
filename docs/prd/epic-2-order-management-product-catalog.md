# Epic 2: Order Management & Product Catalog

**Epic Goal:** Implémenter le cœur métier de HoneyAI en livrant le système complet de gestion des commandes et du catalogue produits. Les utilisateurs pourront créer et suivre des commandes clients à travers les trois statuts métier (Commandée → Récupérée → Payée), gérer un catalogue de produits miel avec leurs tarifs évolutifs par année, et bénéficier d'une application automatique des prix lors de la création des commandes.

## Story 2.1: Product & Tarif Entities with Year-based Pricing

**As a** developer,
**I want** to create Product and Tarif JPA entities with their repositories to support the product catalog with historical year-based pricing,
**so that** I can store honey products (types and formats) and track price changes across years while automatically applying current year prices to new orders.

### Acceptance Criteria:

1. Produit.java entity created with fields: id (Long, @Id @GeneratedValue), nom (String, "Miel" / "Cire avec miel" / "Reine"), type (TypeMiel enum: TOUTES_FLEURS, FORET, CHATAIGNIER - nullable for non-honey products), unite (String: "pot 500g", "pot 1kg", "unité")
2. TypeMiel enum created in enums package with values: TOUTES_FLEURS, FORET, CHATAIGNIER, with display labels in French
3. Tarif.java entity created with fields: id (Long), produitId (Long, @ManyToOne foreign key to Produit), annee (Integer, year like 2024), prix (BigDecimal, 2 decimal places for euros), @UniqueConstraint(produitId, annee) to prevent duplicate prices for same product/year
4. Relation: Produit has @OneToMany List<Tarif> tarifs, Tarif has @ManyToOne Produit produit
5. ProduitRepository interface extending JpaRepository<Produit, Long> with method: findAllByOrderByNomAsc()
6. TarifRepository interface extending JpaRepository<Tarif, Long> with methods: findByProduitIdAndAnnee(Long produitId, Integer annee), findByAnnee(Integer annee) for getting all prices of a specific year
7. Hibernate creates produits and tarifs tables in SQLite with proper foreign key constraints
8. Unit tests: create products (Miel 500g Toutes Fleurs, Miel 1kg Forêt, Cire avec miel, Reine), assign tarifs for years 2024 and 2025, verify retrieval by product and year, verify unique constraint violation if duplicate tarif inserted
9. Seed data: At least 6 products created (3 honey types × 2 formats + Cire + Reine) with 2024 prices populated

## Story 2.2: Commande & LigneCommande Entities with Status Workflow

**As a** developer,
**I want** to create Commande and LigneCommande JPA entities with status management and order line items,
**so that** I can persist complete orders linking clients to products with quantities and prices, tracking their progression through the business workflow.

### Acceptance Criteria:

1. StatutCommande enum created in enums package with values: COMMANDEE, RECUPEREE, PAYEE with French display labels ("Commandée", "Récupérée", "Payée")
2. Commande.java entity created with fields: id (Long), clientId (Long, @ManyToOne to Client), dateCommande (LocalDate, @NotNull), statut (StatutCommande, @Enumerated(STRING), default COMMANDEE), notes (String, @Column(length=1000)), createdAt (LocalDateTime), updatedAt (LocalDateTime)
3. LigneCommande.java entity created with fields: id (Long), commandeId (Long, @ManyToOne to Commande), produitId (Long, @ManyToOne to Produit), quantite (Integer, @Min(1)), prixUnitaire (BigDecimal, price at time of order for historical accuracy)
4. Relation: Commande has @OneToMany(cascade=ALL, orphanRemoval=true) List<LigneCommande> lignes, LigneCommande has @ManyToOne Commande commande and @ManyToOne Produit produit
5. Relation: Client has @OneToMany List<Commande> commandes (update Client.java from Epic 1)
6. CommandeRepository interface extending JpaRepository<Commande, Long> with methods: findByClientIdOrderByDateCommandeDesc(Long clientId), findByStatut(StatutCommande statut), findByDateCommandeBetween(LocalDate start, LocalDate end) for filtering
7. LigneCommandeRepository interface extending JpaRepository<LigneCommande, Long> (standard CRUD sufficient)
8. Hibernate creates commandes and lignes_commande tables with proper foreign keys and cascade rules
9. Unit tests: create commande with 2-3 lignes, verify cascade save (lignes auto-saved with commande), verify orphan removal (delete ligne removes it), verify status enum persists as string, verify client.commandes bidirectional relation works

## Story 2.3: CommandeService & ProduitService with Business Logic

**As a** developer,
**I want** to create service layers for Commande and Produit with business logic for order management and pricing,
**so that** controllers can rely on clean APIs for creating orders with automatic price lookup and status transitions.

### Acceptance Criteria:

1. ProduitService.java created with methods: findAll() returns all products ordered by name, findById(Long id) returns Optional<Produit>, getCurrentYearTarif(Long produitId) returns current year price or throws PriceNotFoundException, updateTarif(Long produitId, Integer annee, BigDecimal prix) saves/updates tarif for product/year
2. CommandeService.java created with methods: findAll(), findById(Long id), findByClientId(Long clientId), findByStatut(StatutCommande statut), create(Commande commande) validates and saves, updateStatut(Long commandeId, StatutCommande newStatut) transitions status with validation, calculateTotal(Long commandeId) sums ligne.quantite * ligne.prixUnitaire for all lignes
3. Business rules in CommandeService.create(): auto-populate dateCommande with today if null, set statut to COMMANDEE if null, for each LigneCommande auto-fetch prixUnitaire from ProduitService.getCurrentYearTarif() if not provided, validate at least one ligne exists
4. Business rules in updateStatut(): only allow forward transitions (COMMANDEE→RECUPEREE→PAYEE), throw InvalidStatusTransitionException if invalid (e.g., PAYEE→COMMANDEE), log status change with timestamp
5. Unit tests with mocked repositories: verify create() auto-fills prices from current year tarifs, verify calculateTotal() sums correctly, verify updateStatut() enforces transition rules and throws exception for invalid transitions, verify findByClientId() returns orders sorted by date descending
6. Integration test: create real commande with 2 lignes, verify prices auto-populated, calculate total, transition through all statuses successfully

## Story 2.4: Product Catalog & Tarif Management UI

**As a** beekeeper (parent user),
**I want** to view and manage my product catalog with prices for each year,
**so that** I can prepare pricing for upcoming seasons and ensure accurate pricing on orders.

### Acceptance Criteria:

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

## Story 2.5: Order List with Filters and Status Badges

**As a** beekeeper (parent user),
**I want** to view a list of all orders with filtering by year and status,
**so that** I can quickly find orders that need attention (e.g., all "Commandée" orders to prepare) and review historical orders.

### Acceptance Criteria:

1. CommandeController created with GET /commandes endpoint returning "commandes/list" view with all commandes, model includes: list of commandes, current year, list of years (distinct from commandes.dateCommande for filter dropdown), list of statuts (enum values)
2. templates/commandes/list.html created displaying: page title "Commandes", filter bar with dropdowns: Année (default: current year), Statut (default: Tous), "Filtrer" button, "Nouvelle Commande" button (green, prominent, top-right)
3. Orders table with columns: N° (id), Client (nom), Date commande (DD/MM/YYYY), Statut (badge colored: COMMANDEE=blue, RECUPEREE=orange, PAYEE=green), Montant total (calculated via CommandeService.calculateTotal()), Actions (Voir)
4. Filtering: GET /commandes?annee=2024&statut=COMMANDEE filters results server-side using CommandeService methods, results update in same view
5. Status badges styled with Bootstrap badge component and appropriate colors for quick visual scanning
6. Orders sorted by date descending (most recent first) within filtered results
7. Empty state if no orders match filters: "Aucune commande trouvée pour les filtres sélectionnés"
8. "Tous" option in statut filter shows all statuses
9. Navigation link "Commandes" added to sidebar menu (update layout.html)
10. Montant total formatted as French currency "123,45 €"

## Story 2.6: Create Order Form with Dynamic Product Lines

**As a** beekeeper (parent user),
**I want** to create a new order by selecting a client and adding product lines with quantities,
**so that** I can record customer orders quickly as they come in, replacing my paper notebook workflow.

### Acceptance Criteria:

1. GET /commandes/nouvelle returns "commandes/form" view with: empty Commande object, list of all active clients (for dropdown), list of all products with current year prices (for product selection)
2. templates/commandes/form.html created with: title "Nouvelle Commande", client selection dropdown (searchable/autocomplete preferred, or simple select), date commande field (datepicker, default today), notes textarea, dynamic product lines section
3. Product lines: initial empty row with: Produit dropdown (showing "Miel 500g Toutes Fleurs - 12,50 €"), Quantité input (number, min=1), Prix unitaire (readonly, auto-filled from product selection), Sous-total (readonly, calculated quantite × prix), "Supprimer ligne" button (icon), "+ Ajouter un produit" button to add new line
4. JavaScript (vanilla) handles: add/remove product lines dynamically, auto-fill prix unitaire when product selected, calculate and display sous-total per line, calculate and display total commande at bottom (sum of all sous-totals)
5. POST /commandes endpoint accepts: clientId, dateCommande, notes, List<LigneCommandeDto> (produitId, quantite, prixUnitaire), calls CommandeService.create(), redirects to /commandes/{id} (detail view) with success message "Commande créée avec succès"
6. Validation: at least one product line required, client selection required, quantite must be ≥1, prix unitaire auto-populated but editable (for special discounts)
7. Form buttons: "Enregistrer" (green), "Annuler" (grey, back to /commandes)
8. Price override: allow user to manually edit prix unitaire if needed (e.g., discount), with visual indicator it differs from catalog price
9. Mobile responsive: form usable on tablet (future consideration), stacks vertically
10. Minimum 1 product line pre-filled on load (empty dropdown) for UX clarity

## Story 2.7: Order Detail View with Status Transitions

**As a** beekeeper (parent user),
**I want** to view order details and transition it through workflow statuses (Commandée → Récupérée → Payée),
**so that** I can track the lifecycle of each order from creation to payment completion.

### Acceptance Criteria:

1. GET /commandes/{id} returns "commandes/detail" view with Commande object including: client info, dateCommande, current statut, notes, list of lignes (produit, quantite, prix, sous-total), total calculated
2. templates/commandes/detail.html created displaying: page title "Commande #{id}", client info section (name, phone with link to client detail), date and current status badge (large, prominent), notes if present, table of ordered products (columns: Produit, Quantité, Prix unitaire, Sous-total), Total row (bold, larger font)
3. Status transition buttons displayed based on current status: if COMMANDEE show "Marquer comme Récupérée" (orange button), if RECUPEREE show "Marquer comme Payée" (green button), if PAYEE show "Payée ✓" (disabled green badge, no button)
4. POST /commandes/{id}/statut endpoint accepts newStatut parameter, calls CommandeService.updateStatut(), redirects back to /commandes/{id} with success message "Statut mis à jour: {newStatut}"
5. Status transition validation: invalid transitions display error message "Transition invalide" and do not update status
6. "Modifier" button to edit commande (link to /commandes/{id}/edit, grey button) - edit form similar to create form but pre-populated
7. "Retour à la liste" link to /commandes
8. Future actions section (placeholder): "Imprimer bon de livraison" button disabled with tooltip "Disponible prochainement"
9. Timestamps: display "Créée le {date}", "Modifiée le {date}" at bottom
10. Responsive: readable on mobile/tablet, buttons stack vertically if needed

## Story 2.8: Client Detail - Display Order History

**As a** beekeeper (parent user),
**I want** to see a client's complete order history on their detail page,
**so that** I can understand their purchasing patterns and reference past orders when they contact me.

### Acceptance Criteria:

1. Update ClientController GET /clients/{id} endpoint to include client.commandes in model (fetched via Client entity @OneToMany relation or CommandeService.findByClientId())
2. Update templates/clients/detail.html to replace placeholder "Historique des commandes (à venir)" with actual order list table
3. Order history table displays: Date commande, Statut (badge), Montant total, Actions (Voir link to /commandes/{id})
4. Orders sorted by date descending (most recent first)
5. Empty state if client has no orders: "Aucune commande pour ce client" with encouragement "Créez la première commande"
6. "Nouvelle commande pour ce client" button (previously disabled in Story 1.6) now active, links to /commandes/nouvelle?clientId={id} (pre-selects client in form)
7. Summary stats above table: Total commandes ({count}), Total dépensé ({sum of all PAYEE commandes}), Dernière commande le {date}
8. Clicking order row or "Voir" navigates to order detail page
9. Table responsive: scrolls horizontally on mobile, stacks on very small screens
10. Performance: limit display to last 50 orders with "Voir toutes les commandes" link if more exist (pagination or "show more")

---
