# Story 2.3: CommandeService & ProduitService with Business Logic

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Ready for Review
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
2. CommandeService.java created with methods: findAll(), findById(Long id), findByClientId(Long clientId), findByStatut(StatutCommande statut), create(Commande order) validates and saves, updateStatut(Long commandeId, StatutCommande newStatut) transitions status with validation, calculateTotal(Long commandeId) sums ligne.quantite * ligne.prixUnitaire for all lignes
3. Business rules in CommandeService.create(): auto-populate dateCommande with today if null, set statut to COMMANDEE if null, for each LigneCommande auto-fetch prixUnitaire from ProduitService.getCurrentYearTarif() if not provided, validate at least one ligne exists
4. Business rules in updateStatut(): only allow forward transitions (COMMANDEE->RECUPEREE->PAYEE), throw InvalidStatusTransitionException if invalid (e.g., PAYEE->COMMANDEE), log status change with timestamp
5. Unit tests with mocked repositories: verify create() auto-fills prices from current year tarifs, verify calculateTotal() sums correctly, verify updateStatut() enforces transition rules and throws exception for invalid transitions, verify findByClientId() returns orders sorted by date descending
6. Integration test: create real order with 2 lignes, verify prices auto-populated, calculate total, transition through all statuses successfully

---

## Technical Notes

- Status transitions: COMMANDEE -> RECUPEREE -> PAYEE (forward only)
- Create InvalidStatusTransitionException in exception package
- Use LocalDate.now().getYear() for current year tarif lookup

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Status transition rules enforced
- [x] Unit tests passing with 80%+ coverage
- [ ] Code committed to repository

---

## Dev Agent Record

### File List

**New Files:**
- `src/main/java/com/honeyai/exception/PriceNotFoundException.java` - Exception for missing product prices
- `src/main/java/com/honeyai/exception/InvalidStatusTransitionException.java` - Exception for invalid status transitions
- `src/main/java/com/honeyai/service/ProductService.java` - Product and pricing business logic
- `src/main/java/com/honeyai/service/CommandeService.java` - Order management business logic
- `src/test/java/com/honeyai/service/ProductServiceTest.java` - ProductService unit tests (6 tests)
- `src/test/java/com/honeyai/service/CommandeServiceTest.java` - CommandeService unit tests (14 tests)
- `src/test/java/com/honeyai/service/CommandeServiceIntegrationTest.java` - Integration tests (3 tests)

**Modified Files:**
- `src/main/java/com/honeyai/model/Commande.java` - Added canTransitionTo() helper method

### Change Log

- Created PriceNotFoundException and InvalidStatusTransitionException custom exceptions
- Created ProductService with findAll(), findById(), getCurrentYearPrice(), updatePrice()
- Created CommandeService with CRUD operations, create() with auto-price population, updateStatut() with transition validation, calculateTotal()
- Added canTransitionTo() method to Commande entity for status transition validation
- Business rules enforced: forward-only transitions (COMMANDEE -> RECUPEREE -> PAYEE)
- Auto-populate dateCommande and statut on create if null
- Auto-fetch prixUnitaire from current year price if not provided
- 23 new tests added (6 + 14 + 3)

### Agent Model Used

Claude Opus 4.5

---

## QA Results

### Review Date: 2026-01-23

### Reviewed By: Quinn (Test Architect)

**Acceptance Criteria Review:**

| AC | Requirement | Status |
|----|-------------|--------|
| 1 | ProductService with findAll, findById, getCurrentYearPrice, updatePrice | ✅ PASS |
| 2 | CommandeService with CRUD, create, updateStatut, calculateTotal | ✅ PASS |
| 3 | create() auto-populates dateCommande, statut, and prices | ✅ PASS |
| 4 | updateStatut() enforces forward-only transitions with exception | ✅ PASS |
| 5 | Unit tests with mocked repositories | ✅ PASS (20 tests) |
| 6 | Integration test with real order | ✅ PASS (3 tests) |

**Code Quality:**
- Follows coding standards (@Transactional, @RequiredArgsConstructor, @Slf4j)
- Proper @Transactional(readOnly = true) on read methods
- BigDecimal for monetary calculations with proper rounding
- Clean separation of concerns (entity validation in Commande.canTransitionTo())
- Comprehensive logging for audit trail

**Test Coverage:**
- 23 new tests added
- Unit tests cover all business logic branches
- Integration test verifies end-to-end flow
- Status transition edge cases tested (backward, skipping)

### Gate Status

Gate: PASS → docs/qa/gates/2.3-order-produit-services.yml
