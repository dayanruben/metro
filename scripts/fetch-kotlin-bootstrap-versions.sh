#!/bin/bash
#
# Fetches Kotlin compiler versions from the JetBrains bootstrap Maven repository
# and outputs the top N versions with their timestamps.
#
# Usage: fetch-kotlin-bootstrap-versions.sh [count]
#   count: Number of versions to output (default: 10)
#
# Output: Tab-separated "version\ttimestamp" per line, sorted newest first
#

set -euo pipefail

MAVEN_BASE="https://packages.jetbrains.team/maven/p/kt/bootstrap/org/jetbrains/kotlin/kotlin-compiler"
COUNT="${1:-10}"

# Get versions from maven-metadata.xml, get top N newest, then reverse for display (oldest first)
VERSIONS=$(curl -sSL "$MAVEN_BASE/maven-metadata.xml" \
  | grep -oE '<version>[^<]+</version>' \
  | sed 's/<version>//; s/<\/version>//' \
  | sort -V -r \
  | head -n "$COUNT" \
  | sort -V)

# For each version, fetch the timestamp from its directory page
for VERSION in $VERSIONS; do
  # Fetch the version directory and extract the timestamp from the .pom file line
  TIMESTAMP=$(curl -sSL "$MAVEN_BASE/$VERSION/" \
    | grep -oE "kotlin-compiler-[^\"]+\.pom</a>[^<]+" \
    | head -1 \
    | grep -oE '[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}' \
    || echo "unknown")
  echo -e "$VERSION\t$TIMESTAMP"
done
