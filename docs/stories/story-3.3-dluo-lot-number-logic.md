# Story 3.3: DLUO Calculation & Lot Number Generation Logic

**Epic:** Epic 3 - Label Generation (Killer Feature)
**Status:** Pending
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

- [ ] All acceptance criteria met
- [ ] DLUO calculation accurate
- [ ] Lot numbers sequential and unique
- [ ] Code committed to repository
