# Story 5.4: Confirmation Messages & Toast Notifications

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Ready for Review
**Priority:** P1 - High
**Depends On:** Story 1.4

---

## User Story

**As a** beekeeper (parent user),
**I want** to see clear confirmation messages after important actions,
**so that** I feel confident my actions succeeded.

---

## Acceptance Criteria

1. Centralized flash message fragment in layout.html using RedirectAttributes
2. Layout template displays flash messages at top of content area (success and error)
3. Standardize all controllers to use `successMessage` and `errorMessage` attribute names
4. Messages auto-dismiss after 5 seconds via JavaScript
5. Encouraging tone with Font Awesome icons (fa-check-circle for success, fa-exclamation-circle for error)
6. Consistent across all features (clients, orders, products, achats, etiquettes, backup)
7. Accessibility: role="alert", aria-live="polite", dismissible with keyboard (btn-close)

---

## Tasks / Subtasks

- [x] Task 1: Create centralized flash message fragment (AC: 1, 2, 5, 7)
  - [x] Create `templates/fragments/flash-messages.html` Thymeleaf fragment
  - [x] Success alert: Bootstrap `alert-success alert-dismissible fade show`, role="alert", aria-live="polite"
  - [x] Error alert: Bootstrap `alert-danger alert-dismissible fade show`, role="alert", aria-live="polite"
  - [x] Icons: `fa-check-circle` for success, `fa-exclamation-circle` for error
  - [x] Dismissible via `btn-close` button with aria-label="Fermer"
  - [x] Conditional display: `th:if="${successMessage}"` and `th:if="${errorMessage}"`

- [x] Task 2: Integrate fragment into layout.html (AC: 2)
  - [x] Add `th:replace="~{fragments/flash-messages :: alerts}"` inside `<main>` container, before the `layout:fragment="content"` div
  - [x] Ensure messages appear at top of content area on every page

- [x] Task 3: Add auto-dismiss JavaScript (AC: 4)
  - [x] Add script in layout.html `<th:block layout:fragment="scripts">` or inline after Bootstrap JS
  - [x] On DOMContentLoaded, select all `.alert` elements
  - [x] `setTimeout()` of 5000ms, then trigger Bootstrap `alert.close()` or add fade-out class
  - [x] Ensure dismiss animation is smooth (Bootstrap fade)

- [x] Task 4: Standardize controller flash attribute names (AC: 3, 6)
  - [x] `ProductController.java`: change `"success"` -> `"successMessage"` (line 51)
  - [x] `OrderController.java`: change `"success"` -> `"successMessage"` (lines 148, 180, 286) and `"error"` -> `"errorMessage"` (lines 184, 187)
  - [x] Verify `ClientController.java` already uses `"successMessage"` - OK
  - [x] Verify `AchatController.java` already uses `"successMessage"` - OK
  - [x] If BackupController exists (story 5.2), verify it uses `"successMessage"` / `"errorMessage"` - OK

- [x] Task 5: Remove per-template flash message blocks (AC: 6)
  - [x] Remove flash message divs from `clients/list.html` (lines ~12-21)
  - [x] Remove flash message divs from `products/list.html` (lines ~18-21)
  - [x] Remove flash message divs from `achats/list.html` (lines ~12-16)
  - [x] Remove flash message divs from `orders/detail.html` (lines ~13-22)
  - [x] Remove flash message divs from `backup/manage.html`
  - [x] Keep `orders/form.html` inline error div (model attribute, not flash - for form re-render context)
  - [x] Keep `etiquettes/form.html` JavaScript-controlled alerts (different mechanism - PDF download context)

- [x] Task 6: Write tests (AC: 4, 7)
  - [x] Verify accessibility: role="alert", aria-live, btn-close with keyboard - implemented in fragment
  - [x] Run full regression to ensure no flash messages are broken after standardization - 292 tests pass
  - [x] Updated controller tests (OrderControllerTest, ProductControllerTest) to use new attribute names

---

## Dev Notes

### Current State of Flash Messages

**Inconsistent attribute names across controllers:**
- `ProductController`: uses `"success"` (1 usage)
- `OrderController`: uses `"success"` (3 usages) and `"error"` (2 usages)
- `ClientController`: uses `"successMessage"` (2 usages) - STANDARD
- `AchatController`: uses `"successMessage"` (3 usages) - STANDARD

**Standardized names to adopt:** `"successMessage"` and `"errorMessage"`

### Current Template Implementation

Each template handles flash display independently:
- `clients/list.html`: checks `${successMessage}` and `${errorMessage}`
- `products/list.html`: checks `${success}`
- `achats/list.html`: checks `${successMessage}`
- `orders/detail.html`: checks `${success}` and `${error}`
- `orders/form.html`: checks `${error}` (model attribute for form re-render - DO NOT TOUCH)
- `etiquettes/form.html`: JavaScript-controlled alerts (DO NOT TOUCH)

