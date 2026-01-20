# Story 5.1: Backup Service with Scheduled Daily Backups

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** Story 1.1

---

## User Story

**As a** developer,
**I want** to implement an automated daily backup service that copies the SQLite database file,
**so that** user data is protected against corruption or hardware failure.

---

## Acceptance Criteria

1. BackupService.java created with method performBackup()
2. Backup copies ./data/honeyai.db to ./backups/honeyai-backup-YYYY-MM-DD-HHmmss.db
3. Backup directory ./backups/ created automatically if not exists
4. @Scheduled annotation with cron "0 0 2 * * ?" (daily at 2:00 AM)
5. @EnableScheduling on main application class
6. Backup retention: delete files older than 30 days
7. Logging: log backup start/success/failure
8. Error handling: log error but don't crash app
9. Unit test: verify backup file created with correct name
10. Integration test: trigger backup, verify file is valid SQLite database

---

## Technical Notes

- Cron expression: "0 0 2 * * ?" = daily at 2:00 AM
- Use Files.copy() for atomic file copy
- Retention cleanup: delete files older than 30 days

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Scheduled backup works
- [ ] Retention cleanup works
- [ ] Code committed to repository
