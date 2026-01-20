# Epic 5: Backup, Packaging & Production Readiness

**Epic Goal:** Finaliser HoneyAI pour la production en implémentant le système de backup automatique quotidien, créer le packaging Windows .exe avec launch4j, polir l'UX avec messages de confirmation et gestion d'erreurs conviviale, et préparer la distribution complète pour le déploiement.

## Story 5.1: Backup Service with Scheduled Daily Backups

**As a** developer,
**I want** to implement an automated daily backup service that copies the SQLite database file,
**so that** user data is protected against corruption or hardware failure.

### Acceptance Criteria:

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

## Story 5.2: Manual Backup Export UI

**As a** beekeeper (parent user),
**I want** to manually export a backup of my data,
**so that** I have an extra safety copy I control.

### Acceptance Criteria:

1. BackupController with GET /backup endpoint
2. templates/backup/manage.html with info, recent backups list, manual backup button
3. "Créer une sauvegarde maintenant" button triggers POST /backup/manual
4. Download recent backup files
5. "Exporter la base de données" downloads current honeyai.db
6. Success messages, help text about USB backup
7. Navigation: "Sauvegarde" link in sidebar

## Story 5.3: Global Exception Handler & User-Friendly Error Pages

**As a** developer,
**I want** to implement a global exception handler with friendly error pages,
**so that** technical errors don't confuse non-technical users.

### Acceptance Criteria:

1. GlobalExceptionHandler.java with @ControllerAdvice
2. @ExceptionHandler methods for common exceptions
3. Custom error pages: 404.html, 500.html, error.html
4. Error pages with friendly messages, no stack traces, "Retour à l'accueil" button
5. Validation errors return to form with field-level messages
6. All exceptions logged with stack trace for debugging
7. Test scenarios: 404, validation error, simulated 500

## Story 5.4: Confirmation Messages & Toast Notifications

**As a** beekeeper (parent user),
**I want** to see clear confirmation messages after important actions,
**so that** I feel confident my actions succeeded.

### Acceptance Criteria:

1. Flash message system using RedirectAttributes
2. Layout template displays flash messages at top
3. Success messages for all CRUD operations
4. Messages auto-dismiss after 5 seconds
5. Encouraging tone with icons
6. Consistent across all features
7. Accessibility: role="alert", dismissible with keyboard

## Story 5.5: Application Launcher Script & Icon

**As a** developer,
**I want** to create a Windows launcher script and icon,
**so that** the application starts with a double-click.

### Acceptance Criteria:

1. Icon file launcher/icon.ico with honey/bee theme
2. Launcher script lancer-honeyai.bat starts app and opens browser
3. README-INSTALLATION.txt with instructions
4. Test: double-click .bat, no console window, browser opens after 4s
5. Error handling if Java not found
6. Documentation with screenshots

## Story 5.6: launch4j Configuration & .exe Wrapper

**As a** developer,
**I want** to configure launch4j to create a Windows .exe wrapper,
**so that** the application launches like a native Windows app.

### Acceptance Criteria:

1. launch4j config file launcher/honeyai-launch4j.xml
2. Splash screen configured: splash.bmp with "HoneyAI" and "Démarrage..."
3. JVM options: Xms128m, Xmx512m, UTF-8 encoding
4. Build script to generate .exe
5. Test .exe: splash appears, browser opens, app functions
6. Optional bundled JRE in dist/jre/
7. Error if JRE not found
8. Distribution package structure
9. Tested on multiple Windows machines

## Story 5.7: Final UX Polish & Accessibility Review

**As a** beekeeper (parent user),
**I want** the application interface to be polished and consistent,
**so that** I feel confident using it.

### Acceptance Criteria:

1. Font size audit: body 16px min, headings 20px min, WCAG AA compliance
2. Button size audit: 44×44px min, proper spacing
3. Consistent terminology throughout
4. Navigation consistency: sidebar works everywhere, active page highlighted
5. Form labels: visible above fields, required marked with *
6. Loading indicators for operations >1s
7. Empty states on all lists
8. Color consistency with honey palette
9. Responsive check at 1280×720 and 1920×1080
10. Accessibility checklist: keyboard nav, screen reader, no color-only info

## Story 5.8: Production Configuration & Deployment Checklist

**As a** developer,
**I want** to finalize production configuration and create deployment checklist,
**so that** the application is properly configured for end-user deployment.

### Acceptance Criteria:

1. application.yml production profile: cache=true, logging INFO, real SIRET/address
2. Configuration validation: verify all config has real data
3. Database path relative ./data/honeyai.db
4. Deployment checklist: docs/DEPLOYMENT-CHECKLIST.md
5. User manual: docs/GUIDE-UTILISATEUR.md with screenshots
6. Troubleshooting guide: docs/DEPANNAGE.md
7. Version number in pom.xml (1.0.0), displayed in footer
8. Release notes: docs/RELEASE-NOTES-v1.0.md
9. Support contact in app footer
10. Final smoke test on clean machine

---
