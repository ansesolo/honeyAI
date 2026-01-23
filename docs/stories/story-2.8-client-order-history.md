# Story 2.8: Client Detail - Display Order History

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Pending
**Priority:** P1 - High
**Depends On:** Story 2.7, Story 1.6

---

## User Story

**As a** beekeeper (parent user),
**I want** to see a client's complete order history on their detail page,
**so that** I can understand their purchasing patterns and reference past orders when they contact me.

---

## Acceptance Criteria

1. Update ClientController GET /clients/{id} endpoint to include client.orders in model (fetched via Client entity @OneToMany relation or OrderService.findByClientId())
2. Update templates/clients/detail.html to replace placeholder "Historique des commandes (a venir)" with actual order list table
3. Order history table displays: Date commande, Statut (badge), Montant total, Actions (Voir link to /orders/{id})
4. Orders sorted by date descending (most recent first)
5. Empty state if client has no orders: "Aucune commande pour ce client" with encouragement "Creez la premiere commande"
6. "Nouvelle commande pour ce client" button (previously disabled in Story 1.6) now active, links to /orders/new?clientId={id} (pre-selects client in form)
7. Summary stats above table: Total orders ({count}), Total depense ({sum of all PAID orders}), Derniere commande le {date}
8. Clicking order row or "Voir" navigates to order detail page
9. Table responsive: scrolls horizontally on mobile, stacks on very small screens
10. Performance: limit display to last 50 orders with "Voir toutes les commandes" link if more exist (pagination or "show more")

---

## Technical Notes

- Reuse status badge styling from order list
- Summary stats: only count PAYEE orders for "Total depense"
- Enable the previously disabled "Nouvelle commande" button from Story 1.6

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Order history displays correctly
- [ ] Summary stats accurate
- [ ] Code committed to repository
