# Story 5.7: Final UX Polish & Accessibility Review

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Approved
**Priority:** P1 - High
**Depends On:** All previous stories

---

## User Story

**As a** beekeeper (parent user),
**I want** the application interface to be polished and consistent,
**so that** I feel confident using it.

---

## Acceptance Criteria

1. Font size audit: body 16px min, headings 20px min (h1-h3), WCAG AA compliance (4.5:1 contrast ratio)
2. Button size audit: 44x44px min touch targets, proper spacing between clickable elements
3. Consistent terminology throughout (French, no mix of English labels)
4. Navigation consistency: navbar works on all pages, active page highlighted correctly
5. Form labels: visible above fields, required fields marked with `*`
6. Loading indicators for PDF generation and backup operations (operations >1s)
7. Empty states on all list views (clients, orders, products, achats, etiquettes history, backups)
8. Color consistency with honey palette (--honey-amber, --forest-green, --off-white)
9. Responsive check at 1280x720 and 1920x1080 resolutions
10. Accessibility: keyboard navigation, aria-labels on icons, no color-only information

---

## Tasks / Subtasks

- [ ] Task 1: Font and typography audit (AC: 1)
  - [ ] Verify body font-size 16px in custom.css (already set)
  - [ ] Audit all h1/h2/h3 across templates - ensure minimum 20px (1.25rem)
  - [ ] Add CSS rules if any headings are below minimum
  - [ ] Check contrast ratios: forest-green (#2D5016) on off-white (#FAFAF8) - verify 4.5:1 minimum
  - [ ] Check contrast: white text on forest-green backgrounds (navbar, table headers, card headers)

- [ ] Task 2: Button and touch target audit (AC: 2)
  - [ ] Verify .btn min-width/min-height 44px in custom.css (already set)
  - [ ] Audit all icon-only buttons (edit, delete) - ensure they have sufficient padding
  - [ ] Check spacing between adjacent clickable elements (minimum 8px gap)

- [ ] Task 3: Terminology consistency audit (AC: 3)
  - [ ] Scan all templates for English labels and translate to French
  - [ ] Verify consistent terms: "Clients" (not "Customers"), "Commandes" (not "Orders"), "Produits", "Achats", "Etiquettes", "Sauvegarde"
  - [ ] Check button labels: "Enregistrer" (not "Save"), "Supprimer" (not "Delete"), "Modifier" (not "Edit")
  - [ ] Check page titles consistency

- [ ] Task 4: Navigation consistency audit (AC: 4)
  - [ ] Verify every controller sets `model.addAttribute("activeMenu", "...")` correctly
  - [ ] Verify all pages extend the layout correctly via `layout:decorate`
  - [ ] Test navbar renders on every page (clients, orders, products, achats, etiquettes, dashboard, backup, error pages)

- [ ] Task 5: Form labels and required fields audit (AC: 5)
  - [ ] Audit all form templates: labels must be above fields (not inline/floating)
  - [ ] Add `*` marker with CSS class `required` to all required field labels
  - [ ] Add CSS: `.required::after { content: " *"; color: red; }`
  - [ ] Ensure all `<input>` have associated `<label>` elements with `for` attribute

- [ ] Task 6: Loading indicators (AC: 6)
  - [ ] Add spinner/loading overlay for PDF generation button (etiquettes/form.html)
  - [ ] Add spinner for manual backup button (backup/manage.html)
  - [ ] Pattern: disable button + show spinner icon on click, re-enable on response
  - [ ] Use Bootstrap spinner: `<span class="spinner-border spinner-border-sm">`

- [ ] Task 7: Empty states audit (AC: 7)
  - [ ] Verify each list template has an empty state with `th:if="${#lists.isEmpty(items)}"`
  - [ ] Templates to check: clients/list, orders/list, products/list, achats/list, etiquettes/historique, backup/manage
  - [ ] Empty state message format: icon + French message + optional action button
  - [ ] Add missing empty states

- [ ] Task 8: Color consistency check (AC: 8)
  - [ ] Verify all custom colors use CSS variables (--honey-amber, --forest-green, --off-white)
  - [ ] Check for hardcoded colors in templates that should use variables
  - [ ] Verify badge colors are consistent and documented

- [ ] Task 9: Responsive verification (AC: 9)
  - [ ] Document responsive behavior at 1280x720 (common laptop)
  - [ ] Document responsive behavior at 1920x1080 (common desktop)
  - [ ] Fix any layout issues found (overlapping elements, text truncation)
  - [ ] Verify tables are scrollable on smaller viewports

- [ ] Task 10: Accessibility audit (AC: 10)
  - [ ] Add `aria-label` to all icon-only buttons and links
  - [ ] Verify all images have `alt` attributes
  - [ ] Check keyboard Tab order is logical on all pages
  - [ ] Ensure status/state information uses text + color (not color alone)
  - [ ] Add `role="status"` to dynamic content areas
  - [ ] Verify all interactive elements are focusable and have visible focus styles

---

## Dev Notes

### Current CSS State (custom.css)

Already implemented:
- Body font-size: 16px with line-height 1.5
- Button min-width/min-height: 44px
- Form controls min-height: 44px
- Color palette: --honey-amber (#F4B942), --forest-green (#2D5016), --off-white (#FAFAF8)
- Responsive media query for mobile
- Brand-colored alerts, cards, tables

### Navigation

The navigation is a **horizontal navbar** (not sidebar). The AC4 reference to "sidebar" is incorrect - audit the navbar instead. Each controller uses `model.addAttribute("activeMenu", "featureName")` and layout.html highlights with `th:classappend`.

### Empty State Pattern

Existing pattern used in some templates:
```html
<div th:if="${#lists.isEmpty(items)}" class="text-center py-5 text-muted">
    <i class="fas fa-inbox fa-3x mb-3"></i>
    <p class="fs-5">Aucun element trouve</p>
</div>
```

### Required Field Pattern

Add to custom.css:
```css
.required::after {
    content: " *";
    color: #dc3545;
}
```

### Loading Spinner Pattern
```html
<button type="submit" class="btn btn-primary" id="submitBtn">
    <span class="spinner-border spinner-border-sm d-none" id="spinner"></span>
    <span id="btnText">Generer</span>
</button>
<script>
document.getElementById('submitBtn').addEventListener('click', function() {
    document.getElementById('spinner').classList.remove('d-none');
    document.getElementById('btnText').textContent = 'Generation...';
    this.disabled = true;
});
</script>
```

### Relevant Source Tree
```
src/main/resources/
├── static/css/custom.css              (MODIFY - add required, heading, accessibility styles)
├── templates/
│   ├── fragments/layout.html          (VERIFY - nav consistency)
│   ├── clients/list.html              (AUDIT - empty state, labels, terminology)
│   ├── clients/form.html              (AUDIT - labels, required markers)
│   ├── clients/detail.html            (AUDIT - terminology)
│   ├── orders/list.html               (AUDIT - empty state, terminology)
│   ├── orders/form.html               (AUDIT - labels, required markers)
│   ├── orders/detail.html             (AUDIT - terminology)
│   ├── products/list.html             (AUDIT - empty state, terminology)
│   ├── achats/list.html               (AUDIT - empty state, labels)
│   ├── achats/form.html               (AUDIT - labels)
│   ├── etiquettes/form.html           (AUDIT - labels, loading spinner)
│   ├── etiquettes/historique.html     (AUDIT - empty state)
│   ├── backup/manage.html             (AUDIT - empty state, loading spinner)
│   ├── home.html                      (AUDIT - terminology)
│   └── error/*.html                   (AUDIT - accessibility)
```

### Testing

- **Primarily manual/visual testing**
- Describe audit findings in completion notes
- Use browser dev tools to verify font sizes, contrast, touch targets
- Test keyboard navigation: Tab through all pages, verify focus visible
- Verify aria-labels with screen reader or browser accessibility inspector
- Run existing test suite for regression after CSS/template changes

---

## Change Log

| Date | Version | Description | Author |
|------|---------|-------------|--------|
| 2026-01-30 | 1.0 | Story initiale depuis epic 5 | PO (Sarah) |
| 2026-01-30 | 2.0 | Story completee: 10 audit tasks, CSS state documented, patterns fournis, AC4 corrige (navbar pas sidebar) | PO (Sarah) |

---

## Dev Agent Record

### Agent Model Used
_To be filled by dev agent_

### Debug Log References
_To be filled by dev agent_

### Completion Notes List
_To be filled by dev agent_

### File List
_To be filled by dev agent_

---

## QA Results
_To be filled by QA agent_
