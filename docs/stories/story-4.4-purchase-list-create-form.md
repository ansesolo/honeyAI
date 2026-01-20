# Story 4.4: Purchase List & Create Form

**Epic:** Epic 4 - Financial Dashboard & Purchases
**Status:** Pending
**Priority:** P1 - High
**Depends On:** Story 4.2, Story 1.4

---

## User Story

**As a** beekeeper (parent user),
**I want** to view a chronological list of my supply purchases and add new expenses,
**so that** I can track where my money is going and maintain accurate records.

---

## Acceptance Criteria

1. AchatController created with GET /achats endpoint returning "achats/list" view
2. templates/achats/list.html created with filter bar (year, category), purchase table, total displayed
3. Quick-add form at top with fields: Date, Designation, Montant, Categorie, Notes
4. POST /achats endpoint validates and saves
5. Empty state: "Aucun achat enregistre"
6. Category badges color-coded
7. Navigation: "Achats" link in sidebar

---

## Technical Notes

- Quick-add form for fast data entry
- Category badges: use Bootstrap badge with different colors
- Total displayed at top for quick reference

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] List displays correctly with filters
- [ ] Quick-add form works
- [ ] Code committed to repository
