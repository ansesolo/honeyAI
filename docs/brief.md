# Project Brief: HoneyAI - Application de Gestion Apicole Familiale

**Version:** 1.0
**Date:** 2026-01-15
**Créé par:** Mary - Business Analyst
**Statut:** Draft Initial

---

## Executive Summary

HoneyAI est une application desktop standalone dédiée à la gestion d'une exploitation apicole familiale (~40 ruches). Elle remplace le système papier actuel (cahier de orders) par une solution numérique simple et intuitive, conçue pour des utilisateurs non-techniques. L'application centralise la gestion des clients, des orders, des stocks, de la production de miel et de l'élevage de reines, tout en facilitant l'impression d'étiquettes réglementaires. Elle vise à simplifier les tâches administratives chronophages (préparation d'étiquettes, suivi des ventes) et à fournir une visibilité financière claire (CA, dépenses, bénéfices) sans complexité comptable.

**Public cible:** Apiculteurs familiaux (2-3 utilisateurs), utilisation mono-poste, installation minimale requise.

**Valeur clé:** Gain de temps opérationnel, réduction des erreurs, meilleure visibilité de l'activité, tout en préservant la simplicité du workflow actuel.

---

## Problem Statement

### Situation Actuelle

Les parents du porteur de projet sont apiculteurs avec ~40 ruches et des ruchettes d'élevage de reines. Ils gèrent actuellement leur activité avec un système papier (cahier de orders) qui présente plusieurs limites:

**Pain Points Identifiés:**

1. **Gestion des orders fragmentée**
   - Commandes notées manuellement dans un cahier annuel
   - Difficulté à retrouver rapidement l'historique d'un client
   - Risque d'oublis ou d'erreurs dans le suivi (order/récupération/paiement)
   - Pas de vision consolidée du statut des orders en cours

2. **Préparation d'étiquettes chronophage**
   - Étiquettes réglementaires préparées manuellement ou semi-manuellement
   - Informations répétitives à remplir (nom, adresse, SIRET, DLUO, etc.)
   - Risque d'erreurs (DLUO mal calculée, informations manquantes)
   - Temps considérable passé sur une tâche répétitive

3. **Suivi financier approximatif**
   - Pas de vision claire du CA en temps réel
   - Suivi des dépenses (fournitures) approximatif
   - Calcul du bénéfice manuel et imprécis
   - Difficulté à analyser les tendances d'une année sur l'autre

4. **Gestion des stocks intuitive mais non formalisée**
   - Stock de pots préparés (500g/1kg) non centralisé
   - Stock de fournitures (cire, pots, couvercles) non suivi
   - Risque de rupture ou de sur-stockage

5. **Suivi production et élevage informel**
   - Production de miel par type (toutes fleurs, forêt, châtaignier) notée de manière dispersée
   - Élevage de reines (activité importante pour le père) non documenté systématiquement

### Impact

- **Temps perdu:** Estimation 3-5h/semaine en pleine saison (septembre-décembre) sur tâches administratives
- **Erreurs potentielles:** Commandes oubliées, paiements non suivis, étiquettes non conformes
- **Manque de visibilité:** Décisions commerciales prises "à l'intuition" sans données fiables
- **Frustration:** Tâches répétitives à faible valeur ajoutée (étiquettes)

### Pourquoi les Solutions Existantes Ne Conviennent Pas

- **Logiciels professionnels d'apiculture:** Trop complexes, coûteux, sur-dimensionnés pour une exploitation familiale
- **Excel/Google Sheets:** Nécessite compétences techniques, pas d'impression étiquettes intégrée, risque d'erreurs de formule
- **Applications cloud génériques (CRM, etc.):** Dépendance internet, courbe d'apprentissage, coût récurrent

### Urgence

Les parents approchent de la retraite. La simplification de leur gestion administrative leur permettrait de se concentrer sur l'aspect plaisant du métier (élevage de reines, travail au rucher) et de mieux transmettre l'activité si besoin.

---

## Proposed Solution

### Concept Central

HoneyAI est une **application desktop standalone ultra-simple**, pensée comme un "cahier numérique augmenté". Elle s'adapte au workflow existant des utilisateurs au lieu d'imposer de nouvelles méthodes de travail.

### Principes de Conception

1. **Zéro Courbe d'Apprentissage:** Interface intuitive inspirée du cahier papier actuel
2. **Installation Minimale:** Double-clic pour lancer, aucune configuration complexe
3. **Mono-Poste:** Données locales (SQLite), pas de dépendance cloud/serveur
4. **Gains Immédiats:** Fonctionnalités "killer" visibles dès le premier usage (étiquettes)
5. **Robustesse Absolue:** Backups automatiques, impossible de "casser" l'application

### Différenciateurs

**vs. Cahier papier:**
- Recherche instantanée de clients/orders
- Calculs automatiques (CA, stocks, DLUO)
- Étiquettes générées en quelques clics
- Historique complet conservé

**vs. Solutions professionnelles:**
- Simplicité radicale (pas de fonctionnalités inutiles)
- Sur-mesure pour le workflow spécifique de l'exploitation
- Pas de coût récurrent
- Support familial direct

