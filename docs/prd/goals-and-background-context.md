# Goals and Background Context

## Goals

Les objectifs à atteindre si ce PRD est implémenté avec succès:

- Remplacer complètement le système papier (cahier de orders) par une solution numérique intuitive
- Réduire de 50% le temps passé sur les tâches administratives (étiquettes, recherche orders)
- Générer des étiquettes réglementaires conformes en moins de 2 minutes pour 10 étiquettes
- Fournir une visibilité financière claire (CA, dépenses, bénéfices) sans complexité comptable
- Permettre aux parents apiculteurs (utilisateurs non-techniques) d'être autonomes après 2 démonstrations
- Assurer une adoption complète avec abandon du cahier papier dans les 2 mois suivant le déploiement
- Garantir 0 perte de données avec backups automatiques et robustesse absolue
- Simplifier le workflow existant sans imposer de nouvelles méthodes de travail

## Background Context

HoneyAI répond au besoin d'une exploitation apicole familiale (~40 ruches) actuellement gérée via un système papier (cahier de orders) qui présente des limites critiques: gestion fragmentée des orders, préparation chronophage d'étiquettes réglementaires, suivi financier approximatif, et absence de centralisation des données clients et production.

L'application vise à transformer ces processus tout en préservant la simplicité du workflow actuel. Contrairement aux solutions professionnelles existantes (trop complexes et coûteuses) ou aux alternatives génériques (Excel, applications cloud), HoneyAI est conçue sur mesure pour des utilisateurs non-techniques (50-65 ans, compétences informatiques basiques) qui ont besoin d'une solution standalone, offline-first, avec une courbe d'apprentissage nulle. Le développement est assuré par un membre de la famille expert Spring Boot, garantissant maintenance et support à long terme. La décision technique (Spring Boot + interface web locale Bootstrap) permet un développement rapide (~40h) tout en offrant une interface moderne sans nécessiter d'expertise frontend avancée.

## Change Log

| Date | Version | Description | Author |
|------|---------|-------------|--------|
| 2026-01-17 | 1.0 | PRD initial créé à partir du Project Brief v1.0 | Mary (Business Analyst) |

---
