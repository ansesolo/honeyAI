# Story 1.8: Client Soft Delete with Confirmation

**Epic:** Epic 1 - Foundation & Client Management
**Status:** Ready for Review
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

- [x] All acceptance criteria met
- [x] Soft delete verified in database
- [x] Confirmation modal functional
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5 (claude-opus-4-5-20251101)

### Debug Log References
None - implementation completed without issues.

### Completion Notes
- Updated confirmation modal message on detail page to match AC3 specification
- Updated ClientNotFoundException message to French: "Ce client n'existe pas ou a ete supprime"
- Added delete button with confirmation modal to list page (desktop table and mobile cards)
- Modal uses JavaScript to dynamically update client name and form action
- Added 3 new controller tests for delete functionality (total 11 controller tests)
- Updated service test to verify French exception message
- All 43 project tests pass

### File List
| File | Action |
|------|--------|
| src/main/resources/templates/clients/detail.html | Modified - Updated modal message |
| src/main/resources/templates/clients/list.html | Modified - Added delete button and modal |
| src/main/java/com/honeyai/exception/ClientNotFoundException.java | Modified - French error message |
| src/test/java/com/honeyai/controller/ClientControllerTest.java | Modified - Added delete tests |
| src/test/java/com/honeyai/service/ClientServiceTest.java | Modified - Updated exception message test |

### Change Log
| Date | Change |
|------|--------|
| 2026-01-22 | Implementation of story 1.8 soft delete with confirmation |

---

## QA Results

### Review Date: 2026-01-22

### Reviewed By: Quinn (Test Architect)

#### Acceptance Criteria Verification

| AC# | Requirement | Status |
|-----|-------------|--------|
| 1 | POST /clients/{id}/delete calls softDelete, redirects with success | PASS |
| 2 | Soft delete sets deletedAt without removing record | PASS |
| 3 | Confirmation modal on detail AND list pages with correct message | PASS |
| 4 | Deleted clients excluded from list/search | PASS |
| 5 | 404 with French message for deleted client | PASS |
| 6 | Database preserves all data, only deletedAt populated | PASS |
| 7 | Success flash message displayed | PASS |
| 8 | Delete button red, positioned appropriately | PASS |
| 9 | No cascade delete | PASS (N/A - no order relationships yet) |

#### Code Quality Assessment

- **Coding Standards:** Compliant - Proper escaping, th:text usage
- **Security:** Modal prevents accidental deletion, no XSS vulnerabilities
- **Testing:** 43 tests pass including 3 new delete-specific tests
- **Build:** Successful

#### Gate Status

Gate: PASS → docs/qa/gates/1.8-client-soft-delete.yml
