# Story 3.3: DLUO Calculation & Lot Number Generation Logic

**Epic:** Epic 3 - Label Generation (Killer Feature)
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** Story 3.2

---

## User Story

**As a** developer,
**I want** to implement business logic for calculating DLUO (Best Before Date) and generating unique batch numbers,
**so that** labels display correct regulatory information automatically without manual calculation errors.

---

## Acceptance Criteria

1. EtiquetteService.java created with method calculateDluo(LocalDate dateRecolte, Integer dureeDays) returns LocalDate = dateRecolte.plusDays(dureeDays), formatted as MM/YYYY (month and year only per French regulation)
2. DLUO calculation uses dluoDureeJours from config (default 730 days = 2 years exactly)
3. Method generateNumeroLot(TypeMiel typeMiel, LocalDate dateRecolte) generates format: "YYYY-{TYPE}-NNN" where YYYY = harvest year, TYPE = miel type abbreviation (TF=Toutes Fleurs, FOR=Foret, CHA=Chataignier), NNN = sequential number per year/type
4. Lot number sequencing: query database (new table lots_etiquettes or counter in config) to get next sequential number for year+type combination, increment and store
5. LotsEtiquettes entity (optional simple approach): fields id, annee (Integer), typeMiel (String), dernierNumero (Integer), with unique constraint on (annee, typeMiel)
6. LotsEtiquettesRepository with method: findByAnneeAndTypeMiel(Integer annee, String typeMiel)
7. EtiquetteService.generateNumeroLot() logic: fetch or create LotsEtiquettes for current year+type, increment dernierNumero, save, return formatted lot number
8. Unit tests: calculateDluo(2024-06-15, 730) returns 2026-06-15 formatted "06/2026", generateNumeroLot(TOUTES_FLEURS, 2024-06-15) returns "2024-TF-001", second call returns "2024-TF-002", different type returns "2024-FOR-001"
9. Edge case tests: DLUO calculation across year boundaries, lot number rollover to new year (2025-TF-001 after 2024-TF-999)
10. DLUO display format: French regulation requires "A consommer de preference avant fin: MM/YYYY" (month/year, not full date)

---

## Technical Notes

- DLUO = Date Limite d'Utilisation Optimale (Best Before Date)
- Lot format: YYYY-TYPE-NNN (e.g., 2024-TF-001)
- Type abbreviations: TF=Toutes Fleurs, FOR=Foret, CHA=Chataignier
- Sequential numbering resets each year

---

## Definition of Done

- [x] All acceptance criteria met
- [x] DLUO calculation accurate
- [x] Lot numbers sequential and unique
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5 (claude-opus-4-5-20251101)

### File List

| File | Action | Description |
|------|--------|-------------|
| `src/main/java/com/honeyai/model/LotsEtiquettes.java` | Created | Entity for tracking lot number sequences per year/type |
| `src/main/java/com/honeyai/repository/LotsEtiquettesRepository.java` | Created | Repository with findByAnneeAndTypeMiel method |
| `src/main/java/com/honeyai/service/EtiquetteService.java` | Created | Service with calculateDluo() and generateNumeroLot() methods |
| `src/test/java/com/honeyai/service/EtiquetteServiceTest.java` | Created | 24 unit tests for DLUO and lot number generation |
| `src/test/java/com/honeyai/repository/LotsEtiquettesRepositoryTest.java` | Created | 5 integration tests for repository |

### Debug Log References
None - implementation completed without issues.

### Completion Notes
- EtiquetteService implements calculateDluo() with configurable duration (default 730 days)
- formatDluo() returns MM/YYYY format per French regulation
- generateNumeroLot() generates format YYYY-TYPE-NNN with sequential numbering per year/type
- Type abbreviations: TF (Toutes Fleurs), FOR (Foret), CHA (Chataignier)
- LotsEtiquettes entity with unique constraint on (annee, typeMiel) ensures no duplicates
- All 173 project tests pass including 29 new tests

### Change Log
| Date | Change |
|------|--------|
| 2026-01-26 | Initial implementation of Story 3.3 |

---

## QA Results

### Review Date: 2026-01-26

### Reviewed By: Quinn (Test Architect)

#### Findings Summary

| Category | Status |
|----------|--------|
| Acceptance Criteria | All 10 met |
| Unit Tests | 24 tests pass |
| Integration Tests | 5 tests pass |
| Regression | 173 total tests pass |
| Coding Standards | Compliant |
| Security | Input validation implemented |

#### Implementation Review

- **EtiquetteService**: Correctly implements DLUO calculation with configurable duration
- **formatDluo()**: Returns MM/YYYY format per French regulation
- **generateNumeroLot()**: Sequential numbering with YYYY-TYPE-NNN format
- **LotsEtiquettes**: Proper entity with unique constraint on (annee, typeMiel)
- **Edge Cases**: Leap year, year boundary, high sequence numbers all tested

### Gate Status

Gate: PASS → docs/qa/gates/3.3-dluo-lot-number-logic.yml
