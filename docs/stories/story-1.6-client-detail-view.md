# Story 1.6: Client Detail View with History Placeholder

**Epic:** Epic 1 - Foundation & Client Management
**Status:** Ready for Review
**Priority:** P1 - High
**Depends On:** Story 1.5

---

## User Story

**As a** beekeeper (parent user),
**I want** to view detailed information about a specific client including their contact details and a placeholder for order history,
**so that** I can see all relevant information about a client in one place.

---

## Acceptance Criteria

1. GET endpoint /clients/{id} in ClientController returns "clients/detail" view with Client object in model or throws ClientNotFoundException if not found/deleted
2. templates/clients/detail.html created displaying: client name as heading, contact information (telephone, email, address) in readable format with labels, notes section if present, creation/update timestamps (formatted DD/MM/YYYY HH:mm)
3. "Modifier" button links to /clients/{id}/edit (green, prominent)
4. "Supprimer" button triggers soft delete with confirmation modal ("Etes-vous sur de vouloir supprimer ce client?"), POST to /clients/{id}/delete, red button positioned away from primary actions
5. "Retour a la liste" link to /clients (secondary styling)
6. Order history section with placeholder message: "Historique des commandes (a venir dans Epic 2)" or empty state "Aucune commande pour ce client"
7. "Nouvelle commande pour ce client" button disabled/greyed with tooltip "Disponible dans Epic 2"
8. 404 error page displayed (friendly message) if client id doesn't exist or is soft-deleted
9. Page layout clean, readable, no clutter, respects accessibility guidelines

---

## Technical Notes

- Date formatting: use DateTimeFormatter for French format DD/MM/YYYY
- Confirmation modal: use Bootstrap modal component
- Order history will be implemented in Epic 2 (Story 2.8)

---

## Definition of Done

- [x] All acceptance criteria met
- [x] 404 handling works correctly
- [x] Delete confirmation modal functional
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5 (claude-opus-4-5-20251101)

### File List
| File | Action |
|------|--------|
| src/main/java/com/honeyai/controller/ClientController.java | Modified (added detail, delete endpoints) |
| src/main/resources/templates/clients/detail.html | Created |
| src/main/resources/templates/error/404.html | Created |
| src/main/java/com/honeyai/exception/GlobalExceptionHandler.java | Created |
| src/main/resources/templates/fragments/layout.html | Modified (added tooltip init, scripts fragment) |

### Completion Notes
- GET /clients/{id} endpoint returns detail view with client info
- Detail page shows contact info, notes, timestamps (DD/MM/YYYY HH:mm format)
- "Modifier" button links to /clients/{id}/edit
- "Supprimer" button triggers Bootstrap modal confirmation
- POST /clients/{id}/delete performs soft delete with flash message
- "Retour a la liste" link present
- Order history placeholder with "a venir dans Epic 2" message
- "Nouvelle commande" button disabled with tooltip
- 404 page with friendly message when client not found
- GlobalExceptionHandler catches ClientNotFoundException
- All 32 tests pass

### Change Log
| Date | Change |
|------|--------|
| 2026-01-21 | Initial implementation of client detail view with modal and 404 handling |
