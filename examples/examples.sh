#!/bin/bash

if [ $# -eq 0 ]; then
    echo "Usage: $0 <arg ...>"
    exit 1
fi

# set the examples directories
declare -a examples=(
    "java"
    "kotlin"
    "annotation-processor/java"
    "annotation-processor/kotlin")

dir=$(dirname "$(readlink -f "$0")")
cyan=$(tput setaf 6)
normal=$(tput sgr0)

i=0
for ex in "${examples[@]}"; do
    if [ $i -ne 0 ]
    then
        read -p "Press [Enter] key to continue..."
        clear
    fi
    cd "$dir/$ex" || exit 1
    echo "> Project: ${cyan}${ex}${normal}"
    ./gradlew --console=plain --no-build-cache clean "$@" || exit 1
    (( i++ ))
done
