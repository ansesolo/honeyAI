# Story 4.8: Expense Breakdown by Category

**Epic:** Epic 4 - Financial Dashboard & Purchases
**Status:** Ready for Review
**Priority:** P2 - Medium
**Depends On:** Story 4.6

---

## User Story

**As a** beekeeper (parent user),
**I want** to see a breakdown of my expenses by category,
**so that** I can understand where most of my money is spent.

---

## Acceptance Criteria

1. Dashboard updated with "Repartition des Depenses" section
2. List or chart showing each category with amount and percentage
3. Data from DashboardService or AchatService
4. Optional: pie/donut chart with Chart.js or CSS progress bars
5. Categories sorted by amount descending
6. Empty state if no expenses

---

## Technical Notes

- Calculate percentage of total for each category
- Sort by amount descending (largest first)
- Optional visual representation (pie chart or progress bars)

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Expense breakdown displays correctly
- [x] Percentages accurate
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5

### File List
- `src/main/java/com/honeyai/dto/DepenseCategorieDto.java` (NEW)
- `src/main/java/com/honeyai/controller/HomeController.java` (MODIFIED - added AchatService injection, depensesParCategorie model attribute, buildDepensesParCategorie method)
- `src/main/resources/templates/home.html` (MODIFIED - added "Repartition des Depenses" section)
- `src/test/java/com/honeyai/controller/HomeControllerTest.java` (MODIFIED - added AchatService mock, 2 new tests)

### Change Log
- Created DepenseCategorieDto with categorie, montant, pourcentage fields
- Injected AchatService in HomeController
- Added buildDepensesParCategorie() private method: calls calculateDepensesByCategorie, computes percentage per category, sorts by montant desc
- Added "Repartition des Depenses" section in home.html between Top Produits and Quick Links
- Category badges with same color scheme as achats list (CIRE=warning, POTS=info, etc.)
- CSS progress bars colored per category, width = percentage
- Empty state: "Aucune depense sur cette periode"
- Added setUp stub for achatService.calculateDepensesByCategorie
- Added 2 new tests: depensesParCategorie with data (2 categories), empty state
- Full regression: 256 tests passing, 0 failures

### Completion Notes
- Percentages computed with BigDecimal divide + HALF_UP rounding
- Categories sorted by amount descending (largest expense first)
- CSS progress bars used (no Chart.js dependency, consistent with project style)
