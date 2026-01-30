# Story 5.3: Global Exception Handler & User-Friendly Error Pages

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Approved
**Priority:** P0 - Critical Path
**Depends On:** Story 1.4

---

## User Story

**As a** developer,
**I want** to implement a global exception handler with friendly error pages,
**so that** technical errors don't confuse non-technical users.

---

## Acceptance Criteria

1. GlobalExceptionHandler.java with @ControllerAdvice handles all exception types
2. @ExceptionHandler methods for: ClientNotFoundException (404), InvalidStatusTransitionException (409), PriceNotFoundException (404), PdfGenerationException (500), generic Exception (500)
3. Custom error pages: 404.html (exists, enhance), 500.html (NEW), error.html (NEW - generic fallback)
4. Error pages with friendly messages in French, no stack traces, "Retour a l'accueil" button
5. Validation errors continue to return to form with field-level messages (no change to existing BindingResult pattern)
6. All exceptions logged with full stack trace via @Slf4j for debugging
7. Test scenarios: 404 via ClientNotFoundException, InvalidStatusTransition returns 409, simulated 500 via generic Exception

---

## Tasks / Subtasks

- [ ] Task 1: Enhance GlobalExceptionHandler.java (AC: 1, 2, 6)
  - [ ] Add @ExceptionHandler for `InvalidStatusTransitionException` - return HTTP 409, view `error/error` with message
  - [ ] Add @ExceptionHandler for `PriceNotFoundException` - return HTTP 404, view `error/404` with message
  - [ ] Add @ExceptionHandler for `PdfGenerationException` - return HTTP 500, view `error/500` with message
  - [ ] Add @ExceptionHandler for generic `Exception` - return HTTP 500, view `error/500` with generic French message
  - [ ] Log all exceptions: `log.error()` with full stack trace for 500s, `log.warn()` for 4xx
  - [ ] Add model attributes: `message` (user-facing), `status` (HTTP code), `error` (status text)

- [ ] Task 2: Create error/500.html template (AC: 3, 4)
  - [ ] Extend base layout via `layout:decorate="~{fragments/layout}"`
  - [ ] Display: icon `fa-exclamation-triangle` (red), title "500 - Erreur serveur", friendly message "Une erreur inattendue s'est produite. Veuillez reessayer."
  - [ ] Show dynamic `${message}` if present, generic fallback otherwise
  - [ ] "Retour a l'accueil" button linking to `/`
  - [ ] No stack trace displayed

- [ ] Task 3: Create error/error.html generic fallback template (AC: 3, 4)
  - [ ] Extend base layout
  - [ ] Display: icon `fa-exclamation-circle` (warning), title "Erreur", dynamic `${message}`
  - [ ] Show `${status}` code if available
  - [ ] "Retour a l'accueil" button linking to `/`

- [ ] Task 4: Enhance existing error/404.html (AC: 4)
  - [ ] Verify existing template follows same pattern (already OK - layout, icon, message, button)
  - [ ] Ensure consistency of styling with new 500.html and error.html

- [ ] Task 5: Write tests (AC: 7)
  - [ ] Unit test GlobalExceptionHandler: verify each @ExceptionHandler returns correct view name and HTTP status
  - [ ] Integration test or @WebMvcTest: trigger ClientNotFoundException -> 404 page
  - [ ] Integration test: trigger InvalidStatusTransitionException -> 409 with error view
  - [ ] Integration test: trigger generic Exception -> 500 page

---

## Dev Notes

### Existing GlobalExceptionHandler

`src/main/java/com/honeyai/exception/GlobalExceptionHandler.java` already exists with:
- `@ControllerAdvice` and `@Slf4j`
- Single handler: `handleClientNotFound(ClientNotFoundException ex, Model model)` -> returns `error/404` with HTTP 404
- Uses `model.addAttribute("message", ex.getMessage())` pattern

### Existing Custom Exceptions

All in `src/main/java/com/honeyai/exception/`:
- `ClientNotFoundException.java` - constructor with `String` or `Long id`
- `InvalidStatusTransitionException.java` - constructor with `String` or `(OrderStatus from, OrderStatus to)`
- `PriceNotFoundException.java` - constructor with `String` or `(Long productId, Integer year)`
- `PdfGenerationException.java` - constructor with `String` or `(String, Throwable)`

### Existing Error Templates

`src/main/resources/templates/error/404.html` already exists:
- Uses `layout:decorate="~{fragments/layout}"`
- Shows icon, "404", "Page non trouvee", dynamic `${message}`, buttons "Accueil" and "Clients"
- Follow same visual pattern for 500.html and error.html

### Current Controller Exception Handling

Some controllers have local try-catch blocks:
- `OrderController` catches `InvalidStatusTransitionException` and generic `Exception` in 3 methods
- `EtiquetteController` catches generic `Exception` in `generatePdf()`
- These **remain as-is** - they handle re-rendering forms with error context. The global handler catches unhandled exceptions only.

### Validation Pattern (DO NOT CHANGE)

All controllers use `@Valid` + `BindingResult` for form validation. This pattern stays unchanged:
```java
if (result.hasErrors()) {
    return "feature/form"; // re-render form with field errors
}
```

### Relevant Source Tree
```
src/main/java/com/honeyai/exception/
├── GlobalExceptionHandler.java       (MODIFY - add handlers)
├── ClientNotFoundException.java      (EXISTS - no change)
├── InvalidStatusTransitionException.java  (EXISTS - no change)
├── PriceNotFoundException.java       (EXISTS - no change)
├── PdfGenerationException.java       (EXISTS - no change)
src/main/resources/templates/error/
├── 404.html                          (EXISTS - verify consistency)
├── 500.html                          (NEW)
├── error.html                        (NEW)
src/test/java/com/honeyai/exception/
├── GlobalExceptionHandlerTest.java   (NEW)
```

### Testing

- **Test location:** `src/test/java/com/honeyai/exception/`
- **Test framework:** JUnit 5 + Mockito + Spring Boot Test
- **Naming convention:** `methodName_shouldExpectedBehavior_whenCondition`
- **Structure:** Given-When-Then
- **Handler unit tests:** Instantiate `GlobalExceptionHandler` directly, call handler methods, assert view name and model attributes
- **Integration tests (optional):** `@WebMvcTest` with a test controller that throws each exception type

---

## Change Log

| Date | Version | Description | Author |
|------|---------|-------------|--------|
| 2026-01-30 | 1.0 | Story initiale depuis epic 5 | PO (Sarah) |
| 2026-01-30 | 2.0 | Story completee: tasks, dev notes, testing, exceptions existantes documentees | PO (Sarah) |

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
