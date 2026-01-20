# Epic 3: Label Generation (Killer Feature)

**Epic Goal:** Développer la fonctionnalité de génération automatique d'étiquettes réglementaires pour pots de miel au format PDF, éliminant le processus manuel chronophage actuel (15 minutes pour 10 étiquettes réduit à moins de 2 minutes).

## Story 3.1: PDF Service Foundation with Apache PDFBox

**As a** developer,
**I want** to set up Apache PDFBox library and create a PdfService foundation for generating PDF documents,
**so that** I have the technical infrastructure to generate label sheets with proper page layout and text rendering.

### Acceptance Criteria:

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

## Story 3.2: Etiquette Configuration & Data Model

**As a** developer,
**I want** to create a configuration model for label parameters and exploitation information,
**so that** the PDF service can access label dimensions, exploitation details (SIRET, address), and layout settings without hardcoding values.

### Acceptance Criteria:

1. EtiquetteConfig.java created as @ConfigurationProperties(prefix = "honeyai.etiquettes") class with fields: siret (String), nomApiculteur (String), adresse (String), telephone (String), dluoDureeJours (Integer, default 730), labelWidthMm (Float, default 60), labelHeightMm (Float, default 40), labelsPerRow (Integer, default 3), labelsPerColumn (Integer, default 7)
2. application.yml updated with section honeyai.etiquettes containing: siret: "XXXXXXXXXXX", nomApiculteur: "Exploitation Familiale", adresse: "123 Rue de la Ruche, 12345 Village", telephone: "06 XX XX XX XX", dluoDureeJours: 730, labelWidthMm: 60.0, labelHeightMm: 40.0, labelsPerRow: 3, labelsPerColumn: 7
3. EtiquetteRequest DTO created with fields: typeMiel (TypeMiel enum), formatPot (FormatPot enum: POT_500G, POT_1KG), dateRecolte (LocalDate), quantite (Integer @Min(1)), with validation annotations
4. FormatPot enum created with POT_500G("500g", 0.5), POT_1KG("1kg", 1.0) containing display label and weight in kg
5. EtiquetteData model class created with all computed fields for one label: typeMiel (String display), formatPot (String display), dateRecolte (String formatted), dluo (LocalDate computed), numeroLot (String generated), nomApiculteur, adresse, siret, telephone, prixUnitaire (BigDecimal from current year tarif)
6. Configuration validated at startup: @Validated annotation on EtiquetteConfig, application fails to start if required fields missing
7. Unit test: load EtiquetteConfig from test application.yml, verify all fields populated correctly
8. PdfService constructor-injected with EtiquetteConfig for accessing exploitation details

## Story 3.3: DLUO Calculation & Lot Number Generation Logic

**As a** developer,
**I want** to implement business logic for calculating DLUO (Best Before Date) and generating unique batch numbers,
**so that** labels display correct regulatory information automatically without manual calculation errors.

### Acceptance Criteria:

1. EtiquetteService.java created with method calculateDluo(LocalDate dateRecolte, Integer dureeDays) returns LocalDate = dateRecolte.plusDays(dureeDays), formatted as MM/YYYY (month and year only per French regulation)
2. DLUO calculation uses dluoDureeJours from config (default 730 days = 2 years exactly)
3. Method generateNumeroLot(TypeMiel typeMiel, LocalDate dateRecolte) generates format: "YYYY-{TYPE}-NNN" where YYYY = harvest year, TYPE = miel type abbreviation (TF=Toutes Fleurs, FOR=Forêt, CHA=Châtaignier), NNN = sequential number per year/type
4. Lot number sequencing: query database (new table lots_etiquettes or counter in config) to get next sequential number for year+type combination, increment and store
5. LotsEtiquettes entity (optional simple approach): fields id, annee (Integer), typeMiel (String), dernierNumero (Integer), with unique constraint on (annee, typeMiel)
6. LotsEtiquettesRepository with method: findByAnneeAndTypeMiel(Integer annee, String typeMiel)
7. EtiquetteService.generateNumeroLot() logic: fetch or create LotsEtiquettes for current year+type, increment dernierNumero, save, return formatted lot number
8. Unit tests: calculateDluo(2024-06-15, 730) returns 2026-06-15 formatted "06/2026", generateNumeroLot(TOUTES_FLEURS, 2024-06-15) returns "2024-TF-001", second call returns "2024-TF-002", different type returns "2024-FOR-001"
9. Edge case tests: DLUO calculation across year boundaries, lot number rollover to new year (2025-TF-001 after 2024-TF-999)
10. DLUO display format: French regulation requires "À consommer de préférence avant fin: MM/YYYY" (month/year, not full date)

