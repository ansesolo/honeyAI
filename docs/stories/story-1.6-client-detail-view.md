# Story 1.6: Client Detail View with History Placeholder

**Epic:** Epic 1 - Foundation & Client Management
**Status:** Pending
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

- [ ] All acceptance criteria met
- [ ] 404 handling works correctly
- [ ] Delete confirmation modal functional
- [ ] Code committed to repository
