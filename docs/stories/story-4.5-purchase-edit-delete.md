# Story 4.5: Purchase Edit & Delete

**Epic:** Epic 4 - Financial Dashboard & Purchases
**Status:** Pending
**Priority:** P1 - High
**Depends On:** Story 4.4

---

## User Story

**As a** beekeeper (parent user),
**I want** to edit or delete a purchase record if I made a mistake,
**so that** my expense tracking remains accurate.

---

## Acceptance Criteria

1. GET /achats/{id}/edit returns edit form
2. POST /achats/{id} updates existing achat
3. DELETE /achats/{id} hard deletes after confirmation
4. Confirmation modal for delete
5. Success flash messages

---

## Technical Notes

- Hard delete for purchases (not soft delete)
- Confirmation modal prevents accidental deletion
- Reuse form template for edit

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Edit form works correctly
- [ ] Delete with confirmation works
- [ ] Code committed to repository
