# Guide Utilisateur - HoneyAI v1.0.0

## Introduction

HoneyAI est une application de gestion pour votre exploitation apicole. Elle permet de gerer vos clients, commandes, produits, etiquettes de pots de miel, et achats depuis votre navigateur web.

L'application fonctionne en local sur votre ordinateur. Vos donnees restent chez vous.

---

## Demarrage

1. Double-cliquez sur `lancer-honeyai.bat`
2. Attendez quelques secondes que le programme demarre
3. Votre navigateur s'ouvre automatiquement

Pour arreter l'application, fermez la fenetre de commande.

---

## Tableau de bord

La page d'accueil affiche un resume de votre activite :

- Nombre total de clients
- Nombre de commandes en cours (par statut)
- Chiffre d'affaires de l'annee en cours
- Graphique des revenus par mois
- Total des achats / depenses

---

## Gestion des Clients

### Voir la liste des clients

Cliquez sur **Clients** dans le menu. La liste affiche tous vos clients avec leur nom, adresse, telephone et email.

### Ajouter un client

1. Cliquez sur **Nouveau Client**
2. Remplissez les champs obligatoires (marques d'un *)
3. Cliquez sur **Enregistrer**

### Modifier un client

1. Dans la liste, cliquez sur **Voir** a cote du client
2. Sur la fiche du client, cliquez sur **Modifier**
3. Modifiez les informations souhaitees
4. Cliquez sur **Enregistrer**

### Supprimer un client

1. Dans la liste, cliquez sur l'icone de suppression (corbeille)
2. Confirmez la suppression dans la fenetre de dialogue

> **Attention** : la suppression d'un client est irreversible.

---

## Gestion des Produits

Les produits representent vos differents types de miel avec leur contenance et tarif.

### Voir les produits

Cliquez sur **Produits** dans le menu.

### Ajouter un produit

1. Cliquez sur **Nouveau Produit**
2. Renseignez le type de miel, le poids et le prix
3. Cliquez sur **Enregistrer**

### Modifier / Supprimer un produit

Utilisez les boutons d'action dans la liste des produits.

---

## Gestion des Commandes

### Voir les commandes

Cliquez sur **Commandes** dans le menu. Vous pouvez filtrer par annee et par statut.

Les statuts possibles sont :
- **Commandee** : commande enregistree, pas encore recuperee
- **Recuperee** : le client a recupere sa commande
- **Payee** : la commande a ete payee

### Creer une commande

1. Cliquez sur **Nouvelle Commande**
2. Selectionnez le client et la date
3. Ajoutez des lignes de commande avec les produits et quantites
4. Cliquez sur **Enregistrer**

### Modifier le statut d'une commande

1. Cliquez sur **Voir** a cote de la commande
2. Utilisez les boutons de changement de statut sur la page de detail

---

## Etiquettes

### Generer des etiquettes

1. Cliquez sur **Etiquettes** > **Generer** dans le menu
2. Selectionnez le type de miel, le poids et le nombre d'etiquettes
3. Cliquez sur **Generer le PDF**
4. Un fichier PDF se telecharge avec les etiquettes prets a imprimer

Les etiquettes contiennent automatiquement :
- Le type de miel et le poids
- Votre nom et adresse
- Le numero SIRET
- La date limite d'utilisation (DLUO)
- Le numero de lot

### Historique des etiquettes

Cliquez sur **Etiquettes** > **Historique** pour voir toutes les generations precedentes et retelecharger les PDF.

---

## Achats

### Enregistrer un achat

1. Cliquez sur **Achats** dans le menu
2. Remplissez le formulaire rapide en haut de la page : date, designation, montant, categorie
3. Cliquez sur **Ajouter**

### Categories d'achats

Les achats sont classes par categorie (materiel, nourriture, traitement, etc.) pour faciliter le suivi des depenses.

### Modifier / Supprimer un achat

Utilisez les boutons d'action dans la liste des achats.

---

## Sauvegarde

### Sauvegarder manuellement

1. Cliquez sur **Sauvegarde** dans le menu
2. Cliquez sur **Sauvegarder maintenant**
3. La sauvegarde est creee dans le dossier `./backups/`

### Restaurer une sauvegarde

1. Dans la page Sauvegarde, trouvez la sauvegarde souhaitee
2. Cliquez sur **Restaurer**
3. Confirmez la restauration

> **Attention** : la restauration remplace toutes les donnees actuelles par celles de la sauvegarde.

### Sauvegardes automatiques

L'application cree automatiquement une sauvegarde au demarrage.

---

## Conseils d'utilisation

- **Sauvegardez regulierement** : faites une sauvegarde manuelle au moins une fois par semaine
- **Copiez les sauvegardes** : copiez le dossier `./backups/` sur une cle USB ou un disque externe
- **Un seul utilisateur a la fois** : l'application est concue pour un seul utilisateur
- **Ne deplacez pas la base de donnees** : le fichier `./data/honeyai.db` doit rester dans son dossier

---

## Support

En cas de probleme, consultez le guide de depannage dans `docs/DEPANNAGE.md`.

Contact support : **06.01.77.59.35**
