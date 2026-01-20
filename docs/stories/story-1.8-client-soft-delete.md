# Story 1.8: Client Soft Delete with Confirmation

**Epic:** Epic 1 - Foundation & Client Management
**Status:** Pending
**Priority:** P1 - High
**Depends On:** Story 1.6

---

## User Story

**As a** beekeeper (parent user),
**I want** to delete a client safely with confirmation and the ability to potentially restore later,
**so that** I don't permanently lose important customer data by accident.

---

## Acceptance Criteria

1. POST endpoint /clients/{id}/delete in ClientController calls ClientService.softDelete(id), redirects to /clients with success message "Client supprime"
2. Soft delete sets deletedAt timestamp without removing record from database
3. Confirmation modal implemented on detail and list pages: Bootstrap modal with title "Confirmer la suppression", message "Etes-vous sur de vouloir supprimer le client {nom}? Cette action peut etre annulee par l'administrateur.", buttons "Annuler" (grey) and "Supprimer" (red)
4. Deleted clients no longer appear in /clients list or search results
5. Accessing /clients/{id} of deleted client returns 404 with message "Ce client n'existe pas ou a ete supprime"
6. Database verification: deletedAt column populated with timestamp, all other data intact (id, nom, telephone, etc. preserved)
7. Success flash message displayed: "Client {nom} supprime avec succes"
8. Delete button styled red, positioned away from primary actions (bottom-right or separate section)
9. No cascade delete of related data (future orders) - just client marked deleted (Epic 2 will handle order relationships)

---

## Technical Notes

- Soft delete pattern: update deletedAt field, don't call repository.delete()
- Modal should prevent accidental clicks
- Consider adding restore functionality in future version

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Soft delete verified in database
- [ ] Confirmation modal functional
- [ ] Code committed to repository
