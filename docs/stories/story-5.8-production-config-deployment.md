# Story 5.8: Production Configuration & Deployment Checklist

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Approved
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

- [ ] Task 1: Create production profile (AC: 1, 2, 3)
  - [ ] Create `src/main/resources/application-prod.yml`
  - [ ] Set `spring.thymeleaf.cache: true`
  - [ ] Set `logging.level.root: INFO`, `logging.level.com.honeyai: INFO`
  - [ ] Set `logging.file.name: ./logs/honeyai.log` with rolling policy
  - [ ] Confirm `spring.datasource.url: jdbc:sqlite:./data/honeyai.db`
  - [ ] Verify `honeyai.etiquettes` section has real data (already configured in application.yml with real SIRET "511036780015" and real address)
  - [ ] Set `spring.jpa.show_sql: false`, `format_sql: false`

- [ ] Task 2: Add version and support info to footer (AC: 7, 9)
  - [ ] Modify `fragments/layout.html` footer section
  - [ ] Display version: "HoneyAI v1.0.0" (hardcoded or from `@Value("${app.version:1.0.0}")`)
  - [ ] Add support contact: "Support: [telephone from config]" or a generic email/phone
  - [ ] Keep footer clean and non-intrusive

- [ ] Task 3: Create deployment checklist (AC: 4)
  - [ ] Create `docs/DEPLOYMENT-CHECKLIST.md`
  - [ ] Sections: Prerequisites, Installation Steps, First Launch, Verification, Troubleshooting Quick Reference
  - [ ] Prerequisites: Java 21, Windows 10/11
  - [ ] Installation: unzip distribution, verify folder structure, run lancer-honeyai.bat
  - [ ] Verification: access localhost:8080, create test client, verify backup

- [ ] Task 4: Create user manual (AC: 5)
  - [ ] Create `docs/GUIDE-UTILISATEUR.md` in French
  - [ ] Sections: Introduction, Demarrage, Gestion des Clients, Gestion des Commandes, Produits et Tarifs, Etiquettes, Achats, Tableau de Bord, Sauvegarde
  - [ ] Keep instructions simple and non-technical
  - [ ] Text-based descriptions (no screenshots - limitation acknowledged)

- [ ] Task 5: Create troubleshooting guide (AC: 6)
  - [ ] Create `docs/DEPANNAGE.md` in French
  - [ ] Common issues: app won't start (Java missing, port 8080 in use), blank page (browser cache), data not saving (disk full), backup issues
  - [ ] Solutions in simple French for non-technical users
  - [ ] How to check logs: `./logs/honeyai.log`
  - [ ] How to reset: delete `./data/honeyai.db` (warning: data loss)

- [ ] Task 6: Create release notes (AC: 8)
  - [ ] Create `docs/RELEASE-NOTES-v1.0.md`
  - [ ] List all features by epic: Client Management, Orders & Products, Label Generation, Financial Dashboard & Purchases, Backup & Production
  - [ ] Known limitations section
  - [ ] Future improvements section

- [ ] Task 7: Final smoke test (AC: 10)
  - [ ] Run full test suite: `mvn test` - all tests must pass
  - [ ] Build production JAR: `mvn clean package -Pprod`
  - [ ] Manual verification: start app, create client, create order, generate label PDF, create backup, verify dashboard
  - [ ] Document results in completion notes

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
