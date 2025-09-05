#!/bin/bash

# Simple Gradle wrapper script
APP_HOME="`pwd -P`"
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

exec java -jar "$CLASSPATH" "$@"
