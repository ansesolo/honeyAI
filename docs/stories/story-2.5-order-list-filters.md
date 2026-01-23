# Story 2.5: Order List with Filters and Status Badges

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** Story 2.3, Story 1.4

---

## User Story

**As a** beekeeper (parent user),
**I want** to view a list of all orders with filtering by year and status,
**so that** I can quickly find orders that need attention (e.g., all "Commandee" orders to prepare) and review historical orders.

---

## Acceptance Criteria

1. CommandeController created with GET /commandes endpoint returning "commandes/list" view with all commandes, model includes: list of commandes, current year, list of years (distinct from commandes.dateCommande for filter dropdown), list of statuts (enum values)
2. templates/commandes/list.html created displaying: page title "Commandes", filter bar with dropdowns: Annee (default: current year), Statut (default: Tous), "Filtrer" button, "Nouvelle Commande" button (green, prominent, top-right)
3. Orders table with columns: No (id), Client (nom), Date commande (DD/MM/YYYY), Statut (badge colored: COMMANDEE=blue, RECUPEREE=orange, PAYEE=green), Montant total (calculated via CommandeService.calculateTotal()), Actions (Voir)
4. Filtering: GET /commandes?annee=2024&statut=COMMANDEE filters results server-side using CommandeService methods, results update in same view
5. Status badges styled with Bootstrap badge component and appropriate colors for quick visual scanning
6. Orders sorted by date descending (most recent first) within filtered results
7. Empty state if no orders match filters: "Aucune commande trouvee pour les filtres selectionnes"
8. "Tous" option in statut filter shows all statuses
9. Navigation link "Commandes" added to sidebar menu (update layout.html)
10. Montant total formatted as French currency "123,45 EUR"

---

## Technical Notes

- Badge colors: COMMANDEE=primary (blue), RECUPEREE=warning (orange), PAYEE=success (green)
- Use @RequestParam for filter parameters with defaults
- Consider caching calculateTotal results for list performance

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Filters work correctly
- [x] Status badges display properly
- [ ] Code committed to repository

---

## Dev Agent Record

**Implemented:** 2026-01-23
**Agent:** James (Full Stack Developer)
**Agent Model:** Claude Opus 4.5

### Files Created
- `src/main/java/com/honeyai/controller/CommandeController.java` - Controller with GET /commandes endpoint and filter support
- `src/main/resources/templates/commandes/list.html` - Order list view with filter bar and status badges
- `src/test/java/com/honeyai/controller/CommandeControllerTest.java` - 5 unit tests for CommandeController

### Files Modified
- `src/main/java/com/honeyai/repository/CommandeRepository.java` - Added query methods for filtering by year, status, with sorting
- `src/main/java/com/honeyai/service/CommandeService.java` - Added filtering methods: findWithFilters(), findByYear(), findByYearAndStatut(), getDistinctYears()
- `src/test/java/com/honeyai/repository/CommandeRepositoryTest.java` - Added 4 tests for new repository methods

### Implementation Details
1. **CommandeController** - GET /commandes with optional `annee` and `statut` query params, passes commandeService to template for total calculation
2. **Filter Bar** - Bootstrap card with year dropdown (distinct years from orders + current year), status dropdown (all StatutCommande values), Filtrer button
3. **Status Badges** - COMMANDEE=bg-primary (blue), RECUPEREE=bg-warning (orange), PAYEE=bg-success (green)
4. **Order Table** - Columns: No, Client, Date commande (dd/MM/yyyy), Statut (badge), Montant total (French format), Actions (Voir)
5. **Empty State** - "Aucune commande trouvee pour les filtres selectionnes" alert
6. **French Formatting** - Montant uses #numbers.formatDecimal with COMMA decimal separator
7. **Navigation** - "Commandes" link already existed in layout.html

### Test Results
- CommandeControllerTest: 5 tests passed
- CommandeRepositoryTest: 13 tests passed (4 new)
- Full regression: 107 tests passed

### Acceptance Criteria Verification
- AC1: CommandeController with GET /commandes - DONE
- AC2: commandes/list.html with filters - DONE
- AC3: Order table with proper columns - DONE
- AC4: Server-side filtering via CommandeService - DONE
- AC5: Status badges with Bootstrap colors - DONE
- AC6: Orders sorted by date descending - DONE
- AC7: Empty state message - DONE
- AC8: "Tous" option for status filter - DONE
- AC9: Navigation link - DONE (already existed)
- AC10: French currency formatting - DONE

---

## Story DoD Checklist

### 1. Requirements Met
- [x] All functional requirements specified in the story are implemented
- [x] All acceptance criteria defined in the story are met (AC1-AC10 verified above)

### 2. Coding Standards & Project Structure
- [x] Code adheres to Operational Guidelines (Lombok, Spring Boot conventions)
- [x] File locations follow Project Structure (controller, service, repository, templates)
- [x] Tech Stack adherence (Spring Boot 3.5.x, Thymeleaf, Bootstrap 5.3.2)
- [x] Basic security best practices applied (input via @RequestParam, enum type safety)
- [x] No new linter errors
- [x] Code well-documented with Javadoc on repository methods

### 3. Testing
- [x] Unit tests implemented (CommandeControllerTest: 5 tests)
- [x] Repository tests implemented (CommandeRepositoryTest: 4 new tests)
- [x] All tests pass (107 tests total)
- [N/A] Integration tests - filtering tested at repository level

### 4. Functionality & Verification
- [x] Functionality verified through automated tests
- [x] Edge cases handled (empty results, missing filters, immutable list handling)

### 5. Story Administration
- [x] Story file updated with Dev Agent Record
- [x] Status set to Ready for Review
- [x] Agent model documented (Claude Opus 4.5)

### 6. Dependencies, Build & Configuration
- [x] Project builds successfully without errors
- [N/A] No new dependencies added
- [N/A] No new environment variables introduced

### 7. Documentation
- [x] Repository methods documented with Javadoc
- [N/A] No user-facing documentation changes needed
- [N/A] No architectural changes

### Final Confirmation
- [x] All applicable items above have been addressed
- Story is ready for review
