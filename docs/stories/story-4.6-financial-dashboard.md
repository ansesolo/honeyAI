# Story 4.6: Financial Dashboard Main View

**Epic:** Epic 4 - Financial Dashboard & Purchases
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 4.3, Story 1.4

---

## User Story

**As a** beekeeper (parent user),
**I want** to see a visual dashboard with my key financial metrics at a glance,
**so that** I can quickly understand my business performance.

---

## Acceptance Criteria

1. HomeController GET / or DashboardController GET /dashboard returns dashboard view
2. templates/dashboard.html with period selector, metric cards (CA, Depenses, Benefice, Commandes Payees)
3. Cards display large number, label, icon, optional comparison
4. Period filtering: current year default, filters apply to all metrics
5. Responsive: cards stack on mobile, 2x2 grid on desktop
6. Navigation: "Tableau de bord" first in sidebar
7. Performance: loads in <1 second

---

## Technical Notes

- Metric cards: Bootstrap card component with large text
- Icons: Font Awesome (fa-euro-sign, fa-shopping-cart, etc.)
- Period filter: dropdown or date range picker

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Dashboard displays correctly
- [ ] Metrics accurate
- [ ] Code committed to repository
