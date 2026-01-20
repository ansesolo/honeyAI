# Story 3.6: Label Generation Form UI

**Epic:** Epic 3 - Label Generation (Killer Feature)
**Status:** Pending
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

- [ ] All acceptance criteria met
- [ ] Form displays correctly
- [ ] Validation works
- [ ] Code committed to repository