## Story 3.4: Single Label PDF Layout & Rendering

**As a** developer,
**I want** to create the PDF layout logic for rendering a single honey label with all regulatory information,
**so that** I can generate compliant labels with proper text positioning, sizing, and formatting.

### Acceptance Criteria:

1. PdfService method renderLabel(PDPage page, EtiquetteData data, float x, float y, float widthMm, float heightMm) renders one label at specified position
2. Label layout structure: Border rectangle (1pt black line), Logo/icon area (optional, top 10%), Product name section (type miel + format, bold, 20% height), Mandatory info section (nom, adresse, SIRET, tel, 40% height), DLUO line (15% height), Lot number + price (bottom 15% height)
3. Font sizes: product name 12pt bold, mandatory info 8pt regular, DLUO 9pt, lot/price 8pt - all adjusted to fit within label dimensions
4. Text content per label: "Miel de {type}" or "Miel Toutes Fleurs" (line 1), "Poids net: {formatPot}" (line 2), "{nomApiculteur}" (line 3), "{adresse}" (line 4), "SIRET: {siret}" (line 5), "Tél: {telephone}" (line 6), "À consommer avant fin: {dluo MM/YYYY}" (line 7), "Lot: {numeroLot}" (line 8), "Prix: {prix} €" (line 9)
5. Coordinate conversion: millimeters to PDF points (1mm = 2.83465 points), position calculations relative to page margins
6. Text alignment: centered for product name, left-aligned for contact info, right-aligned for price
7. Text wrapping: long addresses truncated or wrapped to fit label height
8. Unit test (visual verification): generate single label PDF with mock data, manually verify layout, spacing, readability
9. Border and spacing: 2mm padding inside label border, 1mm margin between text lines
10. Special characters: ensure French accents (é, è, ô, etc.) render correctly in PDF fonts

## Story 3.5: Multi-Label Sheet Generation

**As a** developer,
**I want** to generate a full A4 sheet with multiple labels arranged in a grid,
**so that** users can print labels efficiently on standard label sheets (e.g., 3 columns × 7 rows).

### Acceptance Criteria:

1. PdfService method generateEtiquetteSheet(EtiquetteRequest request, EtiquetteConfig config) returns byte[] of PDF file
2. Sheet layout: A4 page (210mm × 297mm), labels arranged in grid based on config (labelsPerRow × labelsPerColumn, default 3×7 = 21 labels per page)
3. Label positioning: calculate x,y coordinates for each label in grid, accounting for margins (10mm top/left/right, 15mm bottom for printer safety)
4. Spacing between labels: 2mm horizontal gap, 2mm vertical gap between adjacent labels
5. Repeat label data: same EtiquetteData used for all labels on sheet (user requested N copies, generate full sheets + partial last sheet)
6. Multi-page support: if quantite > labels per sheet (e.g., 50 labels requested, 21 per sheet), generate 3 pages (21 + 21 + 8)
7. Partial sheet: last page contains only remaining labels (8 in example), positioned in grid starting top-left
8. Method signature: generateEtiquettePdf(EtiquetteRequest request) returns ResponseEntity<byte[]> with PDF content type and filename header
9. Integration test: request 25 labels, verify PDF has 2 pages, page 1 has 21 labels, page 2 has 4 labels, manually verify print preview looks correct
10. Performance: generation of 100-label PDF (5 pages) completes in <5 seconds per NFR3

## Story 3.6: Label Generation Form UI

**As a** beekeeper (parent user),
**I want** to fill a simple form to generate honey labels by selecting miel type, pot format, harvest date, and quantity,
**so that** I can quickly create printable labels without manual data entry for each label.

### Acceptance Criteria:

