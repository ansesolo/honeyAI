# Story 2.1: Product & Price Entities with Year-based Pricing

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Ready for Review
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

- [x] All acceptance criteria met
- [x] Unit tests passing
- [x] Seed data loads correctly
- [ ] Code committed to repository

---

## Dev Agent Record

### File List

**New Files:**
- `src/main/java/com/honeyai/enums/HoneyType.java` - Honey type enum with French labels
- `src/main/java/com/honeyai/model/Product.java` - Product JPA entity
- `src/main/java/com/honeyai/model/Price.java` - Price JPA entity with year-based pricing
- `src/main/java/com/honeyai/repository/ProductRepository.java` - Product data access
- `src/main/java/com/honeyai/repository/PriceRepository.java` - Price data access
- `src/test/java/com/honeyai/enums/HoneyTypeTest.java` - Enum unit tests
- `src/test/java/com/honeyai/repository/ProductRepositoryTest.java` - Product repo tests
- `src/test/java/com/honeyai/repository/PriceRepositoryTest.java` - Price repo tests
- `src/test/resources/application-test.yml` - H2 test configuration

**Modified Files:**
- `src/main/java/com/honeyai/config/DataInitializer.java` - Added product seed data
- `pom.xml` - Added H2 test dependency

### Change Log

- Created HoneyType enum with TOUTES_FLEURS, FORET, CHATAIGNIER values and French display labels
- Created Product entity with @OneToMany relationship to Price
- Created Price entity with unique constraint on (product_id, price_year)
- Created ProductRepository with findAllByOrderByNameAsc()
- Created PriceRepository with findByProductIdAndYear() and findByYear()
- Added DataInitializer seed data: 8 products (6 honey + Cire + Reine) with 2024 prices
- Renamed 'year' column to 'price_year' to avoid H2 reserved keyword conflict
- Added H2 test database for reliable constraint testing

### Agent Model Used

Claude Opus 4.5

---

## QA Results

### Review Date: 2026-01-23

### Reviewed By: Quinn (Test Architect)

**Acceptance Criteria Review:**

| AC | Requirement | Status |
|----|-------------|--------|
| 1 | Product.java entity with id, name, type, unit fields | ✅ PASS |
| 2 | HoneyType enum with TOUTES_FLEURS, FORET, CHATAIGNIER + French labels | ✅ PASS |
| 3 | Price.java entity with unique constraint on (product_id, price_year) | ✅ PASS |
| 4 | @OneToMany/@ManyToOne relationships between Product and Price | ✅ PASS |
| 5 | ProductRepository.findAllByOrderByNameAsc() | ✅ PASS |
| 6 | PriceRepository.findByProductIdAndYear() and findByYear() | ✅ PASS |
| 7 | Hibernate creates tables in SQLite | ✅ PASS |
| 8 | Unit tests for products, prices, unique constraint | ✅ PASS |
| 9 | Seed data: 8 products with 2024 prices | ✅ PASS |

**Code Quality:**
- Follows coding standards (@Enumerated(EnumType.STRING), BigDecimal for money)
- Proper Lombok annotations
- Column renamed from 'year' to 'price_year' to avoid H2 reserved keyword
- H2 test database added for reliable constraint testing

**Test Coverage:**
- 17 tests for Story 2.1 entities/repositories
- Unique constraint violation properly tested

### Gate Status

Gate: PASS → docs/qa/gates/2.1-product-tarif-entities.yml
