# Story 1.5: Client List & Search Interface

**Epic:** Epic 1 - Foundation & Client Management
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** Story 1.3, Story 1.4

---

## User Story

**As a** beekeeper (parent user),
**I want** to view a list of all my clients and search them by name or phone number,
**so that** I can quickly find and access client information without scrolling through a paper notebook.

---

## Acceptance Criteria

1. ClientController.java created with @Controller, @RequestMapping("/clients"), GET endpoint /clients returns "clients/list" view with model containing list of active clients
2. templates/clients/list.html created using base layout with: page title "Mes Clients" with user icon, prominent search bar (large input, placeholder "Rechercher un client par nom ou telephone..."), "Nouveau Client" button (green, large, top-right), table or card grid displaying clients with columns: Nom, Telephone, Email, Nombre de commandes (placeholder 0 for now), Actions (Voir, Modifier buttons)
3. Search bar submits GET request to /clients?search={query}, controller filters clients using ClientService.searchClients(), results displayed in same view
4. Empty state message displayed if no clients found: "Aucun client trouve" with icon
5. Success flash message displayed if redirected from save/delete (e.g., "Client enregistre avec succes") using Bootstrap alerts dismissible
6. Table/cards responsive: stacks on mobile, readable on desktop
7. Minimum 3 seed clients visible in list for demo purposes
8. Search returns results instantly (<1s) and highlights search term (optional but nice UX)
9. Font size 16px minimum, buttons 44x44px minimum, WCAG AA contrast ratios respected

---

## Technical Notes

- Use Thymeleaf th:each for client list iteration
- Flash messages via RedirectAttributes
- Search should be case-insensitive

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Search functionality works correctly
- [x] Responsive design verified
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5 (claude-opus-4-5-20251101)

### File List
| File | Action |
|------|--------|
| src/main/java/com/honeyai/controller/ClientController.java | Created |
| src/main/resources/templates/clients/list.html | Created |
| src/main/java/com/honeyai/config/DataInitializer.java | Created |

### Completion Notes
- ClientController with /clients endpoint, search query parameter support
- List template with search bar, table (desktop), cards (mobile), empty state
- DataInitializer creates 3 seed clients on startup (Dupont Jean, Martin Marie, Bernard Pierre)
- Search by name verified: "Dupont" returns 1 result
- Search by phone verified: "06 12" returns 1 result
- Flash message support ready (successMessage, errorMessage)
- Responsive: table hidden on mobile, cards hidden on desktop
- All 32 tests pass

### Change Log
| Date | Change |
|------|--------|
| 2026-01-21 | Initial implementation of client list and search interface |

---

## QA Results

### Review Date: 2026-01-21

### Reviewed By: Quinn (Test Architect)

**Findings:**
- All 9 acceptance criteria verified
- ClientController with @Controller, @RequestMapping("/clients") correct
- Search bar with correct placeholder, GET submission works
- "Nouveau Client" button present (btn-primary btn-lg)
- Table shows Nom, Telephone, Email, Commandes (0), Actions
- Empty state "Aucun client trouve" with icon present
- Flash message support implemented (successMessage, errorMessage)
- Responsive: table d-none d-md-block, cards d-md-none
- 3 seed clients visible via DataInitializer
- Search instant (<1s verified)

### Gate Status

Gate: PASS → docs/qa/gates/1.5-client-list-search.yml
