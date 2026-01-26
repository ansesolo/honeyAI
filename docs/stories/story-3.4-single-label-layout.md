# Story 3.4: Single Label PDF Layout & Rendering

**Epic:** Epic 3 - Label Generation (Killer Feature)
**Status:** Ready for Review
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

- [x] All acceptance criteria met
- [x] Label layout visually correct
- [x] French accents render properly
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5 (claude-opus-4-5-20251101)

### File List

| File | Action | Description |
|------|--------|-------------|
| `src/main/java/com/honeyai/service/PdfService.java` | Modified | Added renderLabel(), generateSingleLabelPdf(), helper methods for text alignment, truncation, and border drawing |
| `src/test/java/com/honeyai/service/PdfServiceTest.java` | Modified | Added 8 new tests for label rendering, accents, truncation, multiple labels |

### Debug Log References
None - implementation completed without issues.

### Completion Notes
- `renderLabel()` method renders label at specified position with configurable dimensions
- Layout structure: border (1pt), product name (12pt bold centered), weight (8pt centered), mandatory info (8pt left), DLUO (9pt), lot/price (8pt with right-aligned price)
- Text alignment: centered (product name, weight), left-aligned (contact info), right-aligned (price)
- Long addresses truncated with "..." to fit label width
- French accents (é, è, ê, à, ô, î, ç, ù) supported via WinAnsiEncoding
- Special characters (œ, curly quotes, em-dashes) sanitized for compatibility
- Coordinate conversion: mmToPoints() (1mm = 2.83465 points)
- All 181 project tests pass including 8 new tests

### Change Log
| Date | Change |
|------|--------|
| 2026-01-26 | Initial implementation of Story 3.4 |

---

## QA Results

### Review Date: 2026-01-26

### Reviewed By: Quinn (Test Architect)

#### Findings Summary

| Category | Status |
|----------|--------|
| Acceptance Criteria | All 10 met |
| Unit Tests | 8 new tests pass |
| Regression | 181 total tests pass |
| Coding Standards | Compliant |
| French Accents | Supported via WinAnsiEncoding |

#### Implementation Review

- **renderLabel()**: Renders complete label with all regulatory information
- **Layout**: Border + centered product name + left-aligned info + right-aligned price
- **Font Sizes**: 12pt bold (product), 8pt (info), 9pt (DLUO), 8pt (lot/price)
- **Text Wrapping**: Long addresses truncated with "..."
- **Coordinate System**: mm to points conversion (1mm = 2.83465pt)

### Gate Status

Gate: PASS → docs/qa/gates/3.4-single-label-layout.yml
