# Story 2.6: Create Order Form with Dynamic Product Lines

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 2.5

---

## User Story

**As a** beekeeper (parent user),
**I want** to create a new order by selecting a client and adding product lines with quantities,
**so that** I can record customer orders quickly as they come in, replacing my paper notebook workflow.

---

## Acceptance Criteria

1. GET /commandes/nouvelle returns "commandes/form" view with: empty Commande object, list of all active clients (for dropdown), list of all products with current year prices (for product selection)
2. templates/commandes/form.html created with: title "Nouvelle Commande", client selection dropdown (searchable/autocomplete preferred, or simple select), date commande field (datepicker, default today), notes textarea, dynamic product lines section
3. Product lines: initial empty row with: Produit dropdown (showing "Miel 500g Toutes Fleurs - 12,50 EUR"), Quantite input (number, min=1), Prix unitaire (readonly, auto-filled from product selection), Sous-total (readonly, calculated quantite x prix), "Supprimer ligne" button (icon), "+ Ajouter un produit" button to add new line
4. JavaScript (vanilla) handles: add/remove product lines dynamically, auto-fill prix unitaire when product selected, calculate and display sous-total per line, calculate and display total commande at bottom (sum of all sous-totals)
5. POST /commandes endpoint accepts: clientId, dateCommande, notes, List<LigneCommandeDto> (produitId, quantite, prixUnitaire), calls CommandeService.create(), redirects to /commandes/{id} (detail view) with success message "Commande creee avec succes"
6. Validation: at least one product line required, client selection required, quantite must be >=1, prix unitaire auto-populated but editable (for special discounts)
7. Form buttons: "Enregistrer" (green), "Annuler" (grey, back to /commandes)
8. Price override: allow user to manually edit prix unitaire if needed (e.g., discount), with visual indicator it differs from catalog price
9. Mobile responsive: form usable on tablet (future consideration), stacks vertically
10. Minimum 1 product line pre-filled on load (empty dropdown) for UX clarity

---

## Technical Notes

- Vanilla JavaScript for dynamic form manipulation
- Store product prices in data attributes for JS access
- LigneCommandeDto for form submission binding
- Consider URL param ?clientId={id} for pre-selection from client detail

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Dynamic lines work correctly (add/remove)
- [ ] Price calculations accurate
- [ ] Code committed to repository
