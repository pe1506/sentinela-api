#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper (versão enxuta)
# Na primeira execução baixa o Maven indicado em .mvn/wrapper/maven-wrapper.properties
# para ~/.m2/wrapper/dists e depois só repassa os argumentos:
#   ./mvnw spring-boot:run
# ----------------------------------------------------------------------------
set -e

BASE_DIR=$(cd "$(dirname "$0")" && pwd)
PROPS="$BASE_DIR/.mvn/wrapper/maven-wrapper.properties"

DIST_URL=$(sed -n 's/^distributionUrl=//p' "$PROPS" | tr -d '\r')
if [ -z "$DIST_URL" ]; then
  echo "[mvnw] distributionUrl não encontrado em $PROPS" >&2
  exit 1
fi

ZIP_NAME=$(basename "$DIST_URL")
DIST_NAME=${ZIP_NAME%-bin.zip}
MAVEN_USER_HOME=${MAVEN_USER_HOME:-"$HOME/.m2"}
DIST_DIR="$MAVEN_USER_HOME/wrapper/dists/$DIST_NAME"
MAVEN_HOME="$DIST_DIR/$DIST_NAME"

if [ ! -f "$MAVEN_HOME/bin/mvn" ]; then
  echo "[mvnw] Baixando $ZIP_NAME (só na primeira vez)..." >&2
  mkdir -p "$DIST_DIR"
  TMP_ZIP="$DIST_DIR/$ZIP_NAME"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL -o "$TMP_ZIP" "$DIST_URL"
  elif command -v wget >/dev/null 2>&1; then
    wget -q -O "$TMP_ZIP" "$DIST_URL"
  else
    echo "[mvnw] Instale curl ou wget para baixar o Maven." >&2
    exit 1
  fi
  if command -v unzip >/dev/null 2>&1; then
    unzip -q -o "$TMP_ZIP" -d "$DIST_DIR"
  else
    (cd "$DIST_DIR" && "${JAVA_HOME:+$JAVA_HOME/bin/}jar" xf "$TMP_ZIP")
  fi
  rm -f "$TMP_ZIP"
fi

chmod +x "$MAVEN_HOME/bin/mvn" 2>/dev/null || true
exec "$MAVEN_HOME/bin/mvn" "$@"
