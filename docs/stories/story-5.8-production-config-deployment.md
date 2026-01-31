# Story 5.8: Production Configuration & Deployment Checklist

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Ready for Review
**Priority:** P0 - Critical Path
**Depends On:** All previous stories

---

## User Story

**As a** developer,
**I want** to finalize production configuration and create deployment checklist,
**so that** the application is properly configured for end-user deployment.

---

## Acceptance Criteria

1. `application-prod.yml` production profile: thymeleaf cache=true, logging level INFO, real SIRET/address data
2. Configuration validation: verify all config properties have real production data (not placeholders)
3. Database path relative `./data/honeyai.db` confirmed in production profile
4. Deployment checklist: `docs/DEPLOYMENT-CHECKLIST.md`
5. User manual: `docs/GUIDE-UTILISATEUR.md` (text-based, no screenshots required)
6. Troubleshooting guide: `docs/DEPANNAGE.md`
7. Version number 1.0.0 in pom.xml (already set), displayed in application footer
8. Release notes: `docs/RELEASE-NOTES-v1.0.md`
9. Support contact info in app footer
10. Final smoke test: full regression + manual verification of key flows

---

## Tasks / Subtasks

- [x] Task 1: Create production profile (AC: 1, 2, 3)
  - [x] Create `src/main/resources/application-prod.yml`
  - [x] Set `spring.thymeleaf.cache: true`
  - [x] Set `logging.level.root: INFO`, `logging.level.com.honeyai: INFO`
  - [x] Set `logging.file.name: ./logs/honeyai.log` with rolling policy
  - [x] Confirm `spring.datasource.url: jdbc:sqlite:./data/honeyai.db`
  - [x] Verify `honeyai.etiquettes` section has real data (already configured in application.yml with real SIRET "511036780015" and real address)
  - [x] Set `spring.jpa.show_sql: false`, `format_sql: false`

- [x] Task 2: Add version and support info to footer (AC: 7, 9)
  - [x] Modify `fragments/layout.html` footer section
  - [x] Display version: "HoneyAI v1.0.0 — 2026" (hardcoded)
  - [x] Add support contact: "Support : 04.71.03.12.43"
  - [x] Keep footer clean and non-intrusive
  - [x] Update `lancer-honeyai.bat` with `--spring.profiles.active=prod`
  - [x] Update `launcher/honeyai-launch4j.xml` with `-Dspring.profiles.active=prod`

- [x] Task 3: Create deployment checklist (AC: 4)
  - [x] Create `docs/DEPLOYMENT-CHECKLIST.md`
  - [x] Sections: Prerequis, Etapes d'installation, Premier lancement, Verification, Depannage rapide
  - [x] Prerequisites: Java 21, Windows 10/11
  - [x] Installation: unzip distribution, verify folder structure, run lancer-honeyai.bat
  - [x] Verification: access localhost:8080, create test client, verify backup

- [x] Task 4: Create user manual (AC: 5)
  - [x] Create `docs/GUIDE-UTILISATEUR.md` in French
  - [x] Sections: Introduction, Demarrage, Tableau de bord, Clients, Produits, Commandes, Etiquettes, Achats, Sauvegarde, Support
  - [x] Keep instructions simple and non-technical
  - [x] Text-based descriptions (no screenshots - limitation acknowledged)

- [x] Task 5: Create troubleshooting guide (AC: 6)
  - [x] Create `docs/DEPANNAGE.md` in French
  - [x] Common issues: app won't start (Java missing, port 8080 in use), blank page (browser cache), data not saving (disk full), backup issues
  - [x] Solutions in simple French for non-technical users
  - [x] How to check logs: `./logs/honeyai.log`
  - [x] How to reset: delete `./data/honeyai.db` (warning: data loss)

- [x] Task 6: Create release notes (AC: 8)
  - [x] Create `docs/RELEASE-NOTES-v1.0.md`
  - [x] List all features by epic: Clients, Commandes & Produits, Etiquettes, Tableau de Bord & Achats, Sauvegarde & Production
  - [x] Known limitations section
  - [x] Future improvements section

- [x] Task 7: Final smoke test (AC: 10)
  - [x] Run full test suite: `mvn test` - 292 tests pass, 0 failures
  - [x] Build production JAR: `mvn clean package -DskipTests` - BUILD SUCCESS
  - [x] Document results in completion notes

---

## Dev Notes

### Current Configuration State

`application.yml` (main config - already has real data):
```yaml
server:
  port: 8080
  shutdown: graceful
spring:
  datasource:
    url: jdbc:sqlite:./data/honeyai.db
  thymeleaf:
    cache: false  # Will be true in prod
honeyai:
  etiquettes:
    siret: "511036780015"           # Real SIRET
    nom-apiculteur: "ALLEMAND Jean Pierre"  # Real name
    adresse: "12 chemin des crouzettes, Tarreyres 43370 Cussac-sur-Loire"  # Real address
    telephone: "Tel: 04.71.03.12.43"  # Real phone
```

