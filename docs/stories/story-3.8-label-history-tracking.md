# Story 3.8: Label Generation History & Lot Number Tracking

**Epic:** Epic 3 - Label Generation (Killer Feature)
**Status:** Ready for Review
**Priority:** P2 - Medium
**Depends On:** Story 3.7

---

## User Story

**As a** beekeeper (parent user),
**I want** to see a history of recently generated label batches with their lot numbers,
**so that** I can reference past generations and understand which lot numbers have been used for traceability.

---

## Acceptance Criteria

1. HistoriqueEtiquettes entity created with fields: id, typeMiel, formatPot, dateRecolte, dluo, numeroLot, quantite, dateGeneration (timestamp), prixUnitaire
2. HistoriqueEtiquettesRepository with method: findTop20ByOrderByDateGenerationDesc() for recent history
3. EtiquetteService saves history record after each successful PDF generation
4. GET /etiquettes/historique endpoint returns "etiquettes/historique" view with list of recent generations
5. templates/etiquettes/historique.html displaying: page title "Historique des Etiquettes", table with columns: Date generation, Type miel, Format, Date recolte, DLUO, Lot, Quantite, Actions (Re-generer)
6. "Re-generer" button: links back to /etiquettes form pre-filled with same parameters (type, format, date, quantite) allowing quick regeneration with new lot number
7. Navigation: link "Historique" added below "Etiquettes" in sidebar or as tab on /etiquettes page
8. Empty state: "Aucune etiquette generee pour le moment" if no history
9. History limited to last 20-50 generations (pagination not needed for MVP, just recent history)
10. Display formatted dates (DD/MM/YYYY HH:mm) and French labels for all fields

---

## Technical Notes

- History provides traceability for regulatory compliance
- Re-generate feature creates new lot number (not reuse)
- Consider linking history to label PDF files if stored

---

## Definition of Done

- [x] All acceptance criteria met
- [x] History displays correctly
- [x] Re-generate links work
- [x] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5 (claude-opus-4-5-20251101)

### File List
| File | Action | Description |
|------|--------|-------------|
| src/main/java/com/honeyai/model/HistoriqueEtiquettes.java | Created | Entity for tracking label generation history |
| src/main/java/com/honeyai/repository/HistoriqueEtiquettesRepository.java | Created | Repository with findTop20ByOrderByDateGenerationDesc method |
| src/main/java/com/honeyai/service/EtiquetteService.java | Modified | Added saveHistorique and getRecentHistorique methods |
| src/main/java/com/honeyai/controller/EtiquetteController.java | Modified | Added /historique and /regenerer endpoints, call saveHistorique after PDF generation |
| src/main/resources/templates/etiquettes/historique.html | Created | History view template with table and re-generate links |
| src/main/resources/templates/fragments/layout.html | Modified | Added dropdown menu for Etiquettes with Historique link |
| src/test/java/com/honeyai/repository/HistoriqueEtiquettesRepositoryTest.java | Created | Tests for repository methods |
| src/test/java/com/honeyai/service/EtiquetteServiceTest.java | Modified | Added tests for saveHistorique and getRecentHistorique |
| src/test/java/com/honeyai/controller/EtiquetteControllerTest.java | Modified | Added tests for historique and regenerer endpoints |

### Change Log
- Created HistoriqueEtiquettes entity with all required fields (id, typeMiel, formatPot, dateRecolte, dluo, numeroLot, quantite, dateGeneration, prixUnitaire)
- Created repository with findTop20ByOrderByDateGenerationDesc method
- Modified EtiquetteService to save history after PDF generation and retrieve recent history
- Added GET /etiquettes/historique endpoint to display history
- Added GET /etiquettes/regenerer endpoint to pre-fill form with previous parameters
- Created historique.html template with responsive table, empty state, and re-generate buttons
- Added dropdown navigation menu for Etiquettes section with Generer and Historique links
- All 225 tests pass

### Completion Notes
- All acceptance criteria implemented
- History limited to last 20 generations as specified
- Re-generate creates new lot number (not reuse) as per technical notes
- French labels and formatted dates (DD/MM/YYYY HH:mm) used throughout
- Empty state "Aucune etiquette generee pour le moment" implemented
