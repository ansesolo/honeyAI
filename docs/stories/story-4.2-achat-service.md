# Story 4.2: AchatService with Expense Calculation Logic

**Epic:** Epic 4 - Financial Dashboard & Purchases
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** Story 4.1

---

## User Story

**As a** developer,
**I want** to create an AchatService layer with business logic for purchase management and expense calculations,
**so that** controllers can retrieve purchases and calculate total expenses for specified periods.

---

## Acceptance Criteria

1. AchatService.java created in service package with @Service annotation
2. Methods implemented: findAll(), findById(Long id), save(Achat achat), delete(Long id), findByPeriod(LocalDate start, LocalDate end), findByCategorie(CategorieAchat categorie)
3. Calculation method: calculateTotalDepenses(LocalDate start, LocalDate end) sums montant of all achats in period
4. Calculation method: calculateDepensesByCategorie(LocalDate start, LocalDate end) returns Map<CategorieAchat, BigDecimal>
5. Validation enforced: montant must be positive, designation cannot be blank
6. Unit tests with mocked repository: verify calculateTotalDepenses() sums correctly, verify calculateDepensesByCategorie() groups correctly
7. Service methods transactional where needed

---

## Technical Notes

- Sum calculations should return BigDecimal.ZERO if no purchases
- Group by category for expense breakdown
- Consider caching for dashboard performance

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Calculation methods accurate
- [x] Unit tests passing
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5

### File List
- `src/main/java/com/honeyai/service/AchatService.java` (NEW)
- `src/test/java/com/honeyai/service/AchatServiceTest.java` (NEW)

### Change Log
- Created AchatService with @Service, @Transactional, @RequiredArgsConstructor, @Slf4j
- Implemented CRUD: findAll(), findById(), save(), delete()
- Implemented query: findByPeriod(), findByCategorie()
- Implemented calculations: calculateTotalDepenses() (sum with BigDecimal.ZERO default), calculateDepensesByCategorie() (EnumMap grouping)
- Read-only transactions on all read methods
- Created 12 unit tests with Mockito (all passing): CRUD, period/category filtering, sum calculation, category grouping, empty period edge cases
- Full regression: 229 tests passing, 0 failures

### Completion Notes
- Validation (montant positive, designation not blank) enforced via entity-level annotations (@Positive, @NotBlank on Achat entity from story 4.1)
- EnumMap used for category grouping for performance
- BigDecimal.ZERO returned when no purchases in period
