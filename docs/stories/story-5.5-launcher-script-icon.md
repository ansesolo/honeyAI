# Story 5.5: Application Launcher Script & Icon

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Approved
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

- [ ] Task 1: Create launcher directory and icon (AC: 1)
  - [ ] Create `launcher/` directory at project root
  - [ ] Create or source `launcher/icon.ico` with honey/bee theme, multi-resolution (16, 32, 48, 256 px)
  - [ ] NOTE: Icon creation may require an external tool (ImageMagick, GIMP, or online converter). If not possible to generate, create a placeholder text file `launcher/icon.ico.README` describing the icon requirements

- [ ] Task 2: Create launcher batch script (AC: 2, 4, 5, 6)
  - [ ] Create `lancer-honeyai.bat` at project root
  - [ ] Check Java availability: `where javaw >nul 2>&1` - if error, display French message "Java n'est pas installe. Veuillez installer Java 21 depuis https://adoptium.net" and pause
  - [ ] Start application: `start "" javaw -jar honeyai-1.0.0.jar` (no console window)
  - [ ] Wait 4 seconds: `timeout /t 4 /nobreak >nul`
  - [ ] Open browser: `start http://localhost:8080`
  - [ ] Use `@echo off` and `chcp 65001` for UTF-8 support in console messages

- [ ] Task 3: Create installation README (AC: 3)
  - [ ] Create `README-INSTALLATION.txt` at project root (plain text, UTF-8 with BOM for Windows Notepad)
  - [ ] Content in French: prerequisites (Java 21), installation steps, how to launch, how to stop (close browser + Ctrl+C or close terminal), where data is stored (./data/), how to backup (./backups/)
  - [ ] Keep simple and non-technical for family user

- [ ] Task 4: Verify and test (AC: 2, 4, 5, 6)
  - [ ] Describe manual test plan: double-click .bat on Windows, verify no console window visible, browser opens after ~4s, app accessible at localhost:8080
  - [ ] Describe error test: rename java temporarily, verify error message displays

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
