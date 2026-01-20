# Story 3.1: PDF Service Foundation with Apache PDFBox

**Epic:** Epic 3 - Label Generation (Killer Feature)
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 1.1

---

## User Story

**As a** developer,
**I want** to set up Apache PDFBox library and create a PdfService foundation for generating PDF documents,
**so that** I have the technical infrastructure to generate label sheets with proper page layout and text rendering.

---

## Acceptance Criteria

1. Apache PDFBox dependency added to pom.xml (version 3.0.1 or latest stable 3.x)
2. PdfService.java created in service package with @Service annotation
3. Basic test method generateTestPdf() creates a simple PDF with text "HoneyAI Test PDF" and saves to ./test-output.pdf
4. PDF generation verified: file created, opens without errors in Adobe Reader/browser, displays text correctly
5. Helper methods created in PdfService: createDocument() returns PDDocument, createPage(PDRectangle pageSize) returns PDPage with A4 size, addTextToPage(PDPage page, String text, float x, float y, float fontSize) writes text at coordinates
6. Font loading: standard PDType1Font (Helvetica or Times) configured, or embedded TrueType font if needed for special characters
7. Unit test: generate PDF with multiple text blocks at different positions, verify file size >0 and no exceptions thrown
8. Error handling: try-catch around PDF operations with custom PdfGenerationException thrown if errors occur
9. Logging: log PDF generation start/completion with INFO level
10. Resources cleanup: PDDocument properly closed after generation (try-with-resources or finally block)

---

## Technical Notes

- Apache PDFBox 3.0.1 (API changed from 2.x)
- A4 size: PDRectangle.A4 (595 x 842 points)
- 1 point = 1/72 inch, 1mm = 2.83465 points
- Use try-with-resources for PDDocument

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Test PDF generates correctly
- [ ] Error handling in place
- [ ] Code committed to repository
