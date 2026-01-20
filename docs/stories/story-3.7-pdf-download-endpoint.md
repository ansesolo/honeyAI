# Story 3.7: PDF Download Endpoint & File Response

**Epic:** Epic 3 - Label Generation (Killer Feature)
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 3.6

---

## User Story

**As a** beekeeper (parent user),
**I want** to download the generated PDF file immediately after submitting the form,
**so that** I can open it and print the labels on my printer without additional steps.

---

## Acceptance Criteria

1. POST /etiquettes/generer endpoint in EtiquetteController accepts @Valid EtiquetteRequest, returns ResponseEntity<byte[]> with PDF content
2. Endpoint logic: call EtiquetteService.prepareEtiquetteData(request) to compute DLUO, lot number, fetch price, then call PdfService.generateEtiquettePdf(data, config, request.quantite)
3. Response headers: Content-Type: application/pdf, Content-Disposition: attachment; filename="etiquettes-{type}-{date}-{lot}.pdf" (e.g., "etiquettes-toutes-fleurs-2024-06-15-2024-TF-001.pdf")
4. Browser behavior: download prompt appears (or PDF opens in browser tab depending on browser settings)
5. Error handling: if PDF generation fails, return to form with error message "Erreur lors de la generation du PDF. Veuillez reessayer."
6. Success flow: form submit -> loading indicator (optional spinner) -> PDF downloads -> form reset with success message "PDF genere avec succes ({quantite} etiquettes)"
7. Validation: if form validation fails (e.g., quantite <1), return to form with field-level error messages
8. Price lookup: fetch current year tarif for selected product (type + format), if not found use default or display warning
9. Logging: log each PDF generation with parameters (type, format, quantite, user/timestamp) for audit trail
10. Performance: PDF generation completes in <5 seconds for up to 100 labels per NFR3

---

## Technical Notes

- ResponseEntity<byte[]> for binary PDF response
- Content-Disposition header for download filename
- Log generation parameters for traceability

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] PDF downloads correctly
- [ ] Error handling works
- [ ] Code committed to repository
