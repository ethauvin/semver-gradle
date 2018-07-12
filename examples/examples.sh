#!/bin/bash

if [ $# -eq 0 ]; then
    echo "Usage: $0 <arg> [...]"
    exit 1
fi

dir=$(dirname "$(readlink -f "$0")")
color=$(tput setaf 6)
normal=$(tput sgr0)

for ex in "java" "kotlin" "annotation-processor/java" "annotation-processor/kotlin"; do
    cd "$dir/$ex" || exit 1
    echo "> Project: ${color}${ex}${normal}"
    gradle clean "$@" --console=plain --no-build-cache || exit 1
    echo
done