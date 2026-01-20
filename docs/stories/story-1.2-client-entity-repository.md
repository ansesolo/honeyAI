# Story 1.2: Client Entity & Repository with Soft Delete

**Epic:** Epic 1 - Foundation & Client Management
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 1.1

---

## User Story

**As a** developer,
**I want** to create the Client JPA entity with all required fields and a Spring Data repository,
**so that** I can persist and retrieve client data from SQLite with soft delete support to prevent accidental data loss.

---

## Acceptance Criteria

1. Client.java entity created in model package with fields: id (Long, @Id @GeneratedValue), nom (String, @NotBlank), telephone (String), email (String), adresse (String), notes (String, @Column(length=1000)), createdAt (LocalDateTime, @CreatedDate), updatedAt (LocalDateTime, @LastModifiedDate), deletedAt (LocalDateTime, nullable for soft delete)
2. Client entity annotated with @Entity, @Table(name="clients"), @Data (Lombok)
3. Soft delete helper method isDeleted() returns true if deletedAt is not null
4. ClientRepository interface created extending JpaRepository<Client, Long> with custom query methods: findByDeletedAtIsNullOrderByNomAsc(), findByIdAndDeletedAtIsNull(Long id), searchClients(@Param("search") String search) using @Query searching nom and telephone
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

- [ ] All acceptance criteria met
- [ ] Unit tests passing with 80%+ coverage
- [ ] Code committed to repository
