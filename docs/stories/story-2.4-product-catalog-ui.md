# Story 2.4: Product Catalog & Tarif Management UI

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Ready for Review
**Priority:** P1 - High
**Depends On:** Story 2.3, Story 1.4

---

## User Story

**As a** beekeeper (parent user),
**I want** to view and manage my product catalog with prices for each year,
**so that** I can prepare pricing for upcoming seasons and ensure accurate pricing on orders.

---

## Acceptance Criteria

1. ProduitController created with GET /produits endpoint returning "produits/list" view with all products and their current year tarifs
2. templates/produits/list.html created displaying: page title "Catalogue Produits", table with columns: Nom produit, Type (if miel), Unite, Prix 2024, Prix 2025, Actions (Modifier prix)
3. Current year (2026) prices highlighted or bolded to distinguish from future years
4. "Modifier Prix" button per product opens inline form or modal to edit tarif for selected year (dropdown: 2024, 2025, 2026, 2027), input for price (EUR), save updates TarifRepository
5. POST /produits/{id}/tarif endpoint accepts produitId, annee, prix, calls ProduitService.updateTarif(), redirects back to /produits with success message "Prix mis a jour pour {produit} en {annee}"
6. Products grouped or color-coded by type (Miel Toutes Fleurs, Miel Foret, etc.) for readability
7. Empty state if no products: "Aucun produit dans le catalogue" (should not occur with seed data)
8. Price formatting: display as "12,50 EUR" (French format with comma decimal separator)
9. Navigation link "Produits" added to sidebar menu (update layout.html from Epic 1)
10. Responsive: table scrolls horizontally on mobile, readable on desktop

---

## Technical Notes

- Use NumberFormat for French currency formatting
- Modal or inline form for tarif editing
- Consider showing price history or comparison

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Price editing works correctly
- [x] French formatting applied
- [ ] Code committed to repository

---

## Dev Agent Record

**Implemented:** 2026-01-23
**Agent:** James (Full Stack Developer)

### Files Created
- `src/main/java/com/honeyai/controller/ProductController.java` - Controller with GET /produits and POST /produits/{id}/tarif endpoints
- `src/main/resources/templates/produits/list.html` - Product catalog view with Bootstrap table grouped by honey type, price editing modal
- `src/test/java/com/honeyai/controller/ProductControllerTest.java` - Unit tests for ProductController

### Files Modified
- `src/main/java/com/honeyai/service/ProductService.java` - Added getPriceForYear() method returning null when not found
- `src/main/resources/templates/fragments/layout.html` - Added "Produits" navigation link

### Implementation Details
1. **ProductController** - Handles /produits routes, passes productService to template for price lookups
2. **Product Catalog View** - Table displays products grouped by honey type (Toutes Fleurs, Foret, Chataignier, Autres), with prices for years 2024-2027
3. **Current Year Highlighting** - Year 2026 column highlighted with table-warning class and bold text
4. **Price Editing Modal** - Bootstrap modal with year dropdown and price input, form submits to POST endpoint
5. **French Price Formatting** - Uses Thymeleaf #numbers.formatDecimal with COMMA decimal separator: "12,50 EUR"
6. **Empty State** - Shows "Aucun produit dans le catalogue" alert when no products
7. **Responsive Design** - Table uses Bootstrap table-responsive wrapper for horizontal scrolling on mobile

### Test Results
- ProductControllerTest: 3 tests passed
- Full regression: 98 tests passed
- Acceptance criteria verified manually

---

## Story DoD Checklist

### 1. Requirements Met
- [x] All functional requirements specified in the story are implemented
- [x] All acceptance criteria defined in the story are met:
  - AC1: ProductController with GET /produits - DONE
  - AC2: produits/list.html with proper columns (Nom, Type, Unite, Prix 2024-2027, Actions) - DONE
  - AC3: Current year (2026) highlighted with table-warning class and bold text - DONE
  - AC4: Modal for editing price with year dropdown and price input - DONE
  - AC5: POST /produits/{id}/tarif endpoint with success message - DONE
  - AC6: Products grouped by honey type (color-coded sections) - DONE
  - AC7: Empty state "Aucun produit dans le catalogue" - DONE
  - AC8: French price formatting "12,50 EUR" - DONE
  - AC9: Navigation link "Produits" added to layout - DONE
  - AC10: Responsive with horizontal scrolling - DONE

### 2. Coding Standards & Project Structure
- [x] Code adheres to Operational Guidelines (Lombok, Spring Boot conventions)
- [x] File locations follow Project Structure (controller, service, templates)
- [x] Tech Stack adherence (Spring Boot 3.5.x, Thymeleaf, Bootstrap 5.3.2)
- [x] Basic security best practices applied (input validation via @RequestParam)
- [x] No new linter errors
- [x] Code well-commented where necessary

### 3. Testing
- [x] Unit tests implemented (ProductControllerTest: 3 tests)
- [x] All tests pass (98 tests total)
- [N/A] Integration tests - covered by existing ProductServiceTest

### 4. Functionality & Verification
- [x] Functionality verified through automated tests
- [x] Edge cases handled (null prices display as dash, empty product list shows alert)

### 5. Story Administration
- [x] Story file updated with Dev Agent Record
- [x] Status set to Ready for Review

### 6. Dependencies, Build & Configuration
- [x] Project builds successfully without errors
- [N/A] No new dependencies added
- [N/A] No new environment variables introduced

### 7. Documentation
- [x] Code self-documenting with clear naming conventions
- [N/A] No user-facing documentation changes needed
- [N/A] No architectural changes

### Final Confirmation
- [x] All applicable items above have been addressed
- Story is ready for review
