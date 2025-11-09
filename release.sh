#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: $0 <api|verifier>"
    exit 1
fi

MODULE=$1

if [ "$MODULE" != "api" ] && [ "$MODULE" != "verifier" ]; then
    echo "Invalid module: $MODULE"
    echo "Usage: $0 <api|verifier>"
    exit 1
fi

MODULE_DIR="liquidjava-$MODULE"

cd "$MODULE_DIR"
mvn -Dgpg.skip=false -Dmaven.deploy.skip=false clean deploy
cd ..