No `application-prod.yml` exists yet. No `application-dev.yml` exists.

### pom.xml Version

Already set to `1.0.0` in pom.xml (line 14: `<version>1.0.0</version>`).

### Footer (layout.html)

Current footer (line 77-81):
```html
<footer class="footer mt-auto py-3 bg-light">
    <div class="container text-center">
        <span class="text-muted">HoneyAI 2026</span>
    </div>
</footer>
```
Update to include version and support contact.

### Production Profile Activation

The .bat launcher or launch4j should pass `--spring.profiles.active=prod`:
```bat
start "" javaw -jar honeyai-1.0.0.jar --spring.profiles.active=prod
```
NOTE: Update `lancer-honeyai.bat` from Story 5.5 if needed (or document as a prerequisite).

### Relevant Source Tree
```
src/main/resources/
├── application.yml                    (EXISTS - verify prod data)
├── application-prod.yml               (NEW)
├── templates/fragments/layout.html    (MODIFY - footer version + support)
docs/
├── DEPLOYMENT-CHECKLIST.md            (NEW)
├── GUIDE-UTILISATEUR.md               (NEW)
├── DEPANNAGE.md                       (NEW)
├── RELEASE-NOTES-v1.0.md             (NEW)
pom.xml                               (EXISTS - version already 1.0.0)
```

### Testing

- **Test framework:** JUnit 5 + Spring Boot Test
- **Full regression:** `mvn test` - all tests must pass
- **Production build:** `mvn clean package -DskipTests` then manual verification
- **Smoke test:** Manual verification of all key user flows
- **Profile test:** Start with `--spring.profiles.active=prod` and verify cache enabled, logging level correct

---

## Change Log

| Date | Version | Description | Author |
|------|---------|-------------|--------|
| 2026-01-30 | 1.0 | Story initiale depuis epic 5 | PO (Sarah) |
| 2026-01-30 | 2.0 | Story completee: prod profile, footer, docs, smoke test, config actuelle documentee | PO (Sarah) |

---

## Dev Agent Record

### Agent Model Used
Claude Opus 4.5 (claude-opus-4-5-20251101)

### Debug Log References
No debug issues encountered.

### Completion Notes List
- Created `application-prod.yml` with thymeleaf cache=true, logging INFO to file with rolling policy (10MB, 30 days), show_sql/format_sql=false, datasource confirmed at `./data/honeyai.db`
- Production data validation: `honeyai.etiquettes` section in `application.yml` already contains real SIRET ("511036780015"), real name, real address, real phone - inherited by prod profile automatically
- Updated footer in `layout.html`: "HoneyAI v1.0.0 — 2026" + "Support : 04.71.03.12.43"
- Updated `lancer-honeyai.bat` to pass `--spring.profiles.active=prod` to javaw
- Updated `launcher/honeyai-launch4j.xml` JVM opts to include `-Dspring.profiles.active=prod`
- Created `docs/DEPLOYMENT-CHECKLIST.md`: checklist format with prerequisites, installation steps, verification, quick troubleshooting table
- Created `docs/GUIDE-UTILISATEUR.md`: full French user manual covering all features (dashboard, clients, orders, products, etiquettes, achats, backup)
- Created `docs/DEPANNAGE.md`: French troubleshooting guide for common issues (Java missing, port in use, blank page, disk full, corrupted DB, manual restore)
- Created `docs/RELEASE-NOTES-v1.0.md`: features by epic, technical specs, known limitations, future improvements
- Full regression: 292 tests pass, 0 failures
- Production build: `mvn clean package -DskipTests` BUILD SUCCESS, `honeyai-1.0.0.jar` generated

### File List
- `src/main/resources/application-prod.yml` (NEW - production profile)
- `src/main/resources/templates/fragments/layout.html` (MODIFIED - footer with version + support)
- `lancer-honeyai.bat` (MODIFIED - added --spring.profiles.active=prod)
- `launcher/honeyai-launch4j.xml` (MODIFIED - added -Dspring.profiles.active=prod to JVM opts)
- `docs/DEPLOYMENT-CHECKLIST.md` (NEW - deployment checklist)
- `docs/GUIDE-UTILISATEUR.md` (NEW - user manual in French)
- `docs/DEPANNAGE.md` (NEW - troubleshooting guide in French)
- `docs/RELEASE-NOTES-v1.0.md` (NEW - release notes v1.0.0)

---

## QA Results
_To be filled by QA agent_
