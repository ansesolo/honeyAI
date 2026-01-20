# Story 4.8: Expense Breakdown by Category

**Epic:** Epic 4 - Financial Dashboard & Purchases
**Status:** Pending
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

- [ ] All acceptance criteria met
- [ ] Expense breakdown displays correctly
- [ ] Percentages accurate
- [ ] Code committed to repository
