# Story 4.3: DashboardService with Financial Aggregations

**Epic:** Epic 4 - Financial Dashboard & Purchases
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 4.2, Story 2.3

---

## User Story

**As a** developer,
**I want** to create a DashboardService that aggregates financial data from orders and purchases,
**so that** I can provide dashboard metrics (CA, expenses, profit, top products) efficiently.

---

## Acceptance Criteria

1. DashboardService.java created with constructor injection of CommandeRepository, AchatRepository
2. Method calculateChiffreAffaires(LocalDate start, LocalDate end) returns BigDecimal: sum of amounts from orders with statut=PAYEE in date range
3. Method calculateTotalDepenses(LocalDate start, LocalDate end) delegates to AchatService
4. Method calculateBenefice(LocalDate start, LocalDate end) returns CA - depenses
5. Method getTopProduits(LocalDate start, LocalDate end, int limit) returns List<TopProduitDto>: aggregates from paid orders, groups by product, sorts by quantity
6. TopProduitDto class with fields: produitNom, typeMiel, quantiteTotale, chiffreAffaires
7. Custom query in CommandeRepository to calculate CA for period
8. Unit tests: verify calculateChiffreAffaires() only counts PAYEE orders, verify calculateBenefice() subtracts correctly
9. All monetary values returned as BigDecimal with 2 decimal places

---

## Technical Notes

- CA = Chiffre d'Affaires (Revenue) - only from PAYEE orders
- Benefice = CA - Depenses (Profit)
- Top products: aggregate from LigneCommande of paid orders

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Financial calculations accurate
- [ ] Unit tests passing
- [ ] Code committed to repository
