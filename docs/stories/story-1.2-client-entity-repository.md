# Story 1.2: Client Entity & Repository with Soft Delete

**Epic:** Epic 1 - Foundation & Client Management
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** Story 1.1

---

## User Story

**As a** developer,
**I want** to create the Client JPA entity with all required fields and a Spring Data repository,
**so that** I can persist and retrieve client data from SQLite with soft delete support to prevent accidental data loss.

---

## Acceptance Criteria

1. Client.java entity created in model package with fields: id (Long, @Id @GeneratedValue), name (String, @NotBlank), phone (String), email (String), address (String), notes (String, @Column(length=1000)), createdAt (LocalDateTime, @CreatedDate), updatedAt (LocalDateTime, @LastModifiedDate), deletedAt (LocalDateTime, nullable for soft delete)
2. Client entity annotated with @Entity, @Table(name="clients"), @Data (Lombok)
3. Soft delete helper method isDeleted() returns true if deletedAt is not null
4. ClientRepository interface created extending JpaRepository<Client, Long> with custom query methods: findByDeletedAtIsNullOrderByNameAsc(), findByIdAndDeletedAtIsNull(Long id), searchClients(@Param("search") String search) using @Query searching name and phone
5. Hibernate creates clients table automatically in SQLite with all columns and proper types
6. Unit test for ClientRepository: save client, retrieve by id, verify soft delete (deletedAt set, record still in DB), verify search query returns matching clients
7. At least 3 test clients inserted successfully and retrieved without errors

---

## Technical Notes

- Use Lombok @Data for boilerplate reduction
- Soft delete pattern: set deletedAt timestamp instead of physical deletion
- Search query should use LIKE for partial matching

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Unit tests passing with 80%+ coverage
- [ ] Code committed to repository

---

## Dev Agent Record

**Agent Model Used:** Claude Opus 4.5

### File List

| File | Action | Description |
|------|--------|-------------|
| src/main/java/com/honeyai/model/Client.java | Created | Client JPA entity with all fields, annotations, isDeleted() method |
| src/main/java/com/honeyai/repository/ClientRepository.java | Created | Repository with custom query methods |
| src/main/java/com/honeyai/config/JpaConfig.java | Created | JPA auditing configuration for @CreatedDate/@LastModifiedDate |
| src/test/java/com/honeyai/repository/ClientRepositoryTest.java | Created | 13 unit tests for ClientRepository |

### Change Log

| Date | Change |
|------|--------|
| 2026-01-20 | Implemented Client entity, ClientRepository, JpaConfig, and unit tests |

### Completion Notes

- All 7 acceptance criteria implemented and verified
- 13 unit tests created covering: save, findById, soft delete, search by name, search by telephone, case-insensitive search
- Tests use @AutoConfigureTestDatabase(replace = NONE) to use SQLite in tests
- JPA auditing enabled via JpaConfig for automatic timestamp population
- All 17 project tests pass (4 from story 1.1 + 13 new)

---

## QA Results

### Review Date: 2026-01-20

### Reviewed By: Quinn (Test Architect)

### Code Quality Assessment

Implementation is excellent. The Client entity follows all coding standards with proper Lombok annotations, JPA configuration, and validation constraints. The repository interface is well-designed with appropriate query methods that properly filter soft-deleted records. The JpaConfig correctly enables auditing for automatic timestamp population.

Test coverage is comprehensive with 13 tests covering all repository methods, edge cases (soft delete behavior, case-insensitive search), and the explicit AC7 requirement for 3+ test clients.

### Refactoring Performed

None required - code quality meets all standards.

### Compliance Check

- Coding Standards: ✓ Follows all conventions (Lombok @Data, @NotBlank validation, @Enumerated patterns)
- Project Structure: ✓ Files in correct packages (model/, repository/, config/)
- Testing Strategy: ✓ Integration tests at appropriate level (@DataJpaTest)
- All ACs Met: ✓ All 7 acceptance criteria fully implemented and tested

### Test Coverage by Acceptance Criteria

| AC | Implementation | Test Coverage | Status |
|----|----------------|---------------|--------|
| AC1 | Client.java fields | save_shouldPersistClientWithGeneratedId, findById | ✓ |
| AC2 | @Entity, @Table, @Data | All repository tests (implicit) | ✓ |
| AC3 | isDeleted() method | isDeleted_shouldReturnFalse/True | ✓ |
| AC4 | Repository queries | 6 tests for query methods | ✓ |
| AC5 | Hibernate table creation | All tests (table must exist) | ✓ |
| AC6 | Unit tests | 13 comprehensive tests | ✓ |
| AC7 | 3+ test clients | saveMultipleClients_shouldSucceed | ✓ |

### Improvements Checklist

- [x] All entity fields correctly annotated
- [x] Soft delete pattern properly implemented
- [x] Search query case-insensitive for name
- [x] JPA auditing enabled for timestamps
- [x] Comprehensive test coverage
- [ ] Consider adding @Email validation on email field (future story)
- [ ] Consider adding database index on name column (future optimization)

### Security Review

No security concerns. Input validation present via @NotBlank on required field. Soft delete pattern prevents accidental data loss - records are preserved and can be recovered if needed.

### Performance Considerations

No performance concerns for current implementation. Queries are appropriately indexed by primary key. Future consideration: add index on `name` column if client list grows large.

### Files Modified During Review

None - no refactoring required.

### Gate Status

Gate: PASS → docs/qa/gates/1.2-client-entity-repository.yml

### Recommended Status

[✓ Ready for Done] - All acceptance criteria met, comprehensive test coverage, code follows all standards.
