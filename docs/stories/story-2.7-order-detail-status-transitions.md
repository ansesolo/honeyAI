# Story 2.7: Order Detail View with Status Transitions

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** Story 2.6

---

## User Story

**As a** beekeeper (parent user),
**I want** to view order details and transition it through workflow statuses (Ordered -> recovered -> Paid),
**so that** I can track the lifecycle of each order from creation to payment completion.

---

## Acceptance Criteria

1. GET /orders/{id} returns "orders/detail" view with Order object including: client info, OrderDate, current status, notes, list of lignes (produit, quantite, prix, sous-total), total calculated
2. templates/orders/detail.html created displaying: page title "Commande #{id}", client info section (name, phone with link to client detail), date and current status badge (large, prominent), notes if present, table of ordered products (columns: Produit, Quantite, Prix unitaire, Sous-total), Total row (bold, larger font)
3. Status transition buttons displayed based on current status: if COMMANDEE show "Marquer comme Recuperee" (orange button), if RECUPEREE show "Marquer comme Payee" (green button), if PAYEE show "Payee" (disabled green badge, no button)
4. POST /orders/{id}/statut endpoint accepts newStatus parameter, calls CommandeService.updateStatut(), redirects back to /orders/{id} with success message "Statut mis a jour: {newStatut}"
5. Status transition validation: invalid transitions display error message "Transition invalide" and do not update status
6. "Modifier" button to edit order (link to /orders/{id}/edit, grey button) - edit form similar to create form but pre-populated
7. "Retour a la liste" link to /orders
8. Future actions section (placeholder): "Imprimer bon de livraison" button disabled with tooltip "Disponible prochainement"
9. Timestamps: display "Creee le {date}", "Modifiee le {date}" at bottom
10. Responsive: readable on mobile/tablet, buttons stack vertically if needed

---

## Technical Notes

- Status workflow: COMMANDEE -> RECUPEREE -> PAYEE (forward only)
- Transition buttons should be prominent and clearly labeled
- Consider confirmation before status transition

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Status transitions work correctly
- [x] Invalid transitions handled gracefully
- [ ] Code committed to repository

---

## Dev Agent Record

**Implemented:** 2026-01-23
**Agent:** James (Full Stack Developer)
**Agent Model:** Claude Opus 4.5

### Files Modified
- `src/main/java/com/honeyai/controller/CommandeController.java` - Added POST /orders/{id}/statut, GET /orders/{id}/edit, POST /orders/{id}/edit endpoints
- `src/main/java/com/honeyai/service/CommandeService.java` - Added save() method for updates
- `src/main/resources/templates/orders/detail.html` - Enhanced with status transition buttons, client info card, timestamps, edit button, future actions placeholder
- `src/main/resources/templates/orders/form.html` - Updated to support edit mode with conditional title and action URL
- `src/test/java/com/honeyai/controller/CommandeControllerTest.java` - Added 4 new tests for status transition and edit endpoints

### Implementation Details
1. **Status Transition** - POST /orders/{id}/statut with validation, success/error flash messages
2. **Status Buttons** - Conditional display based on current status (COMMANDEE->Recuperee button, RECUPEREE->Payee button, PAYEE->disabled)
3. **Edit Form** - Pre-populated form with existing data, converts Commande to DTO and back
4. **Client Info Card** - Displays client name and phone with link
5. **Timestamps** - Shows createdAt and updatedAt
6. **Future Actions** - Disabled "Imprimer bon de livraison" button with tooltip
7. **Error Handling** - Invalid transitions show "Transition invalide" error

### Test Results
- CommandeControllerTest: 15 tests passed (4 new)
- Full regression: 117 tests passed

### Acceptance Criteria Verification
- AC1: GET /orders/{id} with all details - DONE (Story 2.6, enhanced)
- AC2: detail.html with all display elements - DONE
- AC3: Status transition buttons based on current status - DONE
- AC4: POST /orders/{id}/statut with redirect and message - DONE
- AC5: Invalid transition displays error message - DONE
- AC6: Modifier button links to edit form - DONE
- AC7: Retour a la liste link - DONE
- AC8: Future actions placeholder (disabled) - DONE
- AC9: Timestamps displayed - DONE
- AC10: Responsive layout - DONE (Bootstrap grid)

---

## Story DoD Checklist

### 1. Requirements Met
- [x] All functional requirements specified in the story are implemented
- [x] All acceptance criteria defined in the story are met (AC1-AC10 verified above)

### 2. Coding Standards & Project Structure
- [x] Code adheres to Operational Guidelines (Lombok, Spring Boot conventions)
- [x] File locations follow Project Structure
- [x] Tech Stack adherence (Spring Boot 3.5.x, Thymeleaf, Bootstrap 5.3.2)
- [x] Basic security best practices applied
- [x] No new linter errors

### 3. Testing
- [x] Unit tests implemented (CommandeControllerTest: 4 new tests)
- [x] All tests pass (117 tests total)

### 4. Functionality & Verification
- [x] Functionality verified through automated tests
- [x] Edge cases handled (invalid transitions, missing data)

### 5. Story Administration
- [x] Story file updated with Dev Agent Record
- [x] Status set to Ready for Review
- [x] Agent model documented (Claude Opus 4.5)

### 6. Dependencies, Build & Configuration
- [x] Project builds successfully without errors
- [N/A] No new dependencies added

### 7. Documentation
- [N/A] No user-facing documentation changes needed

### Final Confirmation
- [x] All applicable items above have been addressed
- Story is ready for review
