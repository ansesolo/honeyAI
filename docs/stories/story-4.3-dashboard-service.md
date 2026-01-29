# Story 4.3: DashboardService with Financial Aggregations

**Epic:** Epic 4 - Financial Dashboard & Purchases
**Status:** Ready for Review
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

- [x] All acceptance criteria met
- [x] Financial calculations accurate
- [x] Unit tests passing
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5

### File List
- `src/main/java/com/honeyai/dto/TopProduitDto.java` (NEW)
- `src/main/java/com/honeyai/service/DashboardService.java` (NEW)
- `src/main/java/com/honeyai/repository/OrderRepository.java` (MODIFIED - added findPaidOrdersWithLinesBetween query)
- `src/test/java/com/honeyai/service/DashboardServiceTest.java` (NEW)

### Change Log
- Created TopProduitDto with produitNom, typeMiel, quantiteTotale, chiffreAffaires
- Added findPaidOrdersWithLinesBetween() JPQL query in OrderRepository (JOIN FETCH lines + products, filtered by PAID status and date range)
- Created DashboardService with @Transactional(readOnly = true) by default
- Implemented calculateChiffreAffaires() - sums line totals from paid orders
- Implemented calculateTotalDepenses() - delegates to AchatService
- Implemented calculateBenefice() - CA minus dépenses
- Implemented getTopProduits() - aggregates by product, sorts by quantity desc, limits results
- All BigDecimal results with scale 2 (HALF_UP)
- Created 8 unit tests with Mockito (all passing): CA calculation, zero CA, delegation to AchatService, bénéfice positive/negative, top produits grouping/sorting/limiting, empty orders
- Full regression: 237 tests passing, 0 failures

### Completion Notes
- DashboardService injects OrderRepository + AchatService (not AchatRepository directly) per delegation pattern
- Uses LinkedHashMap + merge for product aggregation in getTopProduits()
- HoneyType displayLabel used for typeMiel field in TopProduitDto
