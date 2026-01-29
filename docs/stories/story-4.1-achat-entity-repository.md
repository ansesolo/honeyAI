# Story 4.1: Achat Entity & Repository for Purchase Tracking

**Epic:** Epic 4 - Financial Dashboard & Purchases
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** Story 1.1

---

## User Story

**As a** developer,
**I want** to create an Achat JPA entity and repository for tracking supply purchases,
**so that** I can persist and retrieve expense data needed for financial calculations and reporting.

---

## Acceptance Criteria

1. CategorieAchat enum created in enums package with values: CIRE, POTS, COUVERCLES, NOURRISSEMENT, AUTRE with French display labels
2. Achat.java entity created in model package with fields: id (Long), dateAchat (LocalDate, @NotNull), designation (String, @NotBlank), montant (BigDecimal, @NotNull @Positive), categorie (CategorieAchat, @Enumerated(STRING)), notes (String), createdAt (LocalDateTime)
3. Achat entity annotated with @Entity, @Table(name="achats"), @Data (Lombok)
4. AchatRepository interface created extending JpaRepository<Achat, Long> with custom query methods: findByDateAchatBetween(LocalDate start, LocalDate end), findByCategorie(CategorieAchat categorie), findAllByOrderByDateAchatDesc()
5. Hibernate creates achats table automatically in SQLite
6. Unit test for AchatRepository: save achat, retrieve by id, filter by date range, filter by category
7. At least 5 test achats inserted successfully covering different categories and date ranges
8. BigDecimal precision: 2 decimal places for montant (euros and cents)

---

## Technical Notes

- CategorieAchat display labels: Cire, Pots, Couvercles, Nourrissement, Autre
- BigDecimal scale 2 for currency
- No soft delete for purchases (hard delete with confirmation)

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Unit tests passing
- [x] Seed data loads correctly
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5

### File List
- `src/main/java/com/honeyai/enums/CategorieAchat.java` (NEW)
- `src/main/java/com/honeyai/model/Achat.java` (NEW)
- `src/main/java/com/honeyai/repository/AchatRepository.java` (NEW)
- `src/test/java/com/honeyai/repository/AchatRepositoryTest.java` (NEW)

### Change Log
- Created CategorieAchat enum with 5 values and French display labels
- Created Achat entity with all required fields, validation annotations, and auditing
- Created AchatRepository with findByDateAchatBetween, findByCategorie, findAllByOrderByDateAchatDesc
- Created 7 unit tests (all passing) covering save, findById, date range filter, category filter, ordering, empty results, BigDecimal precision
- Full regression: 217 tests passing, 0 failures

### Completion Notes
- Followed existing project conventions (Builder, AuditingEntityListener, @ActiveProfiles("test"))
- 5 seed achats in tests covering all 5 categories
- BigDecimal with precision=10, scale=2 for currency
