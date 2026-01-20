# Story 3.4: Single Label PDF Layout & Rendering

**Epic:** Epic 3 - Label Generation (Killer Feature)
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 3.3

---

## User Story

**As a** developer,
**I want** to create the PDF layout logic for rendering a single honey label with all regulatory information,
**so that** I can generate compliant labels with proper text positioning, sizing, and formatting.

---

## Acceptance Criteria

1. PdfService method renderLabel(PDPage page, EtiquetteData data, float x, float y, float widthMm, float heightMm) renders one label at specified position
2. Label layout structure: Border rectangle (1pt black line), Logo/icon area (optional, top 10%), Product name section (type miel + format, bold, 20% height), Mandatory info section (nom, adresse, SIRET, tel, 40% height), DLUO line (15% height), Lot number + price (bottom 15% height)
3. Font sizes: product name 12pt bold, mandatory info 8pt regular, DLUO 9pt, lot/price 8pt - all adjusted to fit within label dimensions
4. Text content per label: "Miel de {type}" or "Miel Toutes Fleurs" (line 1), "Poids net: {formatPot}" (line 2), "{nomApiculteur}" (line 3), "{adresse}" (line 4), "SIRET: {siret}" (line 5), "Tel: {telephone}" (line 6), "A consommer avant fin: {dluo MM/YYYY}" (line 7), "Lot: {numeroLot}" (line 8), "Prix: {prix} EUR" (line 9)
5. Coordinate conversion: millimeters to PDF points (1mm = 2.83465 points), position calculations relative to page margins
6. Text alignment: centered for product name, left-aligned for contact info, right-aligned for price
7. Text wrapping: long addresses truncated or wrapped to fit label height
8. Unit test (visual verification): generate single label PDF with mock data, manually verify layout, spacing, readability
9. Border and spacing: 2mm padding inside label border, 1mm margin between text lines
10. Special characters: ensure French accents (e, e, o, etc.) render correctly in PDF fonts

---

## Technical Notes

- 1mm = 2.83465 PDF points
- Default label: 60mm x 40mm
- Use PDType1Font.HELVETICA and HELVETICA_BOLD
- Test French accents: e, e, a, u, o, i, c

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Label layout visually correct
- [ ] French accents render properly
- [ ] Code committed to repository
