# Story 2.8: Client Detail - Display Order History

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Ready for Review
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

- [x] All acceptance criteria met
- [x] Order history displays correctly
- [x] Summary stats accurate
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5

### File List

**New Files:**
- `src/main/java/com/honeyai/dto/ClientOrderStatsDto.java` - DTO for client order statistics

**Modified Files:**
- `src/main/java/com/honeyai/repository/OrderRepository.java` - Added countByClientId and paginated findByClientIdOrderByOrderDateDesc methods
- `src/main/java/com/honeyai/service/OrderService.java` - Added findByClientIdWithLimit, countByClientId, getClientOrderStats methods
- `src/main/java/com/honeyai/controller/ClientController.java` - Injected OrderService, added orders and stats to detail view model
- `src/main/resources/templates/clients/detail.html` - Replaced placeholder with order history table, stats summary, active "Nouvelle commande" button
- `src/test/java/com/honeyai/controller/ClientControllerTest.java` - Added OrderService mock

### Completion Notes
- All 10 acceptance criteria implemented
- Order history displays with status badges (reused from order list)
- Summary stats show total orders, total paid amount (PAID orders only), last order date
- Empty state with encouragement message when no orders
- "Nouvelle commande" button now active and links to /orders/new?clientId={id}
- Clickable rows navigate to order detail
- Limited to 50 orders with "Voir toutes les commandes" link
- Responsive table with horizontal scroll
- All 117 tests pass

---

## QA Results

### Review Date: 2026-01-24

### Reviewed By: Quinn (QA Agent)

### Gate Status

Gate: PASS → docs/qa/gates/2.8-client-order-history.yml
