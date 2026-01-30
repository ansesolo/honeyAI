# Story 5.2: Manual Backup Export UI

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Ready for Review
**Priority:** P1 - High
**Depends On:** Story 5.1, Story 1.4

---

## User Story

**As a** beekeeper (parent user),
**I want** to manually export a backup of my data,
**so that** I have an extra safety copy I control.

---

## Acceptance Criteria

1. BackupController with GET /backup endpoint
2. templates/backup/manage.html with info, recent backups list, manual backup button
3. "Creer une sauvegarde maintenant" button triggers POST /backup/manual
4. Download recent backup files via GET /backup/download/{filename}
5. "Exporter la base de donnees" downloads current honeyai.db via GET /backup/export-db
6. Success messages via flash attributes, help text about USB backup
7. Navigation: "Sauvegarde" link in navbar

---

## Tasks / Subtasks

- [x] Task 1: Extend BackupService with listing and file access methods (AC: 2, 4, 5)
  - [x] Add `listRecentBackups()` method returning list of backup file info (name, date, size) sorted by date descending
  - [x] Add `getBackupFile(String filename)` method returning Path to a specific backup file with filename validation (prevent path traversal)
  - [x] Add `getDatabasePath()` accessor method for exporting current DB
  - [x] Unit tests for all new methods

