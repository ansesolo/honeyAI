# Story 5.3: Global Exception Handler & User-Friendly Error Pages

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 1.4

---

## User Story

**As a** developer,
**I want** to implement a global exception handler with friendly error pages,
**so that** technical errors don't confuse non-technical users.

---

## Acceptance Criteria

1. GlobalExceptionHandler.java with @ControllerAdvice
2. @ExceptionHandler methods for common exceptions
3. Custom error pages: 404.html, 500.html, error.html
4. Error pages with friendly messages, no stack traces, "Retour a l'accueil" button
5. Validation errors return to form with field-level messages
6. All exceptions logged with stack trace for debugging
7. Test scenarios: 404, validation error, simulated 500

---

## Technical Notes

- @ControllerAdvice for global exception handling
- Error pages in templates/error/ folder
- Log full stack trace but show friendly message to user

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Error pages display correctly
- [ ] Logging works
- [ ] Code committed to repository
