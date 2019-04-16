#!/bin/bash


#
# Version: 1.0.2
#

# set the examples directories
declare -a dirs=(
    "${PWD##*/}"
    "examples/java"
    "examples/kotlin"
    "examples/annotation-processor/java"
    "examples/annotation-processor/kotlin")
java8=true

###

pwd=$PWD
cyan=$(tput setaf 6)
green=$(tput setaf 2)
red=$(tput setaf 1)
std=$(tput sgr0)

if [ "$java8" = true ]
then
    export JAVA_HOME="$JAVA8_HOME"
    export PATH="$(cygpath "$JAVA_HOME")/bin:$PATH"
fi

kVer=$(kobaltw --version | awk '{print substr($2, 1, length($2)-1)}')
updateWrappers() {
    curVer="$(gradle --version | awk '/Gradle/ {print $2}')"
    if [ -d gradle ]; then
        if [ "$curVer" != "$(./gradlew --version | awk '/Gradle/ {print $2}')" ]; then
            gradle -q --console=plain wrapper
            echo -e "        $(./gradlew --version | awk '/Gradle/') ${green}UPDATED${std}"
        else
            echo -e "        Gradle $curVer UP-TO-DATE"
        fi
    fi
    if [ -d kobalt ]; then
        kw=$(cut -d "=" -f 2 kobalt/wrapper/kobalt-wrapper.properties)
        if [ "$kw" = "$kVer" ]
        then
            echo -e "        Kobalt $kw UP-TO-DATE"
        else
            echo -e "kobalt.version=$kVer" > kobalt/wrapper/kobalt-wrapper.properties
            echo -e "        Kobalt $kVer ${green}UPDATED${std}"
        fi
    fi
}

echo -e "Updating wrappers..."

for d in "${!dirs[@]}"; do
    if [ "$d" -ne 0 ]; then
        cd "${dirs[d]}" || exit 1
    fi
    echo -e "    ${cyan}${dirs[d]}${std}"
    updateWrappers
    cd "$pwd"
done
