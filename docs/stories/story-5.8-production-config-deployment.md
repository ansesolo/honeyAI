# Story 5.8: Production Configuration & Deployment Checklist

**Epic:** Epic 5 - Backup, Packaging & Production Readiness
**Status:** Pending
**Priority:** P0 - Critical Path
**Depends On:** All previous stories

---

## User Story

**As a** developer,
**I want** to finalize production configuration and create deployment checklist,
**so that** the application is properly configured for end-user deployment.

---

## Acceptance Criteria

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

## Technical Notes

- Production profile: spring.profiles.active=prod
- Real exploitation data: SIRET, address, phone
- Version displayed in footer for support reference

---

## Definition of Done

- [ ] All acceptance criteria met
- [ ] Production config complete
- [ ] Documentation complete
- [ ] Smoke test passed on clean machine
- [ ] Code committed to repository
