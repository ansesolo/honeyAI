# Story 5.6: launch4j Configuration & .exe Wrapper

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Approved
**Priority:** P1 - High
**Depends On:** Story 5.5

---

## User Story

**As a** developer,
**I want** to configure launch4j to create a Windows .exe wrapper,
**so that** the application launches like a native Windows app.

---

## Acceptance Criteria

1. launch4j config file `launcher/honeyai-launch4j.xml`
2. Splash screen configured: `launcher/splash.bmp` with "HoneyAI" and "Demarrage..."
3. JVM options: -Xms128m, -Xmx512m, -Dfile.encoding=UTF-8
4. Build script `build-exe.bat` or Maven plugin to generate .exe from JAR
5. Distribution package structure documented in `launcher/DIST-README.md`
6. Error message in French if JRE not found
7. Uses icon.ico from Story 5.5

---

## Tasks / Subtasks

- [ ] Task 1: Create launch4j XML configuration (AC: 1, 3, 6, 7)
  - [ ] Create `launcher/honeyai-launch4j.xml`
  - [ ] Configure: input JAR `honeyai-1.0.0.jar`, output `HoneyAI.exe`
  - [ ] Set icon: `icon.ico` (from Story 5.5)
  - [ ] JVM options: `-Xms128m -Xmx512m -Dfile.encoding=UTF-8`
  - [ ] Minimum JRE version: `21`
  - [ ] JRE search: registry + JAVA_HOME + PATH
  - [ ] Error title: "HoneyAI - Erreur"
  - [ ] Error message: "Java 21 n'a pas ete trouve. Veuillez installer Java depuis https://adoptium.net"
  - [ ] Header type: GUI (no console window)

- [ ] Task 2: Create splash screen BMP (AC: 2)
  - [ ] Create `launcher/splash.bmp` - 400x300 pixels, 24-bit BMP
  - [ ] Content: "HoneyAI" title, "Demarrage..." subtitle, honey/amber color scheme
  - [ ] LIMITATION: Dev agent cannot generate binary BMP. Create a placeholder `launcher/splash.bmp.README` describing requirements, or generate a simple BMP programmatically if possible

- [ ] Task 3: Create build script (AC: 4)
  - [ ] Create `build-exe.bat` at project root
  - [ ] Steps: `mvn clean package -DskipTests`, then invoke `launch4j launcher/honeyai-launch4j.xml`
  - [ ] Prerequisite check: verify launch4j is installed and on PATH
  - [ ] Output: `HoneyAI.exe` in project root or `dist/` directory

- [ ] Task 4: Document distribution package structure (AC: 5)
  - [ ] Create `launcher/DIST-README.md` with the distribution folder structure:
    ```
    HoneyAI/
    ├── HoneyAI.exe
    ├── honeyai-1.0.0.jar
    ├── lancer-honeyai.bat (fallback)
    ├── README-INSTALLATION.txt
    ├── data/           (created at runtime)
    └── backups/        (created at runtime)
    ```
  - [ ] Include instructions for packaging a distribution ZIP

---

## Dev Notes

### launch4j Overview

launch4j is a cross-platform tool that wraps Java JAR files into Windows .exe executables. It:
- Searches for JRE on the system (registry, JAVA_HOME, PATH)
- Displays a splash screen during startup
- Shows a native error dialog if JRE not found
- Provides GUI or console header types

### launch4j XML Structure
```xml
<launch4jConfig>
  <dontWrapJar>true</dontWrapJar>
  <headerType>gui</headerType>
  <jar>honeyai-1.0.0.jar</jar>
  <outfile>HoneyAI.exe</outfile>
  <icon>icon.ico</icon>
  <jre>
    <minVersion>21</minVersion>
    <jdkPreference>preferJre</jdkPreference>
    <initialHeapSize>128</initialHeapSize>
    <maxHeapSize>512</maxHeapSize>
    <opts>-Dfile.encoding=UTF-8</opts>
  </jre>
  <splash>
    <file>splash.bmp</file>
    <waitForWindow>true</waitForWindow>
    <timeout>60</timeout>
  </splash>
  <messages>
    <jreNotFoundErr>Java 21 n'a pas ete trouve. Veuillez installer Java depuis https://adoptium.net</jreNotFoundErr>
  </messages>
  <versionInfo>
    <fileVersion>1.0.0.0</fileVersion>
    <txtFileVersion>1.0.0</txtFileVersion>
    <productVersion>1.0.0.0</productVersion>
    <txtProductVersion>1.0.0</txtProductVersion>
    <fileDescription>HoneyAI - Gestion Apicole</fileDescription>
    <productName>HoneyAI</productName>
    <companyName>Exploitation Apicole Familiale</companyName>
    <internalName>honeyai</internalName>
    <originalFilename>HoneyAI.exe</originalFilename>
    <copyright>2026</copyright>
  </versionInfo>
</launch4jConfig>
```

### Prerequisites

- launch4j must be installed on the build machine (not a runtime dependency)
- Download: https://launch4j.sourceforge.net/
- Not included in Maven dependencies - separate build tool
- Alternatively, can use launch4j Maven plugin: `com.akathist.maven.plugins.launch4j`

### Project Context

- JAR: `honeyai-1.0.0.jar` (pom.xml version 1.0.0)
- Icon: `launcher/icon.ico` (from Story 5.5)
- Java: 21 LTS minimum

### Relevant Source Tree
```
launcher/
├── icon.ico                        (FROM Story 5.5)
├── splash.bmp                      (NEW - or placeholder README)
├── honeyai-launch4j.xml            (NEW)
├── DIST-README.md                  (NEW)
build-exe.bat                       (NEW)
```

### Testing

- **Manual testing on Windows only**
- No JUnit tests
- Test plan: run build-exe.bat, verify HoneyAI.exe is generated, double-click .exe, verify splash appears, app starts, browser opens
- Test JRE missing: temporarily rename/remove Java, verify French error dialog

---

## Change Log

| Date | Version | Description | Author |
|------|---------|-------------|--------|
| 2026-01-30 | 1.0 | Story initiale depuis epic 5 | PO (Sarah) |
| 2026-01-30 | 2.0 | Story completee: launch4j XML template, build script, distribution structure | PO (Sarah) |

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
