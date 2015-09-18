#/bin/sh

ARGS="build-monto build.pluto.buildmonto.GenerateAndCompile.factory build.pluto.buildmonto.GACInput $@"

mvn compile exec:java -Dexec.args="$ARGS"
