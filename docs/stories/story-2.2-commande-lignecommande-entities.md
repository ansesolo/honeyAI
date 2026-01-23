# Story 2.2: Command & CommandLine Entities with Status Workflow

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** Story 2.1, Story 1.2

---

## User Story

**As a** developer,
**I want** to create Command and CommandLine JPA entities with status management and order line items,
**so that** I can persist complete orders linking clients to products with quantities and prices, tracking their progression through the business workflow.

---

## Acceptance Criteria

1. CommandStatus enum created in enums package with values: ORDER, RECOVERED, PAID with French display labels ("Commandée", "Recuperée", "Payée")
2. Command.java entity created with fields: id (Long), clientId (Long, @ManyToOne to Client), dateCommand (LocalDate, @NotNull), status (CommandStatus, @Enumerated(STRING), default COMMANDEE), notes (String, @Column(length=1000)), createdAt (LocalDateTime), updatedAt (LocalDateTime)
3. CommandLine.java entity created with fields: id (Long), commandId (Long, @ManyToOne to Command), productId (Long, @ManyToOne to Product), quantity (Integer, @Min(1)), unitPrice (BigDecimal, price at time of order for historical accuracy)
4. Relation: Command has @OneToMany(cascade=ALL, orphanRemoval=true) List<CommandLine> lines, CommandLine has @ManyToOne Command command and @ManyToOne Product product
5. Relation: Client has @OneToMany List<Command> commands (update Client.java from Epic 1)
6. CommandRepository interface extending JpaRepository<Command, Long> with methods: findByClientIdOrderByDateCommandDesc(Long clientId), findByStatus(CommandStatus status), findByDateCommandBetween(LocalDate start, LocalDate end) for filtering
7. CommandLineRepository interface extending JpaRepository<CommandLine, Long> (standard CRUD sufficient)
8. Hibernate creates Commands and command_lines tables with proper foreign keys and cascade rules
9. Unit tests: create command with 2-3 lines, verify cascade save (lines auto-saved with command), verify orphan removal (delete line removes it), verify status enum persists as string, verify client.commands bidirectional relation works

---

## Technical Notes

- cascade=ALL ensures lines are saved with command
- orphanRemoval=true deletes lines when removed from list
- unitPrice captures price at order time (historical)

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Cascade operations work correctly
- [x] Unit tests passing
- [ ] Code committed to repository

---

## Dev Agent Record

### File List

**New Files:**
- `src/main/java/com/honeyai/enums/StatutCommande.java` - Order status enum (COMMANDEE, RECUPEREE, PAYEE)
- `src/main/java/com/honeyai/model/Commande.java` - Order entity with client relation and status
- `src/main/java/com/honeyai/model/LigneCommande.java` - Order line entity with product and price
- `src/main/java/com/honeyai/repository/CommandeRepository.java` - Order data access with filtering methods
- `src/main/java/com/honeyai/repository/LigneCommandeRepository.java` - Order line data access
- `src/test/java/com/honeyai/enums/StatutCommandeTest.java` - Status enum tests
- `src/test/java/com/honeyai/repository/CommandeRepositoryTest.java` - Order repository tests (cascade, orphan removal)

**Modified Files:**
- `src/main/java/com/honeyai/model/Client.java` - Added @OneToMany commandes relationship

### Change Log

- Created StatutCommande enum with French display labels (Commandée, Récupérée, Payée)
- Created Commande entity with cascade ALL and orphanRemoval for lignes
- Created LigneCommande entity with prixUnitaire for historical price capture
- Created CommandeRepository with findByClientIdOrderByDateCommandeDesc, findByStatut, findByDateCommandeBetween
- Created LigneCommandeRepository with standard CRUD
- Updated Client entity to add bidirectional @OneToMany commandes relationship
- Added 12 new tests verifying cascade save, orphan removal, status string persistence

### Agent Model Used

Claude Opus 4.5

---

## QA Results

### Review Date: 2026-01-23

### Reviewed By: Quinn (Test Architect)

**Acceptance Criteria Review:**

| AC | Requirement | Status |
|----|-------------|--------|
| 1 | StatutCommande enum with COMMANDEE, RECUPEREE, PAYEE + French labels | ✅ PASS |
| 2 | Commande.java entity with all required fields | ✅ PASS |
| 3 | LigneCommande.java entity with prixUnitaire | ✅ PASS |
| 4 | @OneToMany(cascade=ALL, orphanRemoval=true) relationships | ✅ PASS |
| 5 | Client @OneToMany commandes bidirectional relation | ✅ PASS |
| 6 | CommandeRepository with filtering methods | ✅ PASS |
| 7 | LigneCommandeRepository standard CRUD | ✅ PASS |
| 8 | Hibernate creates tables with proper FK/cascade | ✅ PASS |
| 9 | Unit tests for cascade, orphan removal, status persistence | ✅ PASS |

**Code Quality:**
- Follows coding standards (French naming: Commande, LigneCommande, StatutCommande)
- Uses @Enumerated(EnumType.STRING) per standards
- BigDecimal for monetary values
- Proper Lombok annotations (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)
- Auditing with @CreatedDate/@LastModifiedDate

**Test Coverage:**
- 12 new tests added
- Cascade save verified
- Orphan removal verified
- Status string persistence verified
- Repository query methods tested

### Gate Status

Gate: PASS → docs/qa/gates/2.2-commande-lignecommande-entities.yml
