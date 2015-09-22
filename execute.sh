#/bin/sh

ARGS="example build.pluto.example.GenerateAndCompile.factory build.pluto.example.GACInput"

mvn compile exec:java -Dexec.args="$ARGS"