1. EtiquetteController created with GET /etiquettes endpoint returning "etiquettes/form" view with empty EtiquetteRequest model
2. templates/etiquettes/form.html created with: page title "Générer des Étiquettes", form fields: Type de miel (dropdown: Toutes Fleurs, Forêt, Châtaignier), Format pot (radio buttons: 500g, 1kg with large clickable labels), Date de récolte (datepicker, default today), Quantité d'étiquettes (number input, min=1, default=10, max=500)
3. Form layout: simple, vertical, large labels (18px), generous spacing, fields grouped logically
4. Visual preview section (optional): placeholder image or text showing "Aperçu d'une étiquette" with sample label mockup (static for now)
5. Submit button: "Générer PDF" (large, green, prominent, icon: download), positioned prominently at bottom
6. Help text: below quantity field "Nombre d'étiquettes à imprimer (21 par feuille A4)"
7. Navigation: "Étiquettes" link added to sidebar menu (update layout.html)
8. Form responsive: usable on desktop primarily, stacks on tablet
9. Validation feedback: required field indicators (*), client-side validation (HTML5 required, min/max), friendly error messages if validation fails
10. Accessibility: labels properly associated with inputs, keyboard navigation works, WCAG AA contrast

## Story 3.7: PDF Download Endpoint & File Response

**As a** beekeeper (parent user),
**I want** to download the generated PDF file immediately after submitting the form,
**so that** I can open it and print the labels on my printer without additional steps.

### Acceptance Criteria:

1. POST /etiquettes/generer endpoint in EtiquetteController accepts @Valid EtiquetteRequest, returns ResponseEntity<byte[]> with PDF content
2. Endpoint logic: call EtiquetteService.prepareEtiquetteData(request) to compute DLUO, lot number, fetch price, then call PdfService.generateEtiquettePdf(data, config, request.quantite)
3. Response headers: Content-Type: application/pdf, Content-Disposition: attachment; filename="etiquettes-{type}-{date}-{lot}.pdf" (e.g., "etiquettes-toutes-fleurs-2024-06-15-2024-TF-001.pdf")
4. Browser behavior: download prompt appears (or PDF opens in browser tab depending on browser settings)
5. Error handling: if PDF generation fails, return to form with error message "Erreur lors de la génération du PDF. Veuillez réessayer."
6. Success flow: form submit → loading indicator (optional spinner) → PDF downloads → form reset with success message "PDF généré avec succès ({quantite} étiquettes)"
7. Validation: if form validation fails (e.g., quantité <1), return to form with field-level error messages
8. Price lookup: fetch current year tarif for selected product (type + format), if not found use default or display warning
9. Logging: log each PDF generation with parameters (type, format, quantite, user/timestamp) for audit trail
10. Performance: PDF generation completes in <5 seconds for up to 100 labels per NFR3

## Story 3.8: Label Generation History & Lot Number Tracking

**As a** beekeeper (parent user),
**I want** to see a history of recently generated label batches with their lot numbers,
**so that** I can reference past generations and understand which lot numbers have been used for traceability.

### Acceptance Criteria:

1. HistoriqueEtiquettes entity created with fields: id, typeMiel, formatPot, dateRecolte, dluo, numeroLot, quantite, dateGeneration (timestamp), prixUnitaire
2. HistoriqueEtiquettesRepository with method: findTop20ByOrderByDateGenerationDesc() for recent history
3. EtiquetteService saves history record after each successful PDF generation
4. GET /etiquettes/historique endpoint returns "etiquettes/historique" view with list of recent generations
5. templates/etiquettes/historique.html displaying: page title "Historique des Étiquettes", table with columns: Date génération, Type miel, Format, Date récolte, DLUO, Lot, Quantité, Actions (Re-générer)
6. "Re-générer" button: links back to /etiquettes form pre-filled with same parameters (type, format, date, quantite) allowing quick regeneration with new lot number
7. Navigation: link "Historique" added below "Étiquettes" in sidebar or as tab on /etiquettes page
8. Empty state: "Aucune étiquette générée pour le moment" if no history
9. History limited to last 20-50 generations (pagination not needed for MVP, just recent history)
10. Display formatted dates (DD/MM/YYYY HH:mm) and French labels for all fields

---
