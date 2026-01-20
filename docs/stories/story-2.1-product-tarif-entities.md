# Story 2.1: Product & Price Entities with Year-based Pricing

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 1.1

---

## User Story

**As a** developer,
**I want** to create Product and Price JPA entities with their repositories to support the product catalog with historical year-based pricing,
**so that** I can store honey products (types and formats) and track price changes across years while automatically applying current year prices to new orders.

---

## Acceptance Criteria

1. Product.java entity created with fields: id (Long, @Id @GeneratedValue), name (String, "Miel" / "Cire avec miel" / "Reine"), type (HoneyType enum: TOUTES_FLEURS, FORET, CHATAIGNIER - nullable for non-honey products), unit (String: "pot 500g", "pot 1kg", "unite")
2. HoneyType enum created in enums package with values: TOUTES_FLEURS, FORET, CHATAIGNIER, with display labels in French
3. Price.java entity created with fields: id (Long), productId (Long, @ManyToOne foreign key to Product), year (Integer, year like 2024), price (BigDecimal, 2 decimal places for euros), @UniqueConstraint(productId, year) to prevent duplicate prices for same product/year
4. Relation: Product has @OneToMany List<Price> prices, Price has @ManyToOne Product product
5. ProductRepository interface extending JpaRepository<Product, Long> with method: findAllByOrderByNameAsc()
6. PriceRepository interface extending JpaRepository<Price, Long> with methods: findByProductIdAndYear(Long productId, Integer year), findByYear(Integer year) for getting all prices of a specific year
7. Hibernate creates products and prices tables in SQLite with proper foreign key constraints
8. Unit tests: create products (Miel 500g Toutes Fleurs, Miel 1kg Foret, Cire avec miel, Reine), assign prices for years 2024 and 2025, verify retrieval by product and year, verify unique constraint violation if duplicate price inserted
9. Seed data: At least 6 products created (3 honey types x 2 formats + Cire + Reine) with 2024 prices populated

---

## Technical Notes

- BigDecimal for monetary values with scale 2
- HoneyType enum should have getDisplayLabel() method
- Consider data.sql or @PostConstruct for seed data

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Unit tests passing
- [ ] Seed data loads correctly
- [ ] Code committed to repository
