# User Interface Design Goals

## Overall UX Vision

HoneyAI adopte une philosophie de "cahier numérique augmenté" - une interface qui disparaît derrière l'usage naturel, permettant aux parents apiculteurs d'accomplir leurs tâches sans y penser, exactement comme ils utilisaient leur cahier papier, mais avec tous les bénéfices du numérique.

**Principes directeurs:**
- **Zéro courbe d'apprentissage:** L'interface s'inspire du workflow papier existant plutôt que d'imposer de nouvelles méthodes
- **Simplicité radicale:** Aucune fonctionnalité inutile, navigation évidente, pas de menus cachés
- **Confiance et contrôle:** Messages de confirmation clairs, impossible de "casser" l'application, actions réversibles
- **Lisibilité optimale:** Police grande (16px minimum), contraste élevé, espacement généreux pour utilisateurs 50-65 ans
- **Efficacité immédiate:** Les tâches fréquentes (nouvelle commande, génération étiquettes) accessibles en 2 clics maximum

## Key Interaction Paradigms

**Navigation principale:**
- Menu latéral persistant avec grandes icônes et labels textuels clairs (Clients, Commandes, Étiquettes, Tableau de bord, Achats)
- Pas de navigation hiérarchique complexe - toutes les fonctions principales au même niveau
- Fil d'Ariane simple pour indiquer la position actuelle

**Formulaires:**
- Champs larges avec labels au-dessus (pas à côté) pour clarté
- Validation en temps réel avec messages encourageants ("✓ Bien enregistré")
- Boutons d'action principaux en vert/large, secondaires en gris/plus petits
- Ordre des champs suit la logique conversationnelle (comme remplir le cahier papier)

**Listes et recherche:**
- Barre de recherche proéminente en haut avec placeholder explicite ("Rechercher un client par nom ou téléphone...")
- Résultats affichés en cartes ou tableaux avec alternance de couleurs pour lisibilité
- Pas de pagination complexe - scroll infini ou "Voir plus" simple

**Actions destructives:**
- Confirmation modale explicite ("Êtes-vous sûr de vouloir supprimer ce client?")
- Boutons de suppression en rouge, positionnés à l'écart des actions principales
- Toujours possibilité d'annuler ou de restaurer (soft delete)

**Feedback utilisateur:**
- Toast notifications en haut à droite pour confirmations rapides
- Messages d'erreur en rouge clair, expliquant clairement le problème et la solution
- Indicateurs de chargement pour opérations >1 seconde (spinner discret)

## Core Screens and Views

1. **Tableau de bord (Home/Dashboard)** - Résumé financier en cartes visuelles (CA, dépenses, bénéfice), commandes récentes/en cours, accès rapide aux actions fréquentes

2. **Liste Clients** - Barre de recherche proéminente, vue tableau ou cartes avec nom, téléphone, nombre de commandes, bouton "Nouveau Client"

3. **Fiche Client (Détail)** - Informations client éditables, historique complet des commandes en dessous, bouton "Nouvelle commande pour ce client"

4. **Formulaire Client (Création/Édition)** - Formulaire vertical simple (Nom, Téléphone, Email, Adresse, Notes), boutons "Enregistrer" et "Annuler"

5. **Liste Commandes** - Filtres simples par année/statut en haut, vue tableau avec client, date, statut, montant total, badges colorés pour statuts

6. **Fiche Commande (Détail)** - Informations client + produits commandés, boutons de transition de statut visibles, montant total calculé automatiquement

7. **Formulaire Commande (Création/Édition)** - Sélection client (autocomplete), ajout de lignes produits (sélecteur produit + quantité), prix calculés automatiquement, zone notes libres

8. **Génération Étiquettes** - Formulaire simple: Type miel, Format pot, Date récolte, Quantité étiquettes, aperçu visuel d'une étiquette exemple, bouton "Générer PDF"

9. **Catalogue Produits & Tarifs** - Vue tableau produits avec prix par année, possibilité de modifier tarifs année future, prix année en cours mis en évidence

10. **Achats Fournitures** - Liste chronologique avec filtres année/catégorie, total dépenses affiché en haut, formulaire d'ajout rapide

## Accessibility

**Niveau ciblé:** WCAG AA (minimum)

**Mesures spécifiques:**
- Contraste minimum 4.5:1 pour tout le texte
- Taille de police: 16px minimum, 18px pour texte principal
- Tous les boutons et zones interactives: minimum 44x44px (facile à cliquer)
- Navigation entièrement possible au clavier (Tab, Enter, Esc)
- Labels explicites sur tous les champs de formulaire (pas de placeholders seuls)
- Messages d'erreur associés aux champs via aria-describedby
- Pas de dépendance uniquement sur la couleur (icônes + texte pour statuts)

## Branding

**Style visuel:**
- **Palette simple et naturelle:** Tons miel/ambre (#F4B942) pour éléments positifs, vert forêt (#2D5016) pour navigation, blanc cassé (#FAFAF8) pour fond
- **Typographie:** Sans-serif moderne et lisible (Roboto ou Inter), pas de polices décoratives
- **Iconographie:** Font Awesome pour cohérence, icônes simples reconnaissables
- **Ton:** Chaleureux, rassurant, jamais technique ou corporate
- **Logo:** Simple mention "🍯 HoneyAI" en haut de page

## Target Device and Platforms

**Plateforme principale:** Web Responsive (Desktop-First)

**Spécifications:**
- **Desktop (prioritaire):** Optimisé pour écrans 1280x720 minimum (résolution Windows standard)
- **Tablette (futur):** Responsive design permet consultation sur tablette en mode lecture
- **Mobile (hors scope MVP):** Non optimisé pour mobile dans MVP (usage bureau uniquement)

**Navigateurs supportés:** Chrome 90+, Microsoft Edge 90+, Firefox 88+

---
