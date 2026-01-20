# Story 1.4: Base Layout & Navigation with Bootstrap

**Epic:** Epic 1 - Foundation & Client Management
**Status:** Pending
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

- [ ] All acceptance criteria met
- [ ] Layout renders correctly on desktop and mobile
- [ ] Navigation links work
- [ ] Code committed to repository
