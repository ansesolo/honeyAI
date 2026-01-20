# Story 5.5: Application Launcher Script & Icon

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Pending
**Priority:** P1 - High
**Depends On:** Story 1.1

---

## User Story

**As a** developer,
**I want** to create a Windows launcher script and icon,
**so that** the application starts with a double-click.

---

## Acceptance Criteria

1. Icon file launcher/icon.ico with honey/bee theme
2. Launcher script lancer-honeyai.bat starts app and opens browser
3. README-INSTALLATION.txt with instructions
4. Test: double-click .bat, no console window, browser opens after 4s
5. Error handling if Java not found
6. Documentation with screenshots

---

## Technical Notes

- .bat script: start javaw -jar honeyai.jar && timeout /t 4 && start http://localhost:8080
- Use javaw (not java) to hide console window
- Icon: 256x256 with embedded smaller sizes

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Launcher works on Windows
- [ ] Documentation complete
- [ ] Code committed to repository
