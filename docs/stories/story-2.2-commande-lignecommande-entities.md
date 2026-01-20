# Story 2.2: Command & CommandLine Entities with Status Workflow

**Epic:** Epic 2 - Order Management & Product Catalog
**Status:** Pending
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

- [ ] All acceptance criteria met
- [ ] Cascade operations work correctly
- [ ] Unit tests passing
- [ ] Code committed to repository
