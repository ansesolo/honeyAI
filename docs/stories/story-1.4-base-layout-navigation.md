# Story 1.4: Base Layout & Navigation with Bootstrap

**Epic:** Epic 1 - Foundation & Client Management
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** Story 1.1

---

## User Story

**As a** developer,
**I want** to create a Thymeleaf base layout with Bootstrap 5 styling and navigation menu,
**so that** all pages have consistent branding, navigation, and responsive design without duplicating HTML.

---

## Acceptance Criteria

1. templates/fragments/layout.html created with Thymeleaf layout dialect containing: HTML5 boilerplate, Bootstrap 5.3 CDN links (CSS + JS), Font Awesome 6.4 CDN for icons, meta viewport for responsive, title block replaceable
2. Header fragment with logo "HoneyAI" and navigation menu (sidebar or top nav) with links: Tableau de bord, Clients, Commandes, Etiquettes, Achats - using Bootstrap nav components
3. Main content area with Thymeleaf th:block layout:fragment="content" for page-specific content
4. Footer fragment with simple text "HoneyAI 2026" centered
5. CSS custom styles in static/css/custom.css for: minimum font-size 16px body text, primary color palette (amber #F4B942, forest green #2D5016), button sizes minimum 44x44px touch targets
6. Navigation highlights active page using Bootstrap .active class
7. Layout fully responsive: collapses navigation on mobile (<768px), readable on desktop (1280x720+)
8. Home page (templates/home.html) created using layout with placeholder content "Bienvenue sur HoneyAI" to verify layout works
9. Application accessible at localhost:8080 displaying home page with navigation functional

---

## Technical Notes

- Bootstrap 5.3.2 CDN: https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css
- Font Awesome 6.4.2 CDN: https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css
- Color palette: Amber #F4B942, Forest Green #2D5016, Off-white #FAFAF8

---

## Definition of Done

- [x] All acceptance criteria met
- [x] Layout renders correctly on desktop and mobile
- [x] Navigation links work
- [ ] Code committed to repository

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5 (claude-opus-4-5-20251101)

### File List
| File | Action |
|------|--------|
| pom.xml | Modified (added thymeleaf-layout-dialect) |
| src/main/resources/templates/fragments/layout.html | Created |
| src/main/resources/static/css/custom.css | Created |
| src/main/resources/templates/home.html | Created |
| src/main/java/com/honeyai/controller/HomeController.java | Created |

### Completion Notes
- Base layout with Thymeleaf Layout Dialect for template inheritance
- Bootstrap 5.3.2 and Font Awesome 6.4.2 via CDN
- Navigation menu with 5 items: Tableau de bord, Clients, Commandes, Etiquettes, Achats
- Active page highlighting via activeMenu model attribute
- Custom CSS with brand colors (amber #F4B942, forest green #2D5016)
- 44px minimum touch targets for buttons and form controls
- Responsive navbar collapses on mobile (<768px)
- Footer with "HoneyAI 2026"
- Home page verified at localhost:8080 - returns 200 with all expected content
- All 32 existing tests still pass

### Change Log
| Date | Change |
|------|--------|
| 2026-01-21 | Initial implementation of base layout, navigation, and home page |

---

## QA Results

### Review Date: 2026-01-21

### Reviewed By: Quinn (Test Architect)

**Findings:**
- All 9 acceptance criteria verified
- Thymeleaf Layout Dialect properly configured
- Bootstrap 5.3.2 + Font Awesome 6.4.2 via CDN
- Navigation with 5 menu items, active highlighting works
- Responsive layout collapses on mobile
- Brand colors applied (amber/forest green)
- 44px touch targets enforced
- Footer "HoneyAI 2026" present
- localhost:8080 returns 200 with expected content

### Gate Status

Gate: PASS → docs/qa/gates/1.4-base-layout-navigation.yml
