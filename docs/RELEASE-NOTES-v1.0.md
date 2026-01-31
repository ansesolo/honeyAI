# Notes de Version - HoneyAI v1.0.0

**Date de sortie** : Janvier 2026

---

## Fonctionnalites

### Gestion des Clients (Epic 1)
- Creation, modification et suppression de clients
- Fiche client detaillee avec coordonnees completes
- Recherche et liste des clients
- Affichage mobile responsive avec vue en cartes

### Commandes et Produits (Epic 2)
- Catalogue de produits (type de miel, poids, tarif)
- Creation de commandes multi-lignes avec calcul automatique des totaux
- Suivi des commandes par statut : Commandee, Recuperee, Payee
- Modification du prix unitaire par commande avec indicateur visuel
- Filtrage des commandes par annee et statut

### Generation d'Etiquettes (Epic 3)
- Generation de planches d'etiquettes au format PDF
- Conformite reglementaire : SIRET, DLUO, numero de lot, poids net
- Configuration des dimensions et disposition des etiquettes
- Historique des generations avec re-telechargement

### Tableau de Bord et Achats (Epic 4)
- Tableau de bord avec indicateurs cles : clients, commandes, chiffre d'affaires
- Graphique des revenus mensuels
- Gestion des achats et depenses par categorie
- Suivi financier (revenus vs depenses)

### Sauvegarde et Production (Epic 5)
- Sauvegarde et restauration de la base de donnees
- Sauvegarde automatique au demarrage
- Gestion de l'historique des sauvegardes
- Messages de confirmation (toast) sur toutes les operations
- Lanceur Windows (.bat) avec ouverture automatique du navigateur
- Configuration launch4j pour creation d'un .exe
- Interface accessible (WCAG AA) avec navigation au clavier
- Profil de production optimise

---

## Specifications techniques

- **Langage** : Java 21
- **Framework** : Spring Boot 3.5.x
- **Base de donnees** : SQLite (fichier local, zero configuration)
- **Interface** : Thymeleaf + Bootstrap 5.3.2
- **Generation PDF** : Apache PDFBox
- **Systeme d'exploitation** : Windows 10/11

---

## Limitations connues

- Application mono-utilisateur (pas de gestion de comptes)
- Pas d'acces reseau / multi-poste
- Les etiquettes sont generees en PDF uniquement (pas d'impression directe)
- Pas d'export des donnees (CSV, Excel)
- Le tableau de bord ne couvre que l'annee en cours
- Pas de gestion des stocks / inventaire des ruches

---

## Ameliorations futures envisagees

- Export des donnees clients et commandes en CSV
- Statistiques multi-annees sur le tableau de bord
- Gestion des ruches et du suivi sanitaire
- Mode sombre pour l'interface
- Sauvegarde automatique planifiee (quotidienne/hebdomadaire)
