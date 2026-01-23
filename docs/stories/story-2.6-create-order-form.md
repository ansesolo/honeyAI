# Story 2.6: Create Order Form with Dynamic Product Lines

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** Story 2.5

---

## User Story

**As a** beekeeper (parent user),
**I want** to create a new order by selecting a client and adding product lines with quantities,
**so that** I can record customer orders quickly as they come in, replacing my paper notebook workflow.

---

## Acceptance Criteria

1. GET /orders/nouvelle returns "orders/form" view with: empty Commande object, list of all active clients (for dropdown), list of all products with current year prices (for product selection)
2. templates/orders/form.html created with: title "Nouvelle Commande", client selection dropdown (searchable/autocomplete preferred, or simple select), date order field (datepicker, default today), notes textarea, dynamic product lines section
3. Product lines: initial empty row with: Produit dropdown (showing "Miel 500g Toutes Fleurs - 12,50 EUR"), Quantite input (number, min=1), Prix unitaire (readonly, auto-filled from product selection), Sous-total (readonly, calculated quantite x prix), "Supprimer ligne" button (icon), "+ Ajouter un produit" button to add new line
4. JavaScript (vanilla) handles: add/remove product lines dynamically, auto-fill prix unitaire when product selected, calculate and display sous-total per line, calculate and display total order at bottom (sum of all sous-totals)
5. POST /orders endpoint accepts: clientId, dateCommande, notes, List<LigneCommandeDto> (produitId, quantite, prixUnitaire), calls CommandeService.create(), redirects to /orders/{id} (detail view) with success message "Commande creee avec succes"
6. Validation: at least one product line required, client selection required, quantite must be >=1, prix unitaire auto-populated but editable (for special discounts)
7. Form buttons: "Enregistrer" (green), "Annuler" (grey, back to /orders)
8. Price override: allow user to manually edit prix unitaire if needed (e.g., discount), with visual indicator it differs from catalog price
9. Mobile responsive: form usable on tablet (future consideration), stacks vertically
10. Minimum 1 product line pre-filled on load (empty dropdown) for UX clarity

---

## Technical Notes

- Vanilla JavaScript for dynamic form manipulation
- Store product prices in data attributes for JS access
- LigneCommandeDto for form submission binding
- Consider URL param ?clientId={id} for pre-selection from client detail

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Dynamic lines work correctly (add/remove)
- [x] Price calculations accurate
- [ ] Code committed to repository

---

## Dev Agent Record

**Implemented:** 2026-01-23
**Agent:** James (Full Stack Developer)
**Agent Model:** Claude Opus 4.5

### Files Created
- `src/main/java/com/honeyai/dto/LigneCommandeDto.java` - DTO for order line form binding
- `src/main/java/com/honeyai/dto/CommandeFormDto.java` - DTO for order creation form binding
- `src/main/java/com/honeyai/dto/ProductPriceDto.java` - DTO for product with current year price (used in dropdown)
- `src/main/resources/templates/orders/form.html` - Order creation form with dynamic product lines
- `src/main/resources/templates/orders/detail.html` - Order detail view
- `src/main/resources/static/js/order-form.js` - Vanilla JavaScript for dynamic form manipulation

### Files Modified
- `src/main/java/com/honeyai/controller/CommandeController.java` - Added GET /orders/nouvelle, POST /orders, GET /orders/{id} endpoints
- `src/main/java/com/honeyai/service/ProductService.java` - Added findAllWithCurrentYearPrices() method
- `src/main/resources/templates/orders/list.html` - Fixed link to /orders/nouvelle
- `src/test/java/com/honeyai/controller/CommandeControllerTest.java` - Added 6 new tests for form endpoints

### Implementation Details
1. **CommandeFormDto** - Captures clientId, dateCommande, notes, and List<LigneCommandeDto> from form
2. **LigneCommandeDto** - Captures productId, quantite, prixUnitaire for each line
3. **ProductPriceDto** - Provides product info with price for dropdown display (getDisplayLabel())
4. **Dynamic Form (JS)** - Add/remove product lines, auto-fill price on product selection, calculate subtotals and total
5. **Price Override** - Visual indicator when user modifies price from catalog value
6. **Validation** - Client required, at least one product line required, quantity >= 1
7. **Pre-selection** - Support ?clientId= URL param for client pre-selection

### Test Results
- CommandeControllerTest: 11 tests passed (6 new)
- Full regression: 113 tests passed

### Acceptance Criteria Verification
- AC1: GET /orders/nouvelle with clients and products - DONE
- AC2: orders/form.html with all form elements - DONE
- AC3: Product lines with dropdown, quantite, prix, sous-total, buttons - DONE
- AC4: JavaScript dynamic form handling - DONE
- AC5: POST /orders with redirect and success message - DONE
- AC6: Validation (client, lignes, quantite) - DONE
- AC7: Form buttons Enregistrer/Annuler - DONE
- AC8: Price override with visual indicator - DONE
- AC9: Mobile responsive (Bootstrap grid) - DONE
- AC10: One empty product line on load - DONE

---

## Story DoD Checklist

### 1. Requirements Met
- [x] All functional requirements specified in the story are implemented
- [x] All acceptance criteria defined in the story are met (AC1-AC10 verified above)

### 2. Coding Standards & Project Structure
- [x] Code adheres to Operational Guidelines (Lombok, Spring Boot conventions)
- [x] File locations follow Project Structure (controller, service, dto, templates, static/js)
- [x] Tech Stack adherence (Spring Boot 3.5.x, Thymeleaf, Bootstrap 5.3.2, Vanilla JS)
- [x] Basic security best practices applied (th:text for XSS prevention, @Valid validation)
- [x] No new linter errors
- [x] Code well-documented with Javadoc on DTOs

### 3. Testing
- [x] Unit tests implemented (CommandeControllerTest: 6 new tests)
- [x] All tests pass (113 tests total)
- [N/A] Integration tests - form tested at controller level

### 4. Functionality & Verification
- [x] Functionality verified through automated tests
- [x] Edge cases handled (empty lines, missing client, price override)

### 5. Story Administration
- [x] Story file updated with Dev Agent Record
- [x] Status set to Ready for Review
- [x] Agent model documented (Claude Opus 4.5)

### 6. Dependencies, Build & Configuration
- [x] Project builds successfully without errors
- [N/A] No new dependencies added
- [N/A] No new environment variables introduced

### 7. Documentation
- [x] DTOs documented with Javadoc
- [N/A] No user-facing documentation changes needed
- [N/A] No architectural changes

### Final Confirmation
- [x] All applicable items above have been addressed
- Story is ready for review
