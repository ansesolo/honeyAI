# Story 2.7: Order Detail View with Status Transitions

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 2.6

---

## User Story

**As a** beekeeper (parent user),
**I want** to view order details and transition it through workflow statuses (Commandee -> Recuperee -> Payee),
**so that** I can track the lifecycle of each order from creation to payment completion.

---

## Acceptance Criteria

1. GET /commandes/{id} returns "commandes/detail" view with Commande object including: client info, dateCommande, current statut, notes, list of lignes (produit, quantite, prix, sous-total), total calculated
2. templates/commandes/detail.html created displaying: page title "Commande #{id}", client info section (name, phone with link to client detail), date and current status badge (large, prominent), notes if present, table of ordered products (columns: Produit, Quantite, Prix unitaire, Sous-total), Total row (bold, larger font)
3. Status transition buttons displayed based on current status: if COMMANDEE show "Marquer comme Recuperee" (orange button), if RECUPEREE show "Marquer comme Payee" (green button), if PAYEE show "Payee" (disabled green badge, no button)
4. POST /commandes/{id}/statut endpoint accepts newStatut parameter, calls CommandeService.updateStatut(), redirects back to /commandes/{id} with success message "Statut mis a jour: {newStatut}"
5. Status transition validation: invalid transitions display error message "Transition invalide" and do not update status
6. "Modifier" button to edit commande (link to /commandes/{id}/edit, grey button) - edit form similar to create form but pre-populated
7. "Retour a la liste" link to /commandes
8. Future actions section (placeholder): "Imprimer bon de livraison" button disabled with tooltip "Disponible prochainement"
9. Timestamps: display "Creee le {date}", "Modifiee le {date}" at bottom
10. Responsive: readable on mobile/tablet, buttons stack vertically if needed

---

## Technical Notes

- Status workflow: COMMANDEE -> RECUPEREE -> PAYEE (forward only)
- Transition buttons should be prominent and clearly labeled
- Consider confirmation before status transition

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Status transitions work correctly
- [ ] Invalid transitions handled gracefully
- [ ] Code committed to repository
