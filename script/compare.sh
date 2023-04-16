#!/bin/bash

if [[ ! -f "flags" ]]; then
  echo "No flags file. Get one :)"
  exit 1
fi

rm -f "chordata.jar"
cp "../target/chordata-1.0-SNAPSHOT-jar-with-dependencies.jar" "chordata.jar" || \
  { echo "Run 'mvn package' first" && exit 2; }

# Removing the built jar file to enforce new builds.
rm "../target/chordata-1.0-SNAPSHOT-jar-with-dependencies.jar"

rm *.bin
rm *.json

set -e
echo "Running tests"

java -jar chordata.jar -b flags 1.json
java -jar chordata.jar -j 1.json 1.bin
java -jar chordata.jar -b 1.bin 2.json

echo "Comparing original flags and serialized"
{ cmp -b flags 1.bin && echo "OK"; } || echo "different :("

echo "Comparing JSONs"
{ cmp 1.json 2.json && echo "OK"; } || echo "different :("