After this story, all per-template flash blocks are replaced by a centralized fragment in layout.html.

### Existing CSS (custom.css)

Brand-specific alert styling already exists (lines 137-148):
```css
.alert-success { background-color: rgba(45, 80, 22, 0.1); border-color: var(--forest-green); color: var(--forest-green); }
.alert-warning { background-color: rgba(244, 185, 66, 0.2); border-color: var(--honey-amber); color: #856404; }
```
No CSS changes needed.

### Auto-Dismiss Pattern

No auto-dismiss exists currently. Add to layout.html scripts section:
```javascript
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.alert-dismissible').forEach(function(alert) {
        setTimeout(function() {
            var bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            bsAlert.close();
        }, 5000);
    });
});
```

### IMPORTANT: Things NOT to change

- `orders/form.html` error display (`${error}` model attribute for form re-render) - this is NOT a flash attribute
- `etiquettes/form.html` JavaScript-controlled success/error alerts - separate mechanism for PDF download
- These use model attributes, not flash redirectAttributes

### Relevant Source Tree
```
src/main/resources/templates/
├── fragments/
│   ├── layout.html                    (MODIFY - add fragment include + auto-dismiss JS)
│   └── flash-messages.html            (NEW - centralized flash fragment)
├── clients/list.html                  (MODIFY - remove flash divs)
├── products/list.html                 (MODIFY - remove flash divs)
├── achats/list.html                   (MODIFY - remove flash divs)
├── orders/detail.html                 (MODIFY - remove flash divs)
├── orders/form.html                   (NO CHANGE - model attribute, not flash)
├── etiquettes/form.html               (NO CHANGE - JS-controlled)
src/main/java/com/honeyai/controller/
├── ProductController.java             (MODIFY - rename flash attr)
├── OrderController.java               (MODIFY - rename flash attrs)
├── ClientController.java              (NO CHANGE - already standard)
├── AchatController.java               (NO CHANGE - already standard)
```

### Testing

- **Test framework:** JUnit 5 + Spring Boot Test
- **Naming convention:** `methodName_shouldExpectedBehavior_whenCondition`
- **Key test:** Run existing controller tests to verify flash attribute renames don't break anything
- **Manual tests:** Verify auto-dismiss in browser, verify keyboard dismiss (Tab to close button, Enter)
- **Regression:** Run full test suite after all changes

---

## Change Log

| Date | Version | Description | Author |
|------|---------|-------------|--------|
| 2026-01-30 | 1.0 | Story initiale depuis epic 5 | PO (Sarah) |
| 2026-01-30 | 2.0 | Story completee: audit flash existant, standardisation attrs, fragment centralise, auto-dismiss | PO (Sarah) |

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5 (claude-opus-4-5-20251101)

### Debug Log References
No debug issues encountered.

### Completion Notes List
- Created centralized flash-messages.html fragment with success/error alerts, Font Awesome icons, accessibility attributes (role="alert", aria-live="polite", aria-label="Fermer")
- Integrated fragment into layout.html before content area via th:replace
- Added auto-dismiss JS (5s timeout) using bootstrap.Alert.getOrCreateInstance().close() in existing DOMContentLoaded handler
- Standardized ProductController ("success" -> "successMessage") and OrderController ("success" -> "successMessage", "error" -> "errorMessage")
- Removed per-template flash blocks from clients/list.html, products/list.html, achats/list.html, orders/detail.html, backup/manage.html
- Preserved orders/form.html (model attribute) and etiquettes/form.html (JS-controlled) as instructed
- Updated 5 controller tests to match new attribute names
- Full regression: 292 tests pass, 0 failures

### File List
- `src/main/resources/templates/fragments/flash-messages.html` (NEW)
- `src/main/resources/templates/fragments/layout.html` (MODIFIED)
- `src/main/java/com/honeyai/controller/ProductController.java` (MODIFIED)
- `src/main/java/com/honeyai/controller/OrderController.java` (MODIFIED)
- `src/main/resources/templates/clients/list.html` (MODIFIED)
- `src/main/resources/templates/products/list.html` (MODIFIED)
- `src/main/resources/templates/achats/list.html` (MODIFIED)
- `src/main/resources/templates/orders/detail.html` (MODIFIED)
- `src/main/resources/templates/backup/manage.html` (MODIFIED)
- `src/test/java/com/honeyai/controller/OrderControllerTest.java` (MODIFIED)
- `src/test/java/com/honeyai/controller/ProductControllerTest.java` (MODIFIED)

---

## QA Results
_To be filled by QA agent_
