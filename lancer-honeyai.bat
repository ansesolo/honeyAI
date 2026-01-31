@echo off
chcp 65001 >nul

echo.
echo   ===================================
echo        HoneyAI - Demarrage
echo   ===================================
echo.

REM Verifier que Java est installe
where javaw >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo   ============================================
    echo   ERREUR : Java n'est pas installe sur cet
    echo   ordinateur.
    echo.
    echo   Veuillez installer Java 21 depuis :
    echo   https://adoptium.net
    echo   ============================================
    pause
    exit /b 1
)

REM Demarrer l'application (sans fenetre console)
start "" javaw -jar honeyai-1.0.0.jar

REM Attendre le demarrage du serveur
echo   Demarrage de HoneyAI en cours...
timeout /t 4 /nobreak >nul

REM Ouvrir le navigateur
start http://localhost:8080

echo   HoneyAI est pret !
echo   Le navigateur va s'ouvrir automatiquement.
echo.
echo   Pour arreter : fermez cette fenetre.
echo.
