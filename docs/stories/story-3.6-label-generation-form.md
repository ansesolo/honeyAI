# Story 3.6: Label Generation Form UI

**Epic:** Epic 3 - Label Generation (Killer Feature)
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** Story 3.5, Story 1.4

---

## User Story

**As a** beekeeper (parent user),
**I want** to fill a simple form to generate honey labels by selecting miel type, pot format, harvest date, and quantity,
**so that** I can quickly create printable labels without manual data entry for each label.

---

## Acceptance Criteria

1. EtiquetteController created with GET /etiquettes endpoint returning "etiquettes/form" view with empty EtiquetteRequest model
2. templates/etiquettes/form.html created with: page title "Generer des Etiquettes", form fields: Type de miel (dropdown: Toutes Fleurs, Foret, Chataignier), Format pot (radio buttons: 500g, 1kg with large clickable labels), Date de recolte (datepicker, default today), Quantite d'etiquettes (number input, min=1, default=10, max=500)
3. Form layout: simple, vertical, large labels (18px), generous spacing, fields grouped logically
4. Visual preview section (optional): placeholder image or text showing "Apercu d'une etiquette" with sample label mockup (static for now)
5. Submit button: "Generer PDF" (large, green, prominent, icon: download), positioned prominently at bottom
6. Help text: below quantity field "Nombre d'etiquettes a imprimer (21 par feuille A4)"
7. Navigation: "Etiquettes" link added to sidebar menu (update layout.html)
8. Form responsive: usable on desktop primarily, stacks on tablet
9. Validation feedback: required field indicators (*), client-side validation (HTML5 required, min/max), friendly error messages if validation fails
10. Accessibility: labels properly associated with inputs, keyboard navigation works, WCAG AA contrast

---

## Technical Notes

- Radio buttons for format pot: use Bootstrap btn-group for large clickable buttons
- Date picker: HTML5 input type="date" or Bootstrap datepicker
- Default quantity: 10 labels (approximately half a sheet)

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Form displays correctly
- [x] Validation works
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5 (claude-opus-4-5-20251101)

### File List

| File | Action | Description |
|------|--------|-------------|
| `src/main/java/com/honeyai/controller/EtiquetteController.java` | Created | Controller with GET /etiquettes endpoint |
| `src/main/resources/templates/etiquettes/form.html` | Created | Label generation form with preview |
| `src/test/java/com/honeyai/controller/EtiquetteControllerTest.java` | Created | 9 tests for controller |

### Debug Log References
None - implementation completed without issues.

### Completion Notes
- EtiquetteController with GET /etiquettes returning form view
- Form fields: Type miel (dropdown), Format pot (radio buttons), Date recolte (datepicker), Quantite (number)
- Default values: Toutes Fleurs, 500g, today, 10 labels
- Visual preview section with sample label mockup
- Help text: "21 par feuille A4"
- Navigation: "Etiquettes" link already in layout sidebar
- Bootstrap styling with large labels and generous spacing
- HTML5 validation (required, min/max)
- All 205 project tests pass including 9 new tests

### Change Log
| Date | Change |
|------|--------|
| 2026-01-26 | Initial implementation of Story 3.6 |

---

## QA Results

### Review Date: 2026-01-26

### Reviewed By: Quinn (Test Architect)

#### Findings Summary

| Category | Status |
|----------|--------|
| Acceptance Criteria | All 10 met |
| Unit Tests | 9 new tests pass |
| Regression | 205 total tests pass |
| Form UI | Displays correctly |
| Validation | HTML5 required/min/max |

#### Implementation Review

- **EtiquetteController**: GET /etiquettes with default request
- **form.html**: Bootstrap form with preview panel
- **Radio buttons**: btn-group for format pot (500g, 1kg)
- **Navigation**: Link in sidebar (already existed)

### Gate Status

Gate: PASS → docs/qa/gates/3.6-label-generation-form.yml
