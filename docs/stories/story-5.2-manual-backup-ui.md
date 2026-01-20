# Story 5.2: Manual Backup Export UI

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Pending
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
4. Download recent backup files
5. "Exporter la base de donnees" downloads current honeyai.db
6. Success messages, help text about USB backup
7. Navigation: "Sauvegarde" link in sidebar

---

## Technical Notes

- Display list of recent backup files with dates
- Download button for each backup file
- Help text encouraging USB backup

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Manual backup works
- [ ] Download works
- [ ] Code committed to repository
