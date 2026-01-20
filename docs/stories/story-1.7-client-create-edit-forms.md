# Story 1.7: Client Create & Edit Forms

**Epic:** Epic 1 - Foundation & Client Management
**Status:** Pending
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

- [ ] All acceptance criteria met
- [ ] Form validation works correctly
- [ ] Create and edit both functional
- [ ] Code committed to repository