**vs. Excel:**
- Pas de compétences techniques requises
- Interface guidée (pas de cellules vides angoissantes)
- Impression étiquettes intégrée
- Relations de données gérées automatiquement

### Vision Produit

Une application qui disparaît derrière l'usage: les parents l'utilisent naturellement sans y penser, comme ils utilisaient leur cahier, mais avec tous les bénéfices du numérique.

---

## Target Users

### Primary User Segment: Parents Apiculteurs

**Profil Démographique:**
- Âge: 50-65 ans
- Métier: Apiculteurs exploitants (activité principale ou complément retraite)
- Niveau technique: Utilisateurs ordinateur basiques (email, navigation web), aucune compétence technique avancée
- Localisation: France (réglementation française pour étiquettes)

**Workflow Actuel:**
- **Printemps/Été:** Récolte du miel, mise en pots anticipée (500g/1kg), travail au rucher
- **Septembre:** Période de orders (clients réguliers contactent)
- **Septembre-Décembre:** Préparation orders, livraisons/récupérations, encaissements
- **Toute l'année:** Achats fournitures occasionnels, élevage de reines

**Besoins Spécifiques:**
- Centraliser les orders clients (comme le cahier, mais mieux)
- Imprimer étiquettes réglementaires rapidement
- Suivre les clients réguliers (historique, préférences)
- Avoir une vision du CA et des dépenses
- Suivre la production par type de miel
- Documenter l'élevage de reines (motivation du père)

**Pain Points:**
- Peur de "casser quelque chose" avec l'ordinateur
- Frustration face aux interfaces complexes
- Pas de temps pour apprendre pendant la haute saison
- Besoin d'aide immédiate si problème technique

**Goals:**
- Simplifier les tâches administratives chronophages
- Ne plus perdre de temps sur les étiquettes
- Avoir une meilleure vision de l'activité
- Se concentrer sur le métier (rucher, reines) plutôt que l'administratif

### Secondary User Segment: Fils Développeur (Support Technique)

**Profil:**
- Développeur souhaitant créer une solution sur-mesure pour ses parents
- Responsable du support technique, maintenance, évolutions
- Connaissance du métier apicole (contexte familial)

**Besoins:**
- Code maintenable et documenté
- Déploiement simple (pas de serveur à gérer)
- Diagnostics à distance possibles
- Architecture extensible (évolutions futures)

---

## Goals & Success Metrics

### Business Objectives

- **Adoption complète:** Parents abandonnent le cahier papier dans les 2 mois suivant le déploiement
- **Gain de temps:** Réduction de 50% du temps passé sur tâches administratives (étiquettes, recherche orders)
- **Qualité des données:** 100% des orders tracées avec statuts clairs (vs. approximations papier)
- **Satisfaction utilisateurs:** Retour positif des parents ("c'est plus simple qu'avant")

### User Success Metrics

- **Facilité d'usage:** Parents capables d'enregistrer une order seuls après 2 démos
- **Fréquence d'usage:** Utilisation quotidienne en haute saison (septembre-décembre)
- **Autonomie:** Moins de 2 appels d'aide technique par mois après le premier mois
- **Confiance:** Parents font confiance aux données de l'application (ne vérifient plus sur papier)

### Key Performance Indicators (KPIs)

- **Temps préparation étiquettes:** < 2 minutes pour 10 étiquettes (vs. 15 minutes manuellement)
- **Temps recherche client:** < 10 secondes (vs. 2-5 minutes dans cahier)
- **Erreurs orders:** 0 order oubliée ou perdue
- **Taux d'adoption:** 100% des nouvelles orders saisies dans l'app dès semaine 3
- **Disponibilité:** 99.9% (application stable, pas de crashs)

---

## MVP Scope

### Core Features (Must Have)

#### 1. Gestion Clients
**Description:** Fiche client simple avec recherche et historique
**Rationale:** Base de tout le système, clients réguliers sont le cœur de l'activité
**Fonctionnalités:**
- Créer/Modifier/Supprimer client (avec confirmation)
- Champs: Nom, Téléphone, Email (optionnel), Adresse, Notes libres
- Recherche par nom/téléphone
- Vue historique orders automatique
- Soft delete (données non effacées physiquement)

#### 2. Gestion Commandes
**Description:** Workflow complet de la order au paiement
**Rationale:** Remplace directement le cahier papier, fonctionnalité #1 prioritaire
**Fonctionnalités:**
- Créer order: Sélection client + ajout produits (quantités)
- Statuts: "Commandée" → "Récupérée" → "Payée" (transitions simples via boutons)
- Vue liste par année (filtre année en cours par défaut)
- Recherche/Filtres: client, statut, date
- Impact stock automatique à la récupération
- Notes libres par order (ex: "livraison prévue 15/10")

#### 3. Catalogue Produits
**Description:** Liste des produits vendus avec tarifs annuels
**Rationale:** Nécessaire pour orders et étiquettes
**Fonctionnalités:**
- Produits prédéfinis: Miel 500g, Miel 1kg, Cire avec miel, Reines
- Types de miel: Toutes fleurs, Forêt, Châtaignier
- Tarifs par année (2024: Miel 500g = X€, 2025 = Y€)
- Application utilise automatiquement tarifs année en cours
- Modification tarifs future année (préparation)

