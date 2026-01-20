# Story 2.5: Order List with Filters and Status Badges

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 2.3, Story 1.4

---

## User Story

**As a** beekeeper (parent user),
**I want** to view a list of all orders with filtering by year and status,
**so that** I can quickly find orders that need attention (e.g., all "Commandee" orders to prepare) and review historical orders.

---

## Acceptance Criteria

1. CommandeController created with GET /commandes endpoint returning "commandes/list" view with all commandes, model includes: list of commandes, current year, list of years (distinct from commandes.dateCommande for filter dropdown), list of statuts (enum values)
2. templates/commandes/list.html created displaying: page title "Commandes", filter bar with dropdowns: Annee (default: current year), Statut (default: Tous), "Filtrer" button, "Nouvelle Commande" button (green, prominent, top-right)
3. Orders table with columns: No (id), Client (nom), Date commande (DD/MM/YYYY), Statut (badge colored: COMMANDEE=blue, RECUPEREE=orange, PAYEE=green), Montant total (calculated via CommandeService.calculateTotal()), Actions (Voir)
4. Filtering: GET /commandes?annee=2024&statut=COMMANDEE filters results server-side using CommandeService methods, results update in same view
5. Status badges styled with Bootstrap badge component and appropriate colors for quick visual scanning
6. Orders sorted by date descending (most recent first) within filtered results
7. Empty state if no orders match filters: "Aucune commande trouvee pour les filtres selectionnes"
8. "Tous" option in statut filter shows all statuses
9. Navigation link "Commandes" added to sidebar menu (update layout.html)
10. Montant total formatted as French currency "123,45 EUR"

---

## Technical Notes

- Badge colors: COMMANDEE=primary (blue), RECUPEREE=warning (orange), PAYEE=success (green)
- Use @RequestParam for filter parameters with defaults
- Consider caching calculateTotal results for list performance

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Filters work correctly
- [ ] Status badges display properly
- [ ] Code committed to repository
