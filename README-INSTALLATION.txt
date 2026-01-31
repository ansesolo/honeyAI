===================================================
          HoneyAI - Guide d'installation
===================================================


PREREQUIS
---------

  - Java 21 (ou plus recent)
    Telechargez-le ici : https://adoptium.net
    Choisissez "Windows x64" puis installez.


CONTENU DU DOSSIER
------------------

  honeyai-1.0.0.jar       L'application HoneyAI
  lancer-honeyai.bat      Script de lancement
  data/                   Base de donnees (creee automatiquement)
  backups/                Sauvegardes (creees automatiquement)
  logs/                   Journaux de l'application


COMMENT LANCER HONEYAI
----------------------

  1. Double-cliquez sur "lancer-honeyai.bat"
  2. Attendez quelques secondes
  3. Votre navigateur s'ouvre automatiquement sur HoneyAI
  4. Si le navigateur ne s'ouvre pas, allez a :
     http://localhost:8080


COMMENT ARRETER HONEYAI
-----------------------

  Fermez la fenetre du terminal (la petite fenetre noire)
  qui s'est ouverte au lancement.


VOS DONNEES
-----------

  Toutes vos donnees sont stockees dans le dossier "data/".
  Le fichier "data/honeyai.db" contient votre base de donnees.

  Pour sauvegarder manuellement :
  - Copiez le dossier "data/" sur une cle USB ou un autre emplacement

  Les sauvegardes automatiques sont dans le dossier "backups/".


EN CAS DE PROBLEME
------------------

  "Java n'est pas installe" :
    -> Installez Java 21 depuis https://adoptium.net

  Le navigateur affiche "page introuvable" :
    -> Attendez 10 secondes et rechargez la page
    -> Verifiez que le terminal est toujours ouvert

  L'application ne demarre pas :
    -> Verifiez que le fichier "honeyai-1.0.0.jar" est bien
       dans le meme dossier que "lancer-honeyai.bat"


===================================================
  HoneyAI 2026 - Application de gestion apicole
===================================================
