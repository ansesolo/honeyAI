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

### Etapes

1. **Construire le JAR et l'EXE :**
   ```bash
   mvn clean package -Pexe -DskipTests
   ```
   Cela genere :
   - `target/honeyai-1.0.0.jar` (application)
   - `target/HoneyAI.exe` (lanceur Windows)

2. **Creer le dossier de distribution :**
   ```bash
   mkdir -p dist/HoneyAI
   cp target/HoneyAI.exe dist/HoneyAI/
   cp target/honeyai-1.0.0.jar dist/HoneyAI/
   cp lancer-honeyai.bat dist/HoneyAI/
   cp README-INSTALLATION.txt dist/HoneyAI/
   ```

3. **Creer le ZIP :**
   ```bash
   cd dist
   zip -r HoneyAI-1.0.0.zip HoneyAI/
   ```

4. **Livrer** le fichier `HoneyAI-1.0.0.zip` a l'utilisateur.

## Notes importantes

- L'EXE ne contient PAS le JAR (`dontWrapJar=true`). Le fichier `honeyai-1.0.0.jar` doit etre dans le meme repertoire que `HoneyAI.exe`.
- Java 21 doit etre installe sur la machine cible (Windows). L'EXE affiche un message d'erreur en francais si Java est absent.
- Les dossiers `data/` et `backups/` sont crees automatiquement au premier lancement.
- Le script `lancer-honeyai.bat` sert de methode de lancement alternative si l'EXE pose probleme.
- Le build se fait sous WSL (ou tout environnement Linux/Mac), la cible de deploiement est Windows 10/11.
- La configuration launch4j est dans le `pom.xml` (profil `exe`), pas dans un fichier XML separe.