- [x] Task 2: Create BackupController (AC: 1, 3, 4, 5, 6)
  - [x] Create `BackupController.java` with `@Controller`, `@RequestMapping("/backup")`, `@RequiredArgsConstructor`
  - [x] GET `/backup` - displays manage.html with recent backups list and `activeMenu = "backup"`
  - [x] POST `/backup/manual` - triggers `backupService.performBackup()`, adds flash success/error message, redirects to GET /backup
  - [x] GET `/backup/download/{filename}` - streams backup file as download with `Content-Disposition: attachment`, validates filename against path traversal (reject `..`, `/`, `\`)
  - [x] GET `/backup/export-db` - streams current honeyai.db as download with filename `honeyai-export-YYYY-MM-DD.db`
  - [x] Unit tests with `@WebMvcTest` for all endpoints

- [x] Task 3: Create backup/manage.html template (AC: 2, 6)
  - [x] Extend base layout via `layout:decorate="~{fragments/layout}"`
  - [x] Page title: "Sauvegarde"
  - [x] Info card: explanation of the backup system (automatic daily 2h, retention 30 days)
  - [x] Help text card: "Conseil : Copiez regulierement vos sauvegardes sur une cle USB pour une protection maximale"
  - [x] "Creer une sauvegarde maintenant" button (POST /backup/manual via form)
  - [x] "Exporter la base de donnees" button (link to GET /backup/export-db)
  - [x] Recent backups table: filename, date, size, download button per row
  - [x] Empty state: "Aucune sauvegarde disponible" when no backups exist
  - [x] Flash messages display (success/error)

- [x] Task 4: Add navigation link in layout.html (AC: 7)
  - [x] Add "Sauvegarde" nav item with icon `fa-database` after "Achats" in navbar
  - [x] Use `activeMenu == 'backup'` for active state highlighting

---

## Dev Notes

### Existing BackupService (Story 5.1)

The `BackupService.java` already exists at `src/main/java/com/honeyai/service/BackupService.java` with:
- `performBackup()` - copies `./data/honeyai.db` to `./backups/honeyai-backup-YYYY-MM-DD-HHmmss.db`, returns `Path` (or `null` on error)
- `scheduledBackup()` - `@Scheduled(cron = "0 0 2 * * ?")` triggers performBackup
- `cleanupOldBackups()` - deletes backup files older than 30 days
- Configurable paths via `@Value`: `honeyai.backup.database-path` (default `./data/honeyai.db`), `honeyai.backup.directory` (default `./backups`)
- Backup file pattern: `honeyai-backup-YYYY-MM-DD-HHmmss.db`

**New methods needed** (not yet in BackupService):
- `listRecentBackups()` - list files in backup directory matching `honeyai-backup-*.db`, return info (Path, last modified time, file size)
- `getBackupFile(String filename)` - resolve and validate filename within backup directory
- `getDatabasePath()` - return the configured `databasePath` for DB export

### Controller Pattern

Follow existing controller pattern (see `AchatController.java`):
```java
@Controller
@RequestMapping("/backup")
@RequiredArgsConstructor
@Slf4j
public class BackupController {
    private final BackupService backupService;
    // model.addAttribute("activeMenu", "backup");
}
```

For file download endpoints, use:
```java
@GetMapping("/download/{filename}")
public ResponseEntity<Resource> downloadBackup(@PathVariable String filename) {
    // Validate filename: reject if contains "..", "/" or "\"
    // Return InputStreamResource with Content-Disposition header
}
```

### Navigation (layout.html)

The navigation is a **horizontal navbar** (not a sidebar). Add after "Achats" nav-item:
```html
<li class="nav-item">
    <a class="nav-link" th:href="@{/backup}" th:classappend="${activeMenu == 'backup'} ? 'active' : ''">
        <i class="fas fa-database me-1"></i>Sauvegarde
    </a>
</li>
```

### Template Pattern

Templates use Thymeleaf layout dialect:
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{fragments/layout}">
<head><title>Sauvegarde</title></head>
<body>
<div layout:fragment="content">
    <!-- Page content -->
</div>
</body>
</html>
```

### Security Consideration

The download endpoint MUST validate the `filename` parameter to prevent path traversal attacks:
- Reject filenames containing `..`, `/`, or `\`
- Verify the resolved path starts with the backup directory
- Only serve files matching the `honeyai-backup-*.db` pattern

### Relevant Source Tree
```
src/main/java/com/honeyai/
├── controller/BackupController.java          (NEW)
├── service/BackupService.java                (MODIFY - add list/get methods)
src/main/resources/templates/
├── backup/manage.html                        (NEW)
├── fragments/layout.html                     (MODIFY - add nav link)
src/test/java/com/honeyai/
├── controller/BackupControllerTest.java      (NEW)
├── service/BackupServiceTest.java            (MODIFY - add tests for new methods)
```

### Testing

- **Test location:** `src/test/java/com/honeyai/`
- **Test framework:** JUnit 5 + Mockito + Spring Boot Test
- **Naming convention:** `methodName_shouldExpectedBehavior_whenCondition`
- **Structure:** Given-When-Then
- **Controller tests:** Use `@WebMvcTest(BackupController.class)` with `@MockBean BackupService`
- **Service tests:** Use `@ExtendWith(MockitoExtension.class)` or `@TempDir` for file operations
- **Key test scenarios:**
  - Controller: GET /backup returns view with backups list, POST /backup/manual triggers backup and redirects with flash, download returns file with correct headers, download rejects path traversal filenames, export-db returns database file
  - Service: listRecentBackups returns sorted list, listRecentBackups returns empty when no backups, getBackupFile returns valid path, getBackupFile rejects invalid filenames

---

## Change Log

| Date | Version | Description | Author |
|------|---------|-------------|--------|
| 2026-01-30 | 1.0 | Story initiale depuis epic 5 | PO (Sarah) |
| 2026-01-30 | 2.0 | Story completee: tasks/subtasks, dev notes, testing, securite | PO (Sarah) |

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5

### Debug Log References
None needed

### Completion Notes List
- Created `BackupFileDto` to avoid Thymeleaf SpEL security restrictions on `java.nio.file.Path` properties
- `listRecentBackups()` returns DTOs with filename, lastModified, sizeBytes + formatted size helper
- Path traversal protection: triple validation (character check, pattern match, normalize+startsWith)
- Controller uses `InputStreamResource` with `Content-Disposition: attachment` for file downloads
- Export DB uses date-stamped filename: `honeyai-export-YYYY-MM-DD.db`
- Flash attributes use `successMessage`/`errorMessage` (consistent with existing pattern)
- Full regression: 286 tests passing, 0 failures

### File List
- `src/main/java/com/honeyai/service/BackupService.java` (MODIFIED - added listRecentBackups, getBackupFile, getDatabasePath)
- `src/test/java/com/honeyai/service/BackupServiceTest.java` (MODIFIED - added 12 new tests)
- `src/main/java/com/honeyai/dto/BackupFileDto.java` (NEW)
- `src/main/java/com/honeyai/controller/BackupController.java` (NEW)
- `src/test/java/com/honeyai/controller/BackupControllerTest.java` (NEW)
- `src/main/resources/templates/backup/manage.html` (NEW)
- `src/main/resources/templates/fragments/layout.html` (MODIFIED - added Sauvegarde nav link)

---

## QA Results
_To be filled by QA agent_
