# Requirements

## Functional Requirements

**FR1:** Le système doit permettre de créer, modifier, consulter et supprimer (soft delete) des fiches clients contenant: nom (obligatoire), téléphone, email (optionnel), adresse, et notes libres.

**FR2:** Le système doit fournir une fonction de recherche de clients par nom ou numéro de téléphone avec résultats instantanés (<1 seconde).

**FR3:** Le système doit afficher automatiquement l'historique complet des orders pour chaque client sur sa fiche détaillée.

**FR4:** Le système doit permettre de créer une order en sélectionnant un client et en ajoutant des produits avec leurs quantités.

**FR5:** Le système doit gérer trois statuts de order avec transitions simples via boutons: "Commandée" → "Récupérée" → "Payée".

**FR6:** Le système doit mettre à jour automatiquement le stock de produits finis lors du passage d'une order au statut "Récupérée".

**FR7:** Le système doit permettre de filtrer et rechercher les orders par client, statut, et date avec vue par année (année en cours par défaut).

**FR8:** Le système doit gérer un catalogue de produits prédéfinis: Miel (500g/1kg), Cire avec miel, Reines avec types de miel (Toutes fleurs, Forêt, Châtaignier).

**FR9:** Le système doit permettre de définir des tarifs par produit et par année, avec application automatique des tarifs de l'année en cours lors de la création de orders.

**FR10:** Le système doit générer des étiquettes réglementaires pour pots de miel au format PDF téléchargeable via un formulaire simple (type miel, format pot, date récolte, quantité).

**FR11:** Les étiquettes générées doivent contenir automatiquement: nom exploitation, adresse, SIRET, téléphone, prix, poids, DLUO calculée (date récolte + 2 ans), et numéro de lot automatique.

**FR12:** Le système doit afficher un tableau de bord financier présentant: CA total (filtrable par mois/année), dépenses totales, bénéfice brut (CA - dépenses), et top 3 produits vendus.

**FR13:** Le système doit permettre d'enregistrer des achats de fournitures avec: date, désignation, montant, catégorie (Cire, Pots, Couvercles, Nourrissement, Autre), et notes.

**FR14:** Le système doit afficher une liste chronologique des achats filtrable par année et catégorie avec calcul automatique du total des dépenses.

**FR15:** Le système doit effectuer un backup automatique quotidien de la base de données SQLite vers un dossier local avec horodatage.

**FR16:** Le système doit permettre l'export manuel de la base de données via un bouton "Sauvegarder mes données" créant un fichier .db daté.

**FR17:** Le système doit afficher des messages de confirmation clairs après chaque action importante (enregistrement, suppression, modification) pour rassurer l'utilisateur.

**FR18:** Le système doit permettre d'ajouter des notes libres sur les orders (ex: "livraison prévue 15/10") pour capturer des informations non structurées.

## Non-Functional Requirements

**NFR1:** L'application doit démarrer en moins de 5 secondes (démarrage Spring Boot + ouverture automatique du navigateur).

**NFR2:** Toutes les recherches et requêtes doivent retourner des résultats en moins de 1 seconde pour des volumes allant jusqu'à 1000 clients et 2000 orders.

**NFR3:** La génération d'une planche de 10 étiquettes PDF doit prendre moins de 5 secondes.

**NFR4:** Le chargement de chaque page web doit prendre moins de 1 seconde.

**NFR5:** L'interface utilisateur doit utiliser une police de taille minimale de 16px pour assurer la lisibilité pour des utilisateurs de 50-65 ans.

**NFR6:** L'application doit fonctionner entièrement en mode offline sans aucune dépendance réseau (localhost uniquement).

**NFR7:** L'application doit être compatible Windows 10+ (64 bits) avec un minimum de 4 GB RAM et 500 MB d'espace disque.

**NFR8:** L'application doit être distribuée sous forme d'un exécutable .exe autonome (JAR + wrapper launch4j) avec JRE 17 embarqué.

**NFR9:** L'application doit stocker toutes les données localement dans un fichier SQLite unique sans communication externe.

**NFR10:** L'interface doit utiliser de gros boutons et des formulaires simples pour minimiser les erreurs de manipulation par des utilisateurs non-techniques.

**NFR11:** Les suppressions de données doivent être des soft deletes (marquage supprimé, pas effacement physique) pour prévenir les pertes accidentelles.

**NFR12:** L'application doit afficher un splash screen "HoneyAI démarre..." pendant le chargement pour indiquer clairement que l'application se lance.

**NFR13:** L'application doit maintenir une disponibilité de 99.9% (aucun crash, gestion robuste des erreurs).

**NFR14:** Le code doit être maintenable avec une architecture Spring Boot standard (Controllers/Services/Repositories) et commenté pour faciliter le support long terme.

**NFR15:** Les backups automatiques doivent conserver les 30 derniers jours d'historique et supprimer automatiquement les backups plus anciens.

**NFR16:** L'application doit ouvrir automatiquement le navigateur par défaut sur localhost:8080 au démarrage sans intervention manuelle.

**NFR17:** Les étiquettes PDF générées doivent être conformes à la réglementation DGCCRF française pour l'étiquetage du miel (mentions obligatoires complètes).

---
