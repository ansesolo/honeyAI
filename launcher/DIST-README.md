# HoneyAI - Distribution Package

## Structure du package de distribution

```
HoneyAI/
├── HoneyAI.exe                 Lanceur Windows (.exe via launch4j)
├── honeyai-1.0.0.jar           Application Spring Boot
├── lancer-honeyai.bat          Script de lancement alternatif (fallback)
├── README-INSTALLATION.txt     Instructions pour l'utilisateur
├── data/                       Base de donnees (creee au premier lancement)
│   └── honeyai.db
└── backups/                    Sauvegardes automatiques (creees au runtime)
```

## Comment creer le package de distribution

### Prerequis sur la machine de build

- Java 21 (JDK)
- Maven 3.8+
- launch4j 3.50+ (dans le PATH)

### Etapes

1. **Construire le JAR et l'EXE :**
   ```bat
   build-exe.bat
   ```

2. **Creer le dossier de distribution :**
   ```bat
   mkdir dist\HoneyAI
   copy HoneyAI.exe dist\HoneyAI\
   copy honeyai-1.0.0.jar dist\HoneyAI\
   copy lancer-honeyai.bat dist\HoneyAI\
   copy README-INSTALLATION.txt dist\HoneyAI\
   ```

3. **Creer le ZIP :**
   ```bat
   cd dist
   powershell Compress-Archive -Path HoneyAI -DestinationPath HoneyAI-1.0.0.zip
   ```

4. **Livrer** le fichier `HoneyAI-1.0.0.zip` a l'utilisateur.

## Notes importantes

- L'EXE ne contient PAS le JAR (`dontWrapJar=true`). Le fichier `honeyai-1.0.0.jar` doit etre dans le meme repertoire que `HoneyAI.exe`.
- Java 21 doit etre installe sur la machine cible. L'EXE affiche un message d'erreur en francais si Java est absent.
- Les dossiers `data/` et `backups/` sont crees automatiquement au premier lancement.
- Le script `lancer-honeyai.bat` sert de methode de lancement alternative si l'EXE pose probleme.
