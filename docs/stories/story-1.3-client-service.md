# Story 1.3: Client Service with Business Logic

**Epic:** Epic 1 - Foundation & Client Management
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 1.2

---

## User Story

**As a** developer,
**I want** to create a ClientService layer that handles business logic for client operations including soft delete,
**so that** controllers remain thin and business rules are centralized and testable.

---

## Acceptance Criteria

1. ClientService.java created in service package with @Service annotation and constructor injection of ClientRepository
2. Methods implemented: findAllActive() returns all non-deleted clients ordered by name, findById(Long id) returns Optional<Client> if not deleted, save(Client client) validates and persists (sets updatedAt), softDelete(Long id) sets deletedAt to now without physical deletion, searchClients(String search) delegates to repository search
3. Validation enforced: nom cannot be blank, throws custom ClientNotFoundException if client not found or soft-deleted when accessed by id
4. Unit tests with mocked repository: verify findAllActive excludes deleted clients, verify softDelete sets deletedAt and doesn't call repository.delete(), verify save updates updatedAt timestamp, verify searchClients handles empty/null search strings gracefully
5. Service methods transactional where needed (@Transactional on save/delete operations)
6. Test coverage: 80%+ on ClientService business logic

---

## Technical Notes

- Use constructor injection (not @Autowired field injection)
- Create ClientNotFoundException in exception package
- @Transactional for write operations

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Unit tests passing with 80%+ coverage
- [ ] Code committed to repository
