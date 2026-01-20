# Story 3.8: Label Generation History & Lot Number Tracking

**Epic:** Epic 3 - Label Generation (Killer Feature)
**Status:** Pending
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

- [ ] All acceptance criteria met
- [ ] History displays correctly
- [ ] Re-generate links work
- [ ] Code committed to repository
