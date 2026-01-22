# Story 1.7: Client Create & Edit Forms

**Epic:** Epic 1 - Foundation & Client Management
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** Story 1.5

---

## User Story

**As a** beekeeper (parent user),
**I want** to create a new client or edit an existing one through a simple form,
**so that** I can maintain accurate and up-to-date information about my customers.

---

## Acceptance Criteria

1. GET endpoint /clients/nouveau in ClientController returns "clients/form" view with empty Client object for creation
2. GET endpoint /clients/{id}/edit returns "clients/form" view with populated Client object for editing
3. POST endpoint /clients (for both create and update) validates Client object using @Valid, saves via ClientService, redirects to /clients with success flash message, or returns form with error messages if validation fails
4. templates/clients/form.html created with: form fields for nom (required, text input), telephone (text input), email (email input), adresse (textarea), notes (textarea large), clear labels above each field, Bootstrap form-control styling
5. Validation feedback displayed inline: red text below field showing error (e.g., "Le nom est obligatoire") if @NotBlank violated
6. Form buttons: "Enregistrer" (green, large, primary) and "Annuler" (grey, secondary) linking back to /clients or /clients/{id}
7. Form title dynamic: "Nouveau Client" for create, "Modifier Client - {nom}" for edit
8. Required fields marked with asterisk (*) next to label
9. Success message after save: "Client {nom} enregistre avec succes" displayed as dismissible Bootstrap alert on list page
10. Form accessible: keyboard navigation works (Tab through fields, Enter to submit), labels properly associated with inputs (for attribute)
11. Telephone field accepts French format (10 digits or international format), no strict validation but accepts any string for flexibility

---

## Technical Notes

- Use th:object for form binding
- @Valid + BindingResult for validation handling
- Reuse same template for create and edit (check if client.id is null)

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Form validation works correctly
- [x] Create and edit both functional
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5 (claude-opus-4-5-20251101)

### Debug Log References
None - implementation completed without issues.

### Completion Notes
- Implemented GET /clients/nouveau endpoint returning empty Client for creation
- Implemented GET /clients/{id}/edit endpoint returning populated Client for editing
- Implemented POST /clients endpoint with @Valid validation and BindingResult error handling
- Created clients/form.html template with:
  - All required form fields (name, phone, email, address, notes)
  - Bootstrap form-control-lg styling
  - Dynamic title (Nouveau Client vs Modifier Client)
  - Required field marker (asterisk) on name field
  - Inline validation error display with is-invalid class
  - Green "Enregistrer" button and grey "Annuler" button
  - Proper label-for associations for accessibility
  - Autofocus on name field
- Added 8 unit tests for ClientController form endpoints (all passing)
- All 40 project tests pass

### File List
| File | Action |
|------|--------|
| src/main/java/com/honeyai/controller/ClientController.java | Modified - Added createForm, editForm, save endpoints |
| src/main/resources/templates/clients/form.html | Created - Client create/edit form template |
| src/test/java/com/honeyai/controller/ClientControllerTest.java | Created - Unit tests for controller |

### Change Log
| Date | Change |
|------|--------|
| 2026-01-22 | Initial implementation of story 1.7 |

---

## QA Results

### Review Date: 2026-01-22

### Reviewed By: Quinn (Test Architect)

#### Acceptance Criteria Verification

| AC# | Requirement | Status |
|-----|-------------|--------|
| 1 | GET /clients/nouveau returns form with empty Client | PASS |
| 2 | GET /clients/{id}/edit returns form with populated Client | PASS |
| 3 | POST /clients validates, saves, redirects with flash message | PASS |
| 4 | Form fields with Bootstrap styling | PASS |
| 5 | Inline validation errors with red text | PASS |
| 6 | Enregistrer (green) and Annuler (grey) buttons | PASS |
| 7 | Dynamic title (Nouveau/Modifier) | PASS |
| 8 | Required fields marked with asterisk | PASS |
| 9 | Success message as dismissible alert | PASS |
| 10 | Accessible form (labels, keyboard nav) | PASS |
| 11 | Flexible phone format | PASS |

#### Code Quality Assessment

- **Coding Standards:** Compliant - Constructor injection, proper annotations
- **Security:** XSS prevention via th:text, @Valid input validation
- **Testing:** 8 unit tests covering all endpoints and edge cases
- **Build:** Compiles successfully, all 40 tests pass

#### Gate Status

Gate: PASS → docs/qa/gates/1.7-client-create-edit-forms.yml
