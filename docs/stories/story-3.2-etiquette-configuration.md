# Story 3.2: Etiquette Configuration & Data Model

**Epic:** Epic 3 - Label Generation (Killer Feature)
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 3.1

---

## User Story

**As a** developer,
**I want** to create a configuration model for label parameters and exploitation information,
**so that** the PDF service can access label dimensions, exploitation details (SIRET, address), and layout settings without hardcoding values.

---

## Acceptance Criteria

1. EtiquetteConfig.java created as @ConfigurationProperties(prefix = "honeyai.etiquettes") class with fields: siret (String), nomApiculteur (String), adresse (String), telephone (String), dluoDureeJours (Integer, default 730), labelWidthMm (Float, default 60), labelHeightMm (Float, default 40), labelsPerRow (Integer, default 3), labelsPerColumn (Integer, default 7)
2. application.yml updated with section honeyai.etiquettes containing: siret: "XXXXXXXXXXX", nomApiculteur: "Exploitation Familiale", adresse: "123 Rue de la Ruche, 12345 Village", telephone: "06 XX XX XX XX", dluoDureeJours: 730, labelWidthMm: 60.0, labelHeightMm: 40.0, labelsPerRow: 3, labelsPerColumn: 7
3. EtiquetteRequest DTO created with fields: typeMiel (TypeMiel enum), formatPot (FormatPot enum: POT_500G, POT_1KG), dateRecolte (LocalDate), quantite (Integer @Min(1)), with validation annotations
4. FormatPot enum created with POT_500G("500g", 0.5), POT_1KG("1kg", 1.0) containing display label and weight in kg
5. EtiquetteData model class created with all computed fields for one label: typeMiel (String display), formatPot (String display), dateRecolte (String formatted), dluo (LocalDate computed), numeroLot (String generated), nomApiculteur, adresse, siret, telephone, prixUnitaire (BigDecimal from current year tarif)
6. Configuration validated at startup: @Validated annotation on EtiquetteConfig, application fails to start if required fields missing
7. Unit test: load EtiquetteConfig from test application.yml, verify all fields populated correctly
8. PdfService constructor-injected with EtiquetteConfig for accessing exploitation details

---

## Technical Notes

- @ConfigurationProperties requires @EnableConfigurationProperties on main class
- FormatPot enum: POT_500G("500g", 0.5), POT_1KG("1kg", 1.0)
- Default label sheet: 3 columns x 7 rows = 21 labels per A4 page

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Configuration loads correctly
- [ ] Validation works at startup
- [ ] Code committed to repository
