# Story 2.1: Product & Tarif Entities with Year-based Pricing

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 1.1

---

## User Story

**As a** developer,
**I want** to create Product and Tarif JPA entities with their repositories to support the product catalog with historical year-based pricing,
**so that** I can store honey products (types and formats) and track price changes across years while automatically applying current year prices to new orders.

---

## Acceptance Criteria

1. Produit.java entity created with fields: id (Long, @Id @GeneratedValue), nom (String, "Miel" / "Cire avec miel" / "Reine"), type (TypeMiel enum: TOUTES_FLEURS, FORET, CHATAIGNIER - nullable for non-honey products), unite (String: "pot 500g", "pot 1kg", "unite")
2. TypeMiel enum created in enums package with values: TOUTES_FLEURS, FORET, CHATAIGNIER, with display labels in French
3. Tarif.java entity created with fields: id (Long), produitId (Long, @ManyToOne foreign key to Produit), annee (Integer, year like 2024), prix (BigDecimal, 2 decimal places for euros), @UniqueConstraint(produitId, annee) to prevent duplicate prices for same product/year
4. Relation: Produit has @OneToMany List<Tarif> tarifs, Tarif has @ManyToOne Produit produit
5. ProduitRepository interface extending JpaRepository<Produit, Long> with method: findAllByOrderByNomAsc()
6. TarifRepository interface extending JpaRepository<Tarif, Long> with methods: findByProduitIdAndAnnee(Long produitId, Integer annee), findByAnnee(Integer annee) for getting all prices of a specific year
7. Hibernate creates produits and tarifs tables in SQLite with proper foreign key constraints
8. Unit tests: create products (Miel 500g Toutes Fleurs, Miel 1kg Foret, Cire avec miel, Reine), assign tarifs for years 2024 and 2025, verify retrieval by product and year, verify unique constraint violation if duplicate tarif inserted
9. Seed data: At least 6 products created (3 honey types x 2 formats + Cire + Reine) with 2024 prices populated

---

## Technical Notes

- BigDecimal for monetary values with scale 2
- TypeMiel enum should have getDisplayLabel() method
- Consider data.sql or @PostConstruct for seed data

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Unit tests passing
- [ ] Seed data loads correctly
- [ ] Code committed to repository
