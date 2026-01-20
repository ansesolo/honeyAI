# Story 2.2: Commande & LigneCommande Entities with Status Workflow

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 2.1, Story 1.2

---

## User Story

**As a** developer,
**I want** to create Commande and LigneCommande JPA entities with status management and order line items,
**so that** I can persist complete orders linking clients to products with quantities and prices, tracking their progression through the business workflow.

---

## Acceptance Criteria

1. StatutCommande enum created in enums package with values: COMMANDEE, RECUPEREE, PAYEE with French display labels ("Commandee", "Recuperee", "Payee")
2. Commande.java entity created with fields: id (Long), clientId (Long, @ManyToOne to Client), dateCommande (LocalDate, @NotNull), statut (StatutCommande, @Enumerated(STRING), default COMMANDEE), notes (String, @Column(length=1000)), createdAt (LocalDateTime), updatedAt (LocalDateTime)
3. LigneCommande.java entity created with fields: id (Long), commandeId (Long, @ManyToOne to Commande), produitId (Long, @ManyToOne to Produit), quantite (Integer, @Min(1)), prixUnitaire (BigDecimal, price at time of order for historical accuracy)
4. Relation: Commande has @OneToMany(cascade=ALL, orphanRemoval=true) List<LigneCommande> lignes, LigneCommande has @ManyToOne Commande commande and @ManyToOne Produit produit
5. Relation: Client has @OneToMany List<Commande> commandes (update Client.java from Epic 1)
6. CommandeRepository interface extending JpaRepository<Commande, Long> with methods: findByClientIdOrderByDateCommandeDesc(Long clientId), findByStatut(StatutCommande statut), findByDateCommandeBetween(LocalDate start, LocalDate end) for filtering
7. LigneCommandeRepository interface extending JpaRepository<LigneCommande, Long> (standard CRUD sufficient)
8. Hibernate creates commandes and lignes_commande tables with proper foreign keys and cascade rules
9. Unit tests: create commande with 2-3 lignes, verify cascade save (lignes auto-saved with commande), verify orphan removal (delete ligne removes it), verify status enum persists as string, verify client.commandes bidirectional relation works

---

## Technical Notes

- cascade=ALL ensures lignes are saved with commande
- orphanRemoval=true deletes lignes when removed from list
- prixUnitaire captures price at order time (historical)

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Cascade operations work correctly
- [ ] Unit tests passing
- [ ] Code committed to repository
