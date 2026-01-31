# Story 5.5: Application Launcher Script & Icon

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Ready for Review
**Priority:** P1 - High
**Depends On:** Story 1.1

---

## User Story

**As a** developer,
**I want** to create a Windows launcher script and icon,
**so that** the application starts with a double-click.

---

## Acceptance Criteria

1. Icon file launcher/icon.ico with honey/bee theme (multi-resolution: 16x16, 32x32, 48x48, 256x256)
2. Launcher script `lancer-honeyai.bat` starts app with javaw and opens browser after delay
3. `README-INSTALLATION.txt` with installation instructions in French
4. Script hides console window using `javaw` (not `java`)
5. Error handling: display French error message if Java not found
6. Script opens `http://localhost:8080` in default browser after 4-second delay

---

## Tasks / Subtasks

- [x] Task 1: Create launcher directory and icon (AC: 1)
  - [x] Create `launcher/` directory at project root
  - [x] Create placeholder `launcher/icon.ico.README` describing icon requirements (binary icon generation not possible from CLI agent)

- [x] Task 2: Create launcher batch script (AC: 2, 4, 5, 6)
  - [x] Create `lancer-honeyai.bat` at project root
  - [x] Check Java availability: `where javaw >nul 2>&1` - if error, display French message and pause
  - [x] Start application: `start "" javaw -jar honeyai-1.0.0.jar` (no console window)
  - [x] Wait 4 seconds: `timeout /t 4 /nobreak >nul`
  - [x] Open browser: `start http://localhost:8080`
  - [x] Use `@echo off` and `chcp 65001` for UTF-8 support in console messages

- [x] Task 3: Create installation README (AC: 3)
  - [x] Create `README-INSTALLATION.txt` at project root (plain text, UTF-8 with BOM for Windows Notepad)
  - [x] Content in French: prerequisites (Java 21), installation steps, how to launch, how to stop, where data is stored (./data/), how to backup (./backups/)
  - [x] Keep simple and non-technical for family user

- [x] Task 4: Verify and test (AC: 2, 4, 5, 6)
  - [x] Full regression: 292 tests pass, 0 failures
  - [x] Manual test plan documented in Completion Notes below

---

## Dev Notes

### Project Context

- JAR artifact: `honeyai-1.0.0.jar` (from pom.xml: artifactId=honeyai, version=1.0.0)
- Built via: `mvn clean package -DskipTests` -> produces `target/honeyai-1.0.0.jar`
- Java version: 21 LTS
- Server port: 8080

### Batch Script Pattern
```bat
@echo off
chcp 65001 >nul

REM Check Java
where javaw >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ============================================
    echo   Java n'est pas installe sur cet ordinateur.
    echo   Veuillez installer Java 21 depuis:
    echo   https://adoptium.net
    echo ============================================
    pause
    exit /b 1
)

REM Start application (no console window)
start "" javaw -jar honeyai-1.0.0.jar

REM Wait for startup
echo Demarrage de HoneyAI...
timeout /t 4 /nobreak >nul

REM Open browser
start http://localhost:8080
```

### Icon Requirements

- Format: ICO with embedded sizes 16x16, 32x32, 48x48, 256x256
- Theme: honey pot, bee, or honeycomb
- Colors: golden/amber tones matching the app honey palette
- LIMITATION: The dev agent cannot generate binary image files. Create a descriptive placeholder or use a free icon from an open-source icon set

### Relevant Source Tree
```
honeyAI/
├── launcher/
│   └── icon.ico                    (NEW - or placeholder README)
├── lancer-honeyai.bat              (NEW)
├── README-INSTALLATION.txt         (NEW)
├── pom.xml                         (EXISTS - reference for JAR name)
```

### Testing

- **This story is primarily manual testing on Windows**
- No JUnit tests needed for .bat scripts
- Test plan: describe steps in completion notes
- Key verifications: javaw detection, no console window, browser opens, error message on missing Java

---

## Change Log

| Date | Version | Description | Author |
|------|---------|-------------|--------|
| 2026-01-30 | 1.0 | Story initiale depuis epic 5 | PO (Sarah) |
| 2026-01-30 | 2.0 | Story completee: tasks, batch script pattern, icon requirements, test plan | PO (Sarah) |

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5 (claude-opus-4-5-20251101)

### Debug Log References
No debug issues encountered.

### Completion Notes List
- Created `launcher/` directory and `icon.ico.README` placeholder with detailed icon creation instructions (ImageMagick, GIMP, online tools) and free icon sources
- Created `lancer-honeyai.bat` with: @echo off, chcp 65001 (UTF-8), javaw check with French error message, `start "" javaw -jar` for hidden console, 4s timeout, auto browser open
- Created `README-INSTALLATION.txt` with UTF-8 BOM for Windows Notepad compatibility. Content: prerequisites, installation, launch/stop instructions, data location, troubleshooting - all in simple French for family users
- AC1 partial: icon.ico placeholder only (binary generation not possible). README describes exact requirements for manual creation
- Manual test plan for Windows:
  1. Double-click `lancer-honeyai.bat` -> verify javaw starts app without visible console
  2. After ~4s, verify browser opens at http://localhost:8080
  3. Error test: temporarily rename/remove Java from PATH, run .bat -> verify French error message appears with adoptium.net link
  4. Verify README-INSTALLATION.txt opens correctly in Windows Notepad (UTF-8 BOM)

### File List
- `launcher/icon.ico.README` (NEW - placeholder with icon creation instructions)
- `lancer-honeyai.bat` (NEW)
- `README-INSTALLATION.txt` (NEW)

---

## QA Results
_To be filled by QA agent_
