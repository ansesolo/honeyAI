# Story 2.4: Product Catalog & Tarif Management UI

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Pending
**Priority:** P1 - High
**Depends On:** Story 2.3, Story 1.4

---

## User Story

**As a** beekeeper (parent user),
**I want** to view and manage my product catalog with prices for each year,
**so that** I can prepare pricing for upcoming seasons and ensure accurate pricing on orders.

---

## Acceptance Criteria

1. ProduitController created with GET /produits endpoint returning "produits/list" view with all products and their current year tarifs
2. templates/produits/list.html created displaying: page title "Catalogue Produits", table with columns: Nom produit, Type (if miel), Unite, Prix 2024, Prix 2025, Actions (Modifier prix)
3. Current year (2026) prices highlighted or bolded to distinguish from future years
4. "Modifier Prix" button per product opens inline form or modal to edit tarif for selected year (dropdown: 2024, 2025, 2026, 2027), input for price (EUR), save updates TarifRepository
5. POST /produits/{id}/tarif endpoint accepts produitId, annee, prix, calls ProduitService.updateTarif(), redirects back to /produits with success message "Prix mis a jour pour {produit} en {annee}"
6. Products grouped or color-coded by type (Miel Toutes Fleurs, Miel Foret, etc.) for readability
7. Empty state if no products: "Aucun produit dans le catalogue" (should not occur with seed data)
8. Price formatting: display as "12,50 EUR" (French format with comma decimal separator)
9. Navigation link "Produits" added to sidebar menu (update layout.html from Epic 1)
10. Responsive: table scrolls horizontally on mobile, readable on desktop

---

## Technical Notes

- Use NumberFormat for French currency formatting
- Modal or inline form for tarif editing
- Consider showing price history or comparison

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Price editing works correctly
- [ ] French formatting applied
- [ ] Code committed to repository
