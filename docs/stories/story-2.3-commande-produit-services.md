# Story 2.3: CommandeService & ProduitService with Business Logic

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 2.2

---

## User Story

**As a** developer,
**I want** to create service layers for Commande and Produit with business logic for order management and pricing,
**so that** controllers can rely on clean APIs for creating orders with automatic price lookup and status transitions.

---

## Acceptance Criteria

1. ProduitService.java created with methods: findAll() returns all products ordered by name, findById(Long id) returns Optional<Produit>, getCurrentYearTarif(Long produitId) returns current year price or throws PriceNotFoundException, updateTarif(Long produitId, Integer annee, BigDecimal prix) saves/updates tarif for product/year
2. CommandeService.java created with methods: findAll(), findById(Long id), findByClientId(Long clientId), findByStatut(StatutCommande statut), create(Commande commande) validates and saves, updateStatut(Long commandeId, StatutCommande newStatut) transitions status with validation, calculateTotal(Long commandeId) sums ligne.quantite * ligne.prixUnitaire for all lignes
3. Business rules in CommandeService.create(): auto-populate dateCommande with today if null, set statut to COMMANDEE if null, for each LigneCommande auto-fetch prixUnitaire from ProduitService.getCurrentYearTarif() if not provided, validate at least one ligne exists
4. Business rules in updateStatut(): only allow forward transitions (COMMANDEE->RECUPEREE->PAYEE), throw InvalidStatusTransitionException if invalid (e.g., PAYEE->COMMANDEE), log status change with timestamp
5. Unit tests with mocked repositories: verify create() auto-fills prices from current year tarifs, verify calculateTotal() sums correctly, verify updateStatut() enforces transition rules and throws exception for invalid transitions, verify findByClientId() returns orders sorted by date descending
6. Integration test: create real commande with 2 lignes, verify prices auto-populated, calculate total, transition through all statuses successfully

---

## Technical Notes

- Status transitions: COMMANDEE -> RECUPEREE -> PAYEE (forward only)
- Create InvalidStatusTransitionException in exception package
- Use LocalDate.now().getYear() for current year tarif lookup

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Status transition rules enforced
- [ ] Unit tests passing with 80%+ coverage
- [ ] Code committed to repository
