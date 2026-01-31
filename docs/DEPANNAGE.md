# Guide de Depannage - HoneyAI v1.0.0

## L'application ne demarre pas

### "Java n'est pas installe"

**Cause** : Java 21 n'est pas installe ou n'est pas dans le PATH systeme.

**Solution** :
1. Allez sur https://adoptium.net
2. Telechargez "Temurin 21 (LTS)" pour Windows x64
3. Installez en cochant "Ajouter au PATH" pendant l'installation
4. Redemarrez l'ordinateur
5. Relancez `lancer-honeyai.bat`

### L'application se ferme tout de suite

**Cause possible** : le port 8080 est deja utilise par un autre programme.

**Solution** :
1. Ouvrez le fichier `./logs/honeyai.log` avec le Bloc-notes
2. Cherchez "Address already in use" ou "port 8080"
3. Si c'est le cas :
   - Redemarrez l'ordinateur (le plus simple)
   - Ou fermez l'autre programme qui utilise le port 8080

### Erreur "fichier introuvable" ou "acces refuse"

**Cause** : le fichier JAR n'est pas dans le meme dossier que le lanceur.

**Solution** :
1. Verifiez que `honeyai-1.0.0.jar` est dans le meme dossier que `lancer-honeyai.bat`
2. Ne deplacez pas le fichier JAR sans deplacer aussi le lanceur

---

## Le navigateur ne s'ouvre pas

**Solution** :
1. Attendez 10 secondes apres le message "Demarrage en cours"
2. Ouvrez votre navigateur manuellement
3. Tapez `http://localhost:8080` dans la barre d'adresse

---

## Page blanche ou erreur d'affichage

**Solution** :
1. Videz le cache du navigateur : appuyez sur **Ctrl + Maj + Suppr**
2. Selectionnez "Images et fichiers en cache"
3. Cliquez sur "Effacer les donnees"
4. Rechargez la page avec **Ctrl + F5**

---

## Les donnees ne se sauvent pas

### Le disque est plein

**Cause** : le disque dur n'a plus d'espace libre.

**Solution** :
1. Verifiez l'espace disque disponible
2. Liberez de l'espace en supprimant des fichiers inutiles
3. Les anciennes sauvegardes dans `./backups/` peuvent etre deplacees sur un support externe

### La base de donnees est corrompue

**Symptomes** : messages d'erreur a chaque operation, donnees qui disparaissent.

**Solution** :
1. Arretez l'application (fermez la fenetre de commande)
2. Restaurez une sauvegarde recente (voir ci-dessous)

---

## Problemes de sauvegarde

### La sauvegarde echoue

**Solutions** :
1. Verifiez que le dossier `./backups/` existe
2. Verifiez l'espace disque disponible
3. Consultez `./logs/honeyai.log` pour plus de details

### Restaurer une sauvegarde manuellement

Si la restauration via l'interface ne fonctionne pas :
1. Arretez l'application
2. Renommez le fichier actuel : `./data/honeyai.db` en `honeyai.db.ancien`
3. Copiez le fichier de sauvegarde souhaite depuis `./backups/` vers `./data/honeyai.db`
4. Relancez l'application

---

## Problemes d'etiquettes

### Le PDF ne se genere pas

**Solutions** :
1. Verifiez que vous avez selectionne un type de miel et un poids
2. Verifiez que le nombre d'etiquettes est superieur a 0
3. Consultez `./logs/honeyai.log` pour voir l'erreur detaillee

### Le PDF est vide ou mal formate

**Solution** :
1. Essayez d'ouvrir le PDF avec un autre lecteur (Adobe Acrobat Reader recommande)
2. Verifiez que la configuration des etiquettes est correcte dans les parametres

---

## Reinitialisation complete

> **ATTENTION** : cette operation supprime TOUTES vos donnees. Faites une sauvegarde avant.

Si rien d'autre ne fonctionne :
1. Arretez l'application
2. Supprimez le fichier `./data/honeyai.db`
3. Relancez l'application
4. Une nouvelle base de donnees vide sera creee automatiquement

---

## Consulter les journaux

Les journaux de l'application sont dans `./logs/honeyai.log`. Ce fichier contient les messages techniques qui peuvent aider a diagnostiquer un probleme.

Pour ouvrir le fichier :
1. Allez dans le dossier `logs` de l'application
2. Ouvrez `honeyai.log` avec le Bloc-notes

Les anciennes journaux sont archives automatiquement (rotation tous les 10 Mo, conservation 30 jours).

---

## Contact support

Si le probleme persiste : **04.71.03.12.43**
