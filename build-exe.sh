#!/bin/bash
set -e

echo ""
echo "  ==================================="
echo "    HoneyAI - Build EXE Wrapper"
echo "  ==================================="
echo ""

# Verifier Maven
if ! command -v mvn &> /dev/null; then
    echo "  ERREUR : Maven n'est pas installe ou n'est pas dans le PATH."
    echo "  Installez Maven : sudo apt install maven"
    exit 1
fi

# Verifier launch4j (version Linux JAR ou commande)
LAUNCH4J_CMD=""
if command -v launch4jc &> /dev/null; then
    LAUNCH4J_CMD="launch4jc"
elif command -v launch4j &> /dev/null; then
    LAUNCH4J_CMD="launch4j"
elif [ -n "$LAUNCH4J_HOME" ] && [ -f "$LAUNCH4J_HOME/launch4j.jar" ]; then
    LAUNCH4J_CMD="java -jar $LAUNCH4J_HOME/launch4j.jar"
else
    echo "  ERREUR : launch4j n'est pas installe."
    echo ""
    echo "  Options d'installation :"
    echo "    1. Telecharger depuis https://launch4j.sourceforge.net"
    echo "    2. Definir LAUNCH4J_HOME vers le dossier contenant launch4j.jar"
    echo "       export LAUNCH4J_HOME=/chemin/vers/launch4j"
    exit 1
fi

# Etape 1: Build du JAR
echo "  [1/3] Construction du JAR avec Maven..."
mvn clean package -DskipTests -q
echo "  JAR construit avec succes."

# Etape 2: Copier le JAR a la racine
echo "  [2/3] Preparation des fichiers..."
cp -f target/honeyai-1.0.0.jar honeyai-1.0.0.jar

# Etape 3: Generer l'EXE
echo "  [3/3] Generation de HoneyAI.exe avec launch4j..."
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
$LAUNCH4J_CMD "$SCRIPT_DIR/launcher/honeyai-launch4j.xml"

# Deplacer l'EXE a la racine si genere dans launcher/
if [ -f launcher/HoneyAI.exe ]; then
    mv -f launcher/HoneyAI.exe HoneyAI.exe
fi

echo ""
echo "  ==================================="
echo "    Build termine avec succes !"
echo "  ==================================="
echo ""
echo "  Fichiers generes :"
echo "    - honeyai-1.0.0.jar"
echo "    - HoneyAI.exe"
echo ""
echo "  Pour creer le package de distribution,"
echo "  consultez launcher/DIST-README.md"
echo ""
