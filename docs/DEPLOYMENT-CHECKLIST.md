# Checklist de Deploiement - HoneyAI v1.0.0

## Prerequis

- [ ] Windows 10 ou 11 (64 bits)
- [ ] Java 21 installe (Adoptium Temurin recommande : https://adoptium.net)
- [ ] Au moins 200 Mo d'espace disque libre
- [ ] Port 8080 disponible (non utilise par un autre programme)

## Etapes d'installation

### 1. Preparer le dossier

- [ ] Decompresser le fichier `HoneyAI-1.0.0.zip` dans un dossier au choix
  - Exemple : `C:\HoneyAI`
- [ ] Verifier que le dossier contient :
  - `honeyai-1.0.0.jar` (application)
  - `lancer-honeyai.bat` (lanceur)
  - `README-INSTALLATION.txt` (guide rapide)

### 2. Premier lancement

- [ ] Double-cliquer sur `lancer-honeyai.bat`
- [ ] Si Windows SmartScreen s'affiche, cliquer "Informations complementaires" puis "Executer quand meme"
- [ ] Attendre le message "HoneyAI est pret !"
- [ ] Le navigateur s'ouvre automatiquement sur `http://localhost:8080`

### 3. Verification du fonctionnement

- [ ] La page d'accueil (Tableau de bord) s'affiche
- [ ] Creer un client test : Clients > Nouveau Client
- [ ] Creer un produit test : Produits (verifie a la configuration initiale)
- [ ] Creer une commande test : Commandes > Nouvelle Commande
- [ ] Generer une etiquette test : Etiquettes > Generer
- [ ] Verifier le tableau de bord : les chiffres se mettent a jour
- [ ] Creer une sauvegarde : Sauvegarde > Sauvegarder maintenant
- [ ] Verifier que le fichier de sauvegarde est cree dans `./backups/`

### 4. Verification des fichiers crees

- [ ] Le dossier `./data/` contient `honeyai.db` (base de donnees)
- [ ] Le dossier `./logs/` contient `honeyai.log` (journaux)
- [ ] Le dossier `./backups/` contient les fichiers de sauvegarde

## Post-installation

- [ ] Supprimer les donnees de test (clients, commandes)
- [ ] Creer un raccourci vers `lancer-honeyai.bat` sur le Bureau si souhaite
- [ ] Planifier une sauvegarde reguliere (hebdomadaire recommande)

## Depannage rapide

| Probleme | Solution |
|----------|----------|
| "Java n'est pas installe" | Installer Java 21 depuis https://adoptium.net |
| Le navigateur ne s'ouvre pas | Ouvrir manuellement http://localhost:8080 |
| Port 8080 deja utilise | Fermer l'autre programme ou redemarrer le PC |
| Page blanche | Vider le cache du navigateur (Ctrl+Maj+Suppr) |

Pour plus de details, consulter `docs/DEPANNAGE.md`.
