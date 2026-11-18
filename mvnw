#!/bin/sh
BASEDIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
MAVEN_USER_HOME="$BASEDIR/.mvn/cache"
export MAVEN_USER_HOME
exec java -Dmaven.multiModuleProjectDirectory="$BASEDIR" -classpath "$BASEDIR/.mvn/wrapper/maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain "$@"
