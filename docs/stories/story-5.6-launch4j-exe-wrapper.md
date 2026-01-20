# Story 5.6: launch4j Configuration & .exe Wrapper

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Pending
**Priority:** P1 - High
**Depends On:** Story 5.5

---

## User Story

**As a** developer,
**I want** to configure launch4j to create a Windows .exe wrapper,
**so that** the application launches like a native Windows app.

---

## Acceptance Criteria

1. launch4j config file launcher/honeyai-launch4j.xml
2. Splash screen configured: splash.bmp with "HoneyAI" and "Demarrage..."
3. JVM options: Xms128m, Xmx512m, UTF-8 encoding
4. Build script to generate .exe
5. Test .exe: splash appears, browser opens, app functions
6. Optional bundled JRE in dist/jre/
7. Error if JRE not found
8. Distribution package structure
9. Tested on multiple Windows machines

---

## Technical Notes

- launch4j creates native .exe wrapper around JAR
- Splash screen: 400x300 BMP image
- JRE bundling optional but recommended for users without Java

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] .exe works on Windows 10/11
- [ ] Splash screen displays
- [ ] Code committed to repository
