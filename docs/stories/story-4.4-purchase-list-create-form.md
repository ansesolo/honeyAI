# Story 4.4: Purchase List & Create Form

**Epic:** Epic 4 - Financial Dashboard & Purchases
**Status:** Ready for Review
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

- [x] All acceptance criteria met
- [x] List displays correctly with filters
- [x] Quick-add form works
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5

### File List
- `src/main/java/com/honeyai/controller/AchatController.java` (NEW)
- `src/main/resources/templates/achats/list.html` (NEW)
- `src/test/java/com/honeyai/controller/AchatControllerTest.java` (NEW)

### Change Log
- Created AchatController with GET /achats (list with year/category filters) and POST /achats (save with validation)
- Created achats/list.html template with: quick-add form, filter bar (year + category), purchase table with category badges, total in header and footer, empty state, flash messages
- Category badge colors: CIRE=warning, POTS=info, COUVERCLES=secondary, NOURRISSEMENT=success, AUTRE=dark
- Navigation "Achats" link already present in layout.html sidebar
- Created 7 controller tests with MockMvc: list view, empty state, filter by year, filter by category, total calculation, save success redirect, validation errors
- Full regression: 244 tests passing, 0 failures

### Completion Notes
- Quick-add form uses inline row layout for fast data entry
- Available years: current year + 2 previous years
- Filter supports combined year + category filtering
- Total is computed from displayed (filtered) achats list
