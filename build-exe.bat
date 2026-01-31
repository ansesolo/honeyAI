@echo off
chcp 65001 >nul

echo.
echo   ===================================
echo     HoneyAI - Build EXE Wrapper
echo   ===================================
echo.

REM Verifier Maven
where mvn >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo   ERREUR : Maven n'est pas installe ou n'est pas dans le PATH.
    echo   Telechargez Maven depuis : https://maven.apache.org
    pause
    exit /b 1
)

REM Verifier launch4j
where launch4jc >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo   ERREUR : launch4j n'est pas installe ou n'est pas dans le PATH.
    echo   Telechargez launch4j depuis : https://launch4j.sourceforge.net
    echo   Ajoutez le repertoire de launch4j au PATH systeme.
    pause
    exit /b 1
)

REM Etape 1: Build du JAR
echo   [1/3] Construction du JAR avec Maven...
call mvn clean package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo   ERREUR : Le build Maven a echoue.
    pause
    exit /b 1
)
echo   JAR construit avec succes.

REM Etape 2: Copier le JAR a la racine
echo   [2/3] Preparation des fichiers...
copy /Y target\honeyai-1.0.0.jar honeyai-1.0.0.jar >nul

REM Etape 3: Generer l'EXE
echo   [3/3] Generation de HoneyAI.exe avec launch4j...
cd launcher
launch4jc honeyai-launch4j.xml
if %ERRORLEVEL% neq 0 (
    echo   ERREUR : La generation de l'EXE a echoue.
    cd ..
    pause
    exit /b 1
)
cd ..

REM Deplacer l'EXE a la racine
if exist launcher\HoneyAI.exe (
    move /Y launcher\HoneyAI.exe HoneyAI.exe >nul
)

echo.
echo   ===================================
echo     Build termine avec succes !
echo   ===================================
echo.
echo   Fichiers generes :
echo     - honeyai-1.0.0.jar
echo     - HoneyAI.exe
echo.
echo   Pour creer le package de distribution,
echo   consultez launcher\DIST-README.md
echo.
pause