#### 4. Impression Étiquettes PDF
**Description:** Génération étiquettes réglementaires pour pots de miel
**Rationale:** Fonctionnalité "killer", gain de temps énorme, valeur immédiate
**Fonctionnalités:**
- Formulaire simple: Type miel, Format pot (500g/1kg), Date récolte, Quantité étiquettes
- Génération PDF téléchargeable (planche d'étiquettes)
- Données auto-remplies: Nom, Adresse, SIRET, Tel, Prix, Poids, DLUO (calculée: date_récolte + 2 ans)
- Template configurable (dimensions étiquettes variables selon stock parents)
- Numéro de lot automatique (ex: 2024-FORET-001)

#### 5. Tableau de Bord Financier Simple
**Description:** Vue synthétique CA, dépenses, bénéfices
**Rationale:** Visibilité activité sans complexité comptable
**Fonctionnalités:**
- CA total (année en cours, filtrable par mois/année)
- Dépenses totales (achats fournitures)
- Bénéfice brut (CA - dépenses)
- Top 3 produits vendus
- Graphiques simples (barres/courbes) - pas de dashboards complexes

#### 6. Suivi Achats Fournitures
**Description:** Liste chronologique des dépenses
**Rationale:** Nécessaire pour calcul bénéfices et suivi budget
**Fonctionnalités:**
- Enregistrement achat: Date, Désignation, Montant, Catégorie (Cire, Pots, Couvercles, Nourrissement, Autre)
- Liste chronologique (filtrable par année/catégorie)
- Total dépenses calculé automatiquement

### Out of Scope for MVP

- **Gestion multi-postes / synchronisation** (mono-poste suffit)
- **Application mobile / tablette** (usage au bureau uniquement)
- **Suivi détaillé par ruche individuelle** (non utilisé actuellement)
- **Traçabilité sanitaire réglementaire complexe** (pas nécessaire pour vente directe petite échelle)
- **Intégration comptabilité** (pas de comptable, export CSV suffira en v2)
- **Gestion de stocks avancée** (prévisions, alertes seuils, etc.)
- **Module de facturation formelle** (vente directe, pas de factures légales requises)
- **E-commerce / vente en ligne** (vente par bouche-à-oreille uniquement)
- **Multilingue** (français uniquement)

### MVP Success Criteria

**L'application MVP sera considérée comme réussie si:**

1. Les parents enregistrent toutes leurs nouvelles orders dans l'application (abandon du cahier)
2. Les étiquettes générées sont conformes et imprimables sur leur imprimante
3. Aucune perte de données durant les 3 premiers mois
4. Les parents sont autonomes pour les opérations courantes (orders, clients, étiquettes)
5. Feedback utilisateurs positif: "C'est plus pratique qu'avant"

---

## Post-MVP Vision

### Phase 2 Features

**Fonctionnalités identifiées pour enrichissement post-adoption:**

1. **Gestion Stocks Produits Finis**
   - Stock pots préparés par type de miel et format (500g/1kg)
   - Conversion production → mise en pots
   - Alertes stock bas (optionnel)

2. **Suivi Production Miel Détaillé**
   - Enregistrement récoltes par type de miel (quantité kg, date récolte)
   - Calcul DLUO automatique basé sur date récolte
   - Historique productions annuelles

3. **Module Élevage de Reines**
   - Comptage ruchettes d'élevage
   - Production reines (date, quantité, notes)
   - Ventes reines (intégré orders)
   - Rationale: Important pour engagement utilisateur (père), mais non critique pour MVP

4. **Gestion Stocks Fournitures Avancée**
   - Lien achats → stock fournitures
   - Inventaire par type (cire, pots 500g, pots 1kg, couvercles, cadres, etc.)
   - Consommation automatique lors mise en pots

5. **Exports et Reporting**
   - Export CSV (clients, orders, achats) pour comptable/tableur
   - Génération rapport annuel (synthèse année)
   - Graphiques avancés (évolution CA multi-années)

### Long-term Vision (1-2 ans)

**Évolution possible selon adoption et retours:**

- **Multi-poste local:** Si besoin d'un 2ème ordinateur (réseau local)
- **Consultation mobile/tablette:** Vue lecture seule au rucher (stocks, production)
- **Sauvegarde cloud automatique:** Backup sécurisé (Google Drive, Dropbox)
- **Gestion multi-exploitations:** Si activité se développe (associés, transmission)

### Expansion Opportunities

**Opportunités stratégiques (non planifiées, ouvertes):**

- **Marketplace familiale:** Si d'autres apiculteurs familiaux intéressés (version SaaS?)
- **Intégration services tiers:** Comptabilité (API), déclarations réglementaires
- **Modules complémentaires:** Météo au rucher, traçabilité sanitaire avancée, génétique reines

**Note:** Ces expansions dépendront entièrement du succès MVP et de la demande réelle. Aucune décision prise à ce stade.

---

## Technical Considerations

### Platform Requirements

- **Target Platform:** Desktop Web Local (Windows prioritaire, Linux/Mac secondaires)
- **OS Support:** Windows 10+ (64 bits minimum)
- **Hardware Minimal:** 4 GB RAM, 500 MB espace disque, processeur 2 GHz
- **Software Requirements:**
  - JRE 17+ (embarqué dans package distribution)
  - Navigateur moderne (Chrome, Edge, Firefox - préinstallé Windows)
- **Performance Requirements:**
  - Démarrage application < 5 secondes (Spring Boot startup + ouverture navigateur)
  - Recherche client < 1 seconde (jusqu'à 1000 clients)
  - Génération PDF étiquettes < 5 secondes
  - Chargement pages web < 1 seconde

### Technology Preferences

**Architecture Globale:** Application web locale (Spring Boot) + Base de données locale

**Options Technologiques Évaluées:**

#### Option A: Python + PyQt5/6
- **Frontend:** PyQt5/6 (interface native, widgets simples)
- **Backend:** Python 3.10+
- **Database:** SQLite (fichier unique, zéro config)
- **PDF Generation:** reportlab (bibliothèque robuste)
- **Packaging:** PyInstaller (executable standalone Windows)

**Avantages:**
- Légèreté (~50 MB installeur avec Python embarqué)
- Excellente intégration SQLite
- Design UI sobre

**Inconvénients:**
- Courbe d'apprentissage si développeur non familier avec Python/PyQt
- Interface moins moderne

#### Option B: JavaFX/Swing
- **Frontend:** JavaFX (moderne) ou Swing (mature)
- **Backend:** Java 17+
- **Database:** SQLite + JDBC
- **PDF Generation:** Apache PDFBox ou iText

**Avantages:**
- Performances excellentes
- Application native

**Inconvénients:**
- Interface moderne nécessite effort JavaFX
- Swing look "daté"

#### Option C: Spring Boot + Web Local (RETENUE)
- **Frontend:** Thymeleaf + Bootstrap 5 + JavaScript Vanilla
- **Backend:** Spring Boot 3.2+ (Java 17 LTS)
- **Database:** SQLite + Spring Data JPA
- **PDF Generation:** Apache PDFBox
- **Packaging:** JAR exécutable + launch4j (wrapper .exe)

**Avantages:**
- **Interface moderne facile** (Bootstrap = UI professionnelle rapidement)
- **Développement rapide** si développeur expert Spring Boot
- **Architecture propre** (REST, séparation responsabilités)
- **Maintenance simplifiée** (stack standard, bien documentée)
- **Extensibilité future** (API déjà en place si besoins évoluent)
- Familiarité navigateur pour utilisateurs (déjà utilisé quotidiennement)

**Inconvénients:**
- Démarrage application ~4-5s (vs <3s demandé, mais acceptable)
- Taille installeur ~150-200 MB (JRE embarqué)
- Consommation mémoire ~150-200 MB (JVM)
- Lancement en 2 temps (exe → ouvre navigateur automatiquement)

**Décision:** **Spring Boot + Web Local (Java)** pour MVP (Option C)

**Rationale:**
- Développeur expert Spring Boot (métier quotidien) → développement MVP estimé ~40h (vs 60h+ autres options)
- Maintenance long terme assurée (stack maîtrisée)
- Interface moderne Bootstrap sans effort frontend avancé
- Simplicité maintenance prioritaire sur légèreté binaire
- Démarrage 4-5s validé comme acceptable par porteur de projet
- Toutes contraintes techniques respectées (mono-poste, offline, données locales)

### Architecture Considerations

#### Repository Structure
```
honeyAI/
├── src/main/java/com/honeyai/
│   ├── HoneyAiApplication.java          # Main Spring Boot
│   ├── config/
│   │   └── DatabaseConfig.java          # Config SQLite
│   ├── controller/
│   │   ├── HomeController.java          # Dashboard
│   │   ├── ClientController.java        # CRUD clients
│   │   ├── CommandeController.java      # Gestion orders
│   │   ├── ProduitController.java       # Catalogue/tarifs
│   │   ├── AchatController.java         # Achats fournitures
│   │   └── EtiquetteController.java     # Génération PDF
│   ├── service/
│   │   ├── ClientService.java
│   │   ├── CommandeService.java
│   │   ├── ProduitService.java
│   │   ├── DashboardService.java        # Calculs CA, stats
│   │   ├── PdfService.java              # Génération étiquettes
│   │   └── BackupService.java           # Backup automatique
│   ├── repository/
│   │   ├── ClientRepository.java        # Spring Data JPA
│   │   ├── CommandeRepository.java
│   │   ├── ProduitRepository.java
│   │   └── AchatRepository.java
│   ├── model/
│   │   ├── Client.java                  # Entités JPA
│   │   ├── Commande.java
│   │   ├── LigneCommande.java
│   │   ├── Produit.java
│   │   ├── Tarif.java
│   │   └── Achat.java
│   ├── enums/
│   │   ├── StatutCommande.java          # COMMANDEE, RECUPEREE, PAYEE
│   │   ├── TypeMiel.java                # TOUTES_FLEURS, FORET, CHATAIGNIER
│   │   └── CategorieAchat.java
│   └── exception/
│       └── GlobalExceptionHandler.java
├── src/main/resources/
│   ├── application.yml                  # Config Spring
│   ├── static/                          # CSS, JS, images
│   │   ├── css/
│   │   ├── js/
│   │   └── icons/
│   └── templates/                       # Thymeleaf templates
│       ├── fragments/                   # Navbar, footer
│       ├── clients/
│       ├── orders/
│       ├── etiquettes/
│       └── dashboard.html
├── data/
│   └── honeyai.db                       # SQLite (généré runtime)
├── docs/                                # Documentation projet
├── tests/                               # Tests unitaires
├── launcher/
│   ├── honeyai-launcher.xml             # Config launch4j
│   └── icon.ico
├── pom.xml                              # Maven dependencies
└── README.md
```

#### Service Architecture
- **Monolithique simple:** Pas de microservices (overkill)
- **Séparation logique:** Controllers / Services / Repositories (pattern MVC)
- **API REST interne:** Communication HTTP localhost uniquement (pas d'exposition réseau)
- **Server-side rendering:** Thymeleaf pour génération HTML (pas de SPA complexe)

#### Database Schema (SQLite + JPA)

**Tables Principales (générées par Hibernate):**

**Note:** Le schéma est défini via annotations JPA sur les entités Java. Hibernate génère automatiquement les tables SQLite.

**Entités JPA principales:**

- **Client** (@Entity): id, nom, telephone, email, adresse, notes, createdAt, updatedAt, deletedAt (soft delete)
- **Produit** (@Entity): id, nom, type (enum TypeMiel), unite
- **Tarif** (@Entity): id, produitId, annee, prix - @UniqueConstraint(produitId, annee)
- **Commande** (@Entity): id, clientId, dateCommande, statut (enum StatutCommande), notes, createdAt, updatedAt
- **LigneCommande** (@Entity): id, commandeId, produitId, quantite, prixUnitaire
- **Achat** (@Entity): id, dateAchat, designation, montant, categorie (enum CategorieAchat), notes, createdAt

**Relations JPA:**
```java
// Client ↔ Commandes (OneToMany)
@OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
private List<Commande> orders;

// Commande ↔ LignesCommande (OneToMany)
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
private List<LigneCommande> lignes;

// Produit ↔ Tarifs (OneToMany)
@OneToMany(mappedBy = "produit")
private List<Tarif> tarifs;
```

**Enums Java:**
- `StatutCommande`: COMMANDEE, RECUPEREE, PAYEE
- `TypeMiel`: TOUTES_FLEURS, FORET, CHATAIGNIER
- `CategorieAchat`: CIRE, POTS, COUVERCLES, NOURRISSEMENT, AUTRE

**Phase 2 (post-MVP):**
- **ProductionMiel** (@Entity): id, annee, typeMiel, quantiteKg, dateRecolte, notes
- **ElevageReine** (@Entity): id, date, nbReinesProduites, nbRuchettes, notes

#### Integration Requirements
- **Imprimante:** Génération PDF standard (téléchargement navigateur → impression via lecteur PDF système ou Ctrl+P)
- **Navigateur:** Application s'ouvre dans navigateur par défaut (Chrome, Edge, Firefox)
- **Système fichiers:** Accès lecture/écriture pour backups automatiques, base SQLite locale
- **Pas d'intégrations externes:** Aucune API tierce (volontairement autonome, localhost uniquement)

#### Security/Compliance Considerations

**Sécurité:**
- **Données locales uniquement:** Pas d'exposition réseau
- **Pas d'authentification:** Application mono-utilisateur familial (pas nécessaire)
- **Backup chiffré optionnel:** Si besoin (v2)

**Conformité:**
- **RGPD:** Données clients stockées localement, contrôle total utilisateur
- **Étiquettes miel:** Conformité réglementation française (DGCCRF)
  - Mentions obligatoires: Dénomination, Quantité, DLUO, Nom/Adresse, Lot
  - Validation avec Chambre d'Agriculture recommandée

**Backup & Disaster Recovery:**
- **Backup automatique quotidien:** Copie `honeyai.db` vers dossier `backups/` avec timestamp
- **Export manuel:** Bouton "Sauvegarder mes données" → fichier `.db` daté
- **Restauration:** Import fichier `.db` sauvegardé
- **Stratégie 3-2-1:** Encourager parents à copier backups sur clé USB externe

---

## Constraints & Assumptions

### Constraints

#### Budget
- **Développement:** Temps développeur bénévole (fils)
- **Infrastructure:** Coût zéro (pas d'hébergement, pas d'abonnement)
- **Maintenance:** Support familial gratuit
- **Contrainte:** Minimiser complexité pour réduire temps développement/maintenance

#### Timeline
- **Cible lancement:** Octobre/Novembre 2026 (hors saison, période calme)
- **Rationale:** Éviter lancement en haute saison (septembre-décembre = stress)
- **Développement MVP:** Estimation 40-45h (5-6 semaines à temps partiel)
  - Setup projet + modèles JPA: 4h
  - Services + Repositories Spring Data: 8h
  - Controllers + Templates Thymeleaf: 15h
  - Génération PDF étiquettes: 6h
  - Backup automatique: 2h
  - Tests + Debugging: 6h
  - Packaging (JAR + launcher): 4h
- **Phase tests utilisateurs:** 2-4 semaines (avec parents, période test calme)

#### Resources
- **Développeur:** 1 personne (fils), temps partiel
- **Testeurs:** 2 personnes (parents apiculteurs)
- **Infrastructure:** 1 ordinateur (Windows), 1 imprimante jet d'encre
- **Pas d'équipe:** Design UI sobre (pas de designer), QA manuelle uniquement

#### Technical Constraints
- **Mono-poste:** Application non conçue pour multi-postes initialement
- **Offline-first:** Aucune dépendance internet (volontaire)
- **Imprimante standard:** Pas d'imprimante d'étiquettes spécialisée (jet d'encre commerce)
- **OS cible:** Windows prioritaire (Linux/Mac secondaire si facile)

### Key Assumptions

- **Assumption 1:** Les parents sont prêts à abandonner le cahier papier si l'application est vraiment plus simple
  - **Risque:** Résistance au changement
  - **Mitigation:** Démo convaincante, fonctionnalité killer (étiquettes), formation douce

- **Assumption 2:** L'ordinateur actuel est suffisamment performant (Windows 10+, 4GB RAM)
  - **Risque:** Matériel obsolète
  - **Mitigation:** Vérification specs AVANT développement

- **Assumption 3:** Les besoins métier sont correctement compris
  - **Risque:** Complexité cachée découverte post-développement
  - **Mitigation:** Session observation workflow réel, prototype jetable

- **Assumption 4:** Pas de besoins multi-utilisateurs simultanés (usage tour de rôle)
  - **Risque:** Conflits de saisie si usage simultané
  - **Mitigation:** Clarification usage réel avec parents

- **Assumption 5:** Backup manuel suffit initialement (clé USB)
  - **Risque:** Oubli de brancher clé USB, perte données
  - **Mitigation:** Backup auto local + alertes visuelles

- **Assumption 6:** Pas d'évolution réglementaire majeure étiquettes miel
  - **Risque:** Changement législation nécessite refonte
  - **Mitigation:** Template étiquettes configurable/modifiable

- **Assumption 7:** Volumétrie reste faible (<500 clients, <2000 orders)
  - **Risque:** Performances si croissance activité
  - **Mitigation:** SQLite performant jusqu'à 10k+ entrées, index DB

---

## Risks & Open Questions

### Key Risks

#### Risque 1: Abandon Utilisateur (Probabilité: ÉLEVÉE | Impact: CRITIQUE)
**Description:** Les parents trouvent l'application trop compliquée et retournent au cahier papier.
**Impact:** Échec total du projet, temps développement perdu, frustration familiale.
**Mitigations:**
- Lancement hors saison (période calme pour apprentissage)
- Interface ULTRA-simple (gros boutons, pas de menus cachés)
- Formation en présentiel (vous présent physiquement)
- Hotline familiale réactive
- Guide papier illustré à côté de l'ordinateur

#### Risque 2: Perte de Données (Probabilité: MOYENNE | Impact: CATASTROPHIQUE)
**Description:** Crash ordinateur, corruption DB, suppression accidentelle.
**Impact:** Perte historique clients (10+ ans), perte orders en cours, perte confiance totale.
**Mitigations (OBLIGATOIRES):**
- Backup automatique quotidien (dossier local + clé USB)
- Export manuel facile ("Sauvegarder mes données")
- Confirmation avant suppressions
- Soft delete (données marquées supprimées, pas effacées)
- Versioning DB pour migrations sécurisées
- Logs d'erreurs pour debug

#### Risque 3: Ordinateur Inadapté (Probabilité: MOYENNE | Impact: ÉLEVÉ)
**Description:** Matériel trop vieux, imprimante incompatible, résolution écran faible.
**Impact:** Application inutilisable, impossibilité d'imprimer étiquettes.
**Mitigations:**
- Vérification specs ordinateur/imprimante AVANT développement
- Tests sur machine cible réguliers durant dev
- Requirements minimums définis et communiqués
- Application légère (<100 MB)

#### Risque 4: Complexité Métier Cachée (Probabilité: ÉLEVÉE | Impact: ÉLEVÉ)
**Description:** Découverte post-dev de besoins non exprimés ("ah oui, on fait aussi...").
**Impact:** Scope creep, refonte architecture, frustration utilisateurs.
**Mitigations:**
- Session observation (regarder parents travailler avec cahier)
- Prototype jetable rapide (découvrir besoins cachés)
- Architecture extensible (facile d'ajouter fonctionnalités)
- Champs "Notes" partout (capture besoins non structurés)

#### Risque 5: Étiquettes Non Conformes Réglementairement (Probabilité: FAIBLE | Impact: ÉLEVÉ)
**Description:** DLUO mal calculée, mentions obligatoires manquantes, contrôle DGCCRF négatif.
**Impact:** Amendes, non-conformité sanitaire, perte confiance.
**Mitigations:**
- Template étiquette validé avec parents avant dev
- Vérification réglementaire (consulter Chambre Agriculture)
- Calcul DLUO testé (date_récolte + exactement 730 jours)
- Numéro de lot automatique

#### Risque 6: Maintenance Long Terme Insoutenable (Probabilité: ÉLEVÉE | Impact: MOYEN)
**Description:** Vous êtes support technique à vie, bugs après 6 mois, demandes évolutions continues.
**Impact:** Charge maintenance élevée, burnout, obsolescence.
**Mitigations:**
- Code propre et documenté (commentaires, README)
- Tests automatisés (prévenir régressions)
- Documentation technique complète
- Considérer solution commerce si charge trop élevée

#### Risque 7: Fracture Numérique Générationnelle (Probabilité: MOYENNE | Impact: MOYEN)
**Description:** Peur de "casser", oublis après 4 mois sans usage (saisonnalité).
**Impact:** Dépendance totale à vous, frustration utilisateurs.
**Mitigations:**
- Mode guidé (wizards pour actions complexes)
- Messages encourageants ("Bien enregistré ✓")
- Undo facile ("Annuler dernière action")
- Aide contextuelle (? à côté de chaque action)
- Design intuitif (pas besoin mémoriser)

### Open Questions

**Questions nécessitant réponses AVANT développement:**

1. **Specs techniques ordinateur actuel:** OS exact? RAM? Espace disque? Modèle imprimante?
2. **Validation workflow:** Observer 1 journée complète de travail avec cahier (septembre idéal)?
3. **Usage multi-utilisateurs:** Parents utilisent l'ordinateur simultanément ou à tour de rôle?
4. **Template étiquettes:** Dimensions exactes des étiquettes utilisées? (60x40mm, 70x37mm, autre?)
5. **Réglementaire:** Conformité actuelle étiquettes papier? Validation Chambre Agriculture nécessaire?
6. **Backup:** Où sauvegarder? Clé USB disponible? Espace cloud envisageable (Dropbox, Google Drive)?
7. **Historique migration:** Faut-il migrer orders en cours du cahier vers app (période transition)?
8. **Besoins cachés:** Y a-t-il d'autres workflows non mentionnés? (vente marché, échanges cire, locations ruches?)

**Questions pour Phase 2 (post-MVP):**

9. **Évolution activité:** Prévision croissance (+ de ruches, + de clients)?
10. **Transmission:** Projet de transmission activité à moyen terme (5 ans)?
11. **Besoins comptables:** Comptable externe? Format exports requis?

### Areas Needing Further Research

1. **Réglementation étiquetage miel (France 2026)**
   - Vérifier mentions obligatoires exactes (DGCCRF)
   - Numéro de lot: format requis?
   - Allergènes: mention nécessaire si cire avec miel?
   - Contact: Chambre d'Agriculture locale

2. **Solutions existantes apiculture**
   - Benchmark: Beekeepers Manager, Apiary Book, autres?
   - Identifier forces/faiblesses (éviter mêmes erreurs)
   - Fonctionnalités appréciées/détestées

3. **Packaging Python applicatif**
   - PyInstaller vs. cx_Freeze vs. Nuitka
   - Taille executable final réaliste
   - Compatibilité antivirus (faux positifs?)
   - Installation silencieuse possible?

4. **Génération PDF étiquettes**
   - reportlab: capabilities exactes pour layout étiquettes?
   - Alternative: weasyprint, fpdf2?
   - Gestion marges imprimante (preview avant impression?)

5. **UX pour non-techniques**
   - Patterns UI pour seniors/peu techniques
   - Taille police minimale (lisibilité)
   - Couleurs accessibles (contraste)
   - Études de cas: applications simples réussies

---

## Appendices

### A. Research Summary

**Sources d'information utilisées:**

1. **Élicitation approfondie avec porteur de projet (fils)**
   - 3 sessions d'analyse (Chain of Thought, Agile Team Perspectives, Risk Analysis)
   - Compréhension détaillée workflow apicole actuel
   - Identification besoins explicites et implicites

2. **Analyse comparative solutions existantes**
   - Constat: Logiciels pros trop complexes pour usage familial
   - Excel/Sheets: Manque d'intégration (étiquettes, etc.)
   - Solutions cloud: Dépendance internet non souhaitée

3. **Analyse risques multi-perspectives**
   - PO: Priorisation valeur utilisateur (étiquettes = killer feature)
   - SM: Identification risques adoption (formation, changement)
   - Dev: Évaluation options techniques (Python recommandé)
   - QA: Scénarios critiques (perte données, conformité étiquettes)

**Conclusions clés:**
- Simplicité radicale est le critère de succès #1
- Fonctionnalité "killer" (étiquettes) doit être démo initiale
- Robustesse données (backups) est non-négociable
- Lancement hors saison est crucial pour adoption

### B. Stakeholder Input

**Porteur de projet (Fils développeur):**
- Objectif: Simplifier vie de ses parents, gain de temps administratif
- Contrainte: Développement temps partiel, pas de budget
- Préoccupation: Maintenance long terme soutenable

**Utilisateurs finaux (Parents apiculteurs):**
- Demande explicite: Remplacement cahier papier, aide étiquettes
- Contrainte: Confort tech limité, pas de temps formation en saison
- Motivation: Se concentrer sur métier plaisant (élevage reines, rucher)

### C. References

**Réglementation:**
- DGCCRF - Étiquetage des miels: [https://www.economie.gouv.fr/dgccrf/Publications/Vie-pratique/Fiches-pratiques/Miel](https://www.economie.gouv.fr/dgccrf/Publications/Vie-pratique/Fiches-pratiques/Miel)
- Code de la consommation - Mentions obligatoires denrées alimentaires

**Technologies:**
- Java: [https://www.oracle.com/java/](https://www.oracle.com/java/)
- Spring Boot: [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
- Spring Data JPA: [https://spring.io/projects/spring-data-jpa](https://spring.io/projects/spring-data-jpa)
- Thymeleaf: [https://www.thymeleaf.org/](https://www.thymeleaf.org/)
- Bootstrap: [https://getbootstrap.com/](https://getbootstrap.com/)
- SQLite: [https://www.sqlite.org/](https://www.sqlite.org/)
- SQLite JDBC Driver: [https://github.com/xerial/sqlite-jdbc](https://github.com/xerial/sqlite-jdbc)
- Apache PDFBox: [https://pdfbox.apache.org/](https://pdfbox.apache.org/)
- launch4j: [https://launch4j.sourceforge.net/](https://launch4j.sourceforge.net/)

**Communauté apicole:**
- Forums apiculteurs français (retours outils gestion)
- Chambre d'Agriculture locale (contact pour validation étiquettes)

---

## Next Steps

### Immediate Actions

1. **Validation Specs Techniques** (Priorité: P0)
   - Vérifier OS, RAM, espace disque ordinateur parents
   - Noter modèle exact imprimante (compatibilité PDF)
   - Prendre photo setup actuel (bureau, écran)

2. **Observation Workflow Réel** (Priorité: P0)
   - Planifier 1 journée observation en septembre (haute saison)
   - Regarder parents utiliser cahier papier (noter workflow exact)
   - Identifier besoins cachés non exprimés

3. **Validation Template Étiquettes** (Priorité: P0)
   - Récupérer étiquettes actuelles (photo ou scan)
   - Mesurer dimensions exactes (largeur x hauteur mm)
   - Lister toutes mentions présentes
   - Consulter Chambre Agriculture si doute conformité

4. **Clarification Usage Multi-Utilisateurs** (Priorité: P1)
   - Demander parents: utilisent ordinateur en même temps ou tour de rôle?
   - Si simultané: prévoir gestion conflits (locks)
   - Si tour de rôle: SQLite standard suffit

5. **Définition Stratégie Backup** (Priorité: P0)
   - Discuter avec parents: où sauvegarder? (clé USB? cloud?)
   - Acheter clé USB dédiée si nécessaire (petite dépense)
   - Définir fréquence backup (quotidien recommandé)

6. **Prototypage Rapide** (Priorité: P1 - post validations ci-dessus)
   - Développer prototype jetable (3-5 jours)
   - Focus: Formulaire order + liste orders uniquement
   - Tester avec parents (découvrir frictions UX)

7. **Setup Projet Développement** (Priorité: P1)
   - Initialiser repo Git (GitHub/GitLab)
   - Structure dossiers (src/, docs/, tests/)
   - Requirements.txt (PyQt5, SQLAlchemy, reportlab)
   - README.md avec vision projet

8. **Planning Développement MVP** (Priorité: P2)
   - Découper MVP en sprints (1-2 semaines chacun)
   - Sprint 1: DB + Gestion Clients
   - Sprint 2: Gestion Commandes
   - Sprint 3: Impression Étiquettes PDF
   - Sprint 4: Tableau de bord financier + Achats
   - Sprint 5: Tests utilisateurs + corrections

9. **Préparation Formation Utilisateurs** (Priorité: P2)
   - Rédiger guide utilisateur illustré (captures écran)
   - Préparer démo étape par étape (script formation)
   - Planifier 2-3 sessions formation (présentiel)

10. **Communication Lancement** (Priorité: P3)
    - Annoncer projet aux parents (gérer attentes)
    - Expliquer timeline (pas disponible avant octobre/novembre)
    - Souligner: participation active requise (tests, feedback)

### PM Handoff

Ce Project Brief fournit le contexte complet pour **HoneyAI - Application de Gestion Apicole Familiale**.

**Décision Technique Validée:** Spring Boot + Web Local (Java)
- Développeur expert Spring Boot → développement rapide (~40h)
- Architecture: Spring Boot 3.2+, Spring Data JPA, SQLite, Thymeleaf, Bootstrap 5
- Packaging: JAR exécutable + wrapper .exe (launch4j)
- Interface web moderne accessible via navigateur local

**Prochaine étape recommandée:** Créer le PRD (Product Requirements Document) détaillé basé sur ce brief.

Le PRD devra spécifier:
- User stories détaillées avec critères d'acceptation
- Wireframes/mockups UI Bootstrap (interfaces web)
- Spécifications techniques précises (entités JPA, endpoints controllers, templates Thymeleaf)
- Plan de tests (tests unitaires Spring Boot, tests utilisateurs)
- Roadmap développement (sprints, jalons)

**Handoff au Product Manager:** Merci de commencer en "PRD Generation Mode", de revoir ce brief minutieusement, et de travailler avec le porteur de projet pour créer le PRD section par section en demandant clarifications et en suggérant améliorations.

---

**Document généré par:** Mary - Business Analyst
**Méthodologie:** BMAD™ Core - Élicitation Approfondie (Chain of Thought, Agile Team Perspectives, Risk Analysis)
**Statut:** Ready for PM Review

