# HoneyAI - Index des Stories

**Projet:** HoneyAI - Application de Gestion Apicole Familiale
**Total:** 40 stories across 5 epics
**Estimation:** 68-91 heures de developpement

---

## Epic 1: Foundation & Client Management (8 stories)

Infrastructure technique et gestion des clients.

| Story | Titre | Priorite | Statut |
|-------|-------|----------|--------|
| [1.1](./story-1.1-project-bootstrap.md) | Project Bootstrap & Core Configuration | P0 | Done |
| [1.2](./story-1.2-client-entity-repository.md) | Client Entity & Repository with Soft Delete | P0 | Done |
| [1.3](./story-1.3-client-service.md) | Client Service with Business Logic | P0 | Done |
| [1.4](./story-1.4-base-layout-navigation.md) | Base Layout & Navigation with Bootstrap | P0 | Done |
| [1.5](./story-1.5-client-list-search.md) | Client List & Search Interface | P0 | Done |
| [1.6](./story-1.6-client-detail-view.md) | Client Detail View with History Placeholder | P1 | Done |
| [1.7](./story-1.7-client-create-edit-forms.md) | Client Create & Edit Forms | P0 | Done |
| [1.8](./story-1.8-client-soft-delete.md) | Client Soft Delete with Confirmation | P1 | Done |

---

## Epic 2: Order Management & Product Catalog (8 stories)

Gestion des orders et catalogue produits.

| Story | Titre | Priorite | Statut |
|-------|-------|----------|--------|
| [2.1](./story-2.1-product-tarif-entities.md) | Product & Tarif Entities with Year-based Pricing | P0 | Done |
| [2.2](./story-2.2-order-lignecommande-entities.md) | Commande & LigneCommande Entities with Status Workflow | P0 | Done |
| [2.3](./story-2.3-order-produit-services.md) | CommandeService & ProduitService with Business Logic | P0 | Done |
| [2.4](./story-2.4-product-catalog-ui.md) | Product Catalog & Tarif Management UI | P1 | Done |
| [2.5](./story-2.5-order-list-filters.md) | Order List with Filters and Status Badges | P0 | Done |
| [2.6](./story-2.6-create-order-form.md) | Create Order Form with Dynamic Product Lines | P0 | Done |
| [2.7](./story-2.7-order-detail-status-transitions.md) | Order Detail View with Status Transitions | P0 | Done |
| [2.8](./story-2.8-client-order-history.md) | Client Detail - Display Order History | P1 | Done |

---

## Epic 3: Label Generation - Killer Feature (8 stories)

Generation d'etiquettes PDF reglementaires.

| Story | Titre | Priorite | Statut |
|-------|-------|----------|--------|
| [3.1](./story-3.1-pdf-service-foundation.md) | PDF Service Foundation with Apache PDFBox | P0 | Done |
| [3.2](./story-3.2-etiquette-configuration.md) | Etiquette Configuration & Data Model | P0 | Done |
| [3.3](./story-3.3-dluo-lot-number-logic.md) | DLUO Calculation & Lot Number Generation Logic | P0 | Done |
| [3.4](./story-3.4-single-label-layout.md) | Single Label PDF Layout & Rendering | P0 | Done |
| [3.5](./story-3.5-multi-label-sheet.md) | Multi-Label Sheet Generation | P0 | Done |
| [3.6](./story-3.6-label-generation-form.md) | Label Generation Form UI | P0 | Done |
| [3.7](./story-3.7-pdf-download-endpoint.md) | PDF Download Endpoint & File Response | P0 | Done |
| [3.8](./story-3.8-label-history-tracking.md) | Label Generation History & Lot Number Tracking | P2 | Done |

---

## Epic 4: Financial Dashboard & Purchases (8 stories)

Tableau de bord financier et suivi des achats.

| Story | Titre | Priorite | Statut |
|-------|-------|----------|--------|
| [4.1](./story-4.1-achat-entity-repository.md) | Achat Entity & Repository for Purchase Tracking | P0 | Done |
| [4.2](./story-4.2-achat-service.md) | AchatService with Expense Calculation Logic | P0 | Done |
| [4.3](./story-4.3-dashboard-service.md) | DashboardService with Financial Aggregations | P0 | Done |
| [4.4](./story-4.4-purchase-list-create-form.md) | Purchase List & Create Form | P1 | Done |
| [4.5](./story-4.5-purchase-edit-delete.md) | Purchase Edit & Delete | P1 | Done |
| [4.6](./story-4.6-financial-dashboard.md) | Financial Dashboard Main View | P0 | Done |
| [4.7](./story-4.7-top-products-widget.md) | Top Products Widget & Simple Charts | P2 | Done |
| [4.8](./story-4.8-expense-breakdown.md) | Expense Breakdown by Category | P2 | Done |

---

## Epic 5: Backup, Packaging & Production Readiness (8 stories)

Backup, packaging et preparation production.

| Story | Titre | Priorite | Statut |
|-------|-------|----------|--------|
| [5.1](./story-5.1-backup-service.md) | Backup Service with Scheduled Daily Backups | P0 | Pending |
| [5.2](./story-5.2-manual-backup-ui.md) | Manual Backup Export UI | P1 | Pending |
| [5.3](./story-5.3-global-exception-handler.md) | Global Exception Handler & User-Friendly Error Pages | P0 | Pending |
| [5.4](./story-5.4-confirmation-messages.md) | Confirmation Messages & Toast Notifications | P1 | Pending |
| [5.5](./story-5.5-launcher-script-icon.md) | Application Launcher Script & Icon | P1 | Pending |
| [5.6](./story-5.6-launch4j-exe-wrapper.md) | launch4j Configuration & .exe Wrapper | P1 | Pending |
| [5.7](./story-5.7-ux-polish-accessibility.md) | Final UX Polish & Accessibility Review | P1 | Pending |
| [5.8](./story-5.8-production-config-deployment.md) | Production Configuration & Deployment Checklist | P0 | Pending |

---

## Priorites

- **P0 - Critical Path:** Stories bloquantes pour le MVP
- **P1 - High:** Stories importantes mais non bloquantes
- **P2 - Medium:** Stories optionnelles pour le MVP

---

## Ordre de Developpement Recommande

1. **Epic 1** (Foundation) - DOIT etre complete en premier
2. **Epic 2** (Orders) - Depend de Epic 1
3. **Epic 3** (Labels) - Peut commencer apres Epic 1.1, killer feature
4. **Epic 4** (Dashboard) - Depend de Epic 2
5. **Epic 5** (Production) - Finalisation apres toutes les features

---

*Genere par John - Product Manager (BMAD Core)*
*Date: 2026-01-19*
