# Story 3.5: Multi-Label Sheet Generation

**Epic:** Epic 3 - Label Generation (Killer Feature)
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 3.4

---

## User Story

**As a** developer,
**I want** to generate a full A4 sheet with multiple labels arranged in a grid,
**so that** users can print labels efficiently on standard label sheets (e.g., 3 columns x 7 rows).

---

## Acceptance Criteria

1. PdfService method generateEtiquetteSheet(EtiquetteRequest request, EtiquetteConfig config) returns byte[] of PDF file
2. Sheet layout: A4 page (210mm x 297mm), labels arranged in grid based on config (labelsPerRow x labelsPerColumn, default 3x7 = 21 labels per page)
3. Label positioning: calculate x,y coordinates for each label in grid, accounting for margins (10mm top/left/right, 15mm bottom for printer safety)
4. Spacing between labels: 2mm horizontal gap, 2mm vertical gap between adjacent labels
5. Repeat label data: same EtiquetteData used for all labels on sheet (user requested N copies, generate full sheets + partial last sheet)
6. Multi-page support: if quantite > labels per sheet (e.g., 50 labels requested, 21 per sheet), generate 3 pages (21 + 21 + 8)
7. Partial sheet: last page contains only remaining labels (8 in example), positioned in grid starting top-left
8. Method signature: generateEtiquettePdf(EtiquetteRequest request) returns ResponseEntity<byte[]> with PDF content type and filename header
9. Integration test: request 25 labels, verify PDF has 2 pages, page 1 has 21 labels, page 2 has 4 labels, manually verify print preview looks correct
10. Performance: generation of 100-label PDF (5 pages) completes in <5 seconds per NFR3

---

## Technical Notes

- A4: 210mm x 297mm = 595 x 842 points
- Default grid: 3 columns x 7 rows = 21 labels per page
- Margins: 10mm top/left/right, 15mm bottom
- Gap between labels: 2mm horizontal, 2mm vertical

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Multi-page generation works
- [ ] Performance target met (<5s for 100 labels)
- [ ] Code committed to repository
