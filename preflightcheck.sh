#!/bin/bash

# set source and test locations
src="src/main/kotlin/net/thauvin/erik/gradle/semver/"
test="src/main/kotlin/net/thauvin/erik/gradle/semver/"
# e.g: .java, .kt, etc.
ext=".kt"
java8=true
# e.g: <example directory> <gradle args...>
declare -a examples=(
    "examples/java incrementBuildMeta run"
    "examples/kotlin incrementBuildMeta run"
    "examples/annotation-processor/java incrementMinor run"
    "examples/annotation-processor/kotlin incrementMinor run")
# e.g: empty or javadoc, etc.
gradle_doc=""
# e.g. empty or sonarqube
gradle_sonar="sonarqube"
# gradle options for examples
gradle_opts="--console=plain --no-build-cache --no-daemon"
# maven arguments for examples
maven_args="compile exec:java"

#
# Version: 1.1.3
#

if [ "$java8" = true ]
then
    export JAVA_HOME="$JAVA8_HOME"
    export PATH="$(cygpath "$JAVA_HOME")/bin:$PATH"
fi

pwd=$PWD
red=$(tput setaf 1)
cyan=$(tput setaf 6)
std=$(tput sgr0)
date=$(date +%Y)

pause() {
  read -p "Press [Enter] key to continue..."
}

checkCopyright() {
    if [ "$(grep -c "$date" "$1")" -eq 0 ]
    then
        echo -e "   Invalid: ${red}$f${std}"
    else
        echo -e "   Checked: $1"
    fi
}

runGradle() {
    cd "$1" || exit 1
    clear
    reset
    echo -e "> Project: ${cyan}${1}${std} [Gradle]"
    shift
    ./gradlew $@ || exit 1
    pause
    cd "$pwd"
}

runKobalt() {
    cd "$1" || exit 1
    if [ -f kobalt/src/Build.kt ]
    then
        clear
        reset
        echo -e "> Project: ${cyan}${1}${std} [Kobalt]"
        shift
        ./kobaltw $@ || exit 1
        pause
    fi
    cd "$pwd"
}

runMaven() {
    cd "$1" || exit 1
    if [ -f pom.xml ]
    then
        clear
        reset
        echo -e "> Project: ${cyan}${1}${std} [Maven]"
        shift
        mvn $@ || exit 1
        pause
    fi
    cd "$pwd"
}

updateWrappers() {
    clear
    ./updatewrappers.sh
    pause
}

checkDeps() {
    clear
    echo -e "${cyan}Checking depencencies...${std}"
    gradle --console=plain dU || exit 1
    read -p "Check Examples depencencies? [y/n] " cont
    clear
    case $cont in
        [Nn] ) return ;;
        * ) for ex in "${!examples[@]}"
            do
                runGradle $(echo "${examples[ex]}" | cut -d " " -f 1) dU
                runKobalt $(echo "${examples[ex]}" | cut -d " " -f 1) checkVersions
                runMaven $(echo "${examples[ex]}" | cut -d " " -f 1) versions:display-dependency-updates 
                if [ "$ex" -eq "${#examples}" ]
                then
                    read -p "Continue? [y/n]: " cont
                    clear
                    case $cont in
                        * ) continue ;;
                        [Nn] ) return ;;
                    esac
                fi
            done ;;
    esac
}

gradleCheck() {
    clear
    echo -e "${cyan}Checking Gradle build....${std}"
    gradle $gradle_opts clean check $gradle_doc $gradle_sonar || exit 1
    pause
}

runExamples() {
    for ex in "${!examples[@]}"
    do
        runGradle ${examples[ex]} clean $gradle_opts
        runKobalt ${examples[ex]} clean
        runMaven $(echo "${examples[ex]}" | cut -d " " -f 1) clean $maven_args
    done
}

examplesMenu() {
    clear
    echo -e "${cyan}Examples${std}"
    for ex in "${!examples[@]}"
    do
        printf  '    %d. %s\n' $(($ex + 1)) $(echo "${examples[ex]}" | cut -d " " -f 1)
    done
    echo "    $((${#examples[@]} + 1)). Run All Examples"
    read -p "Enter choice [1-${#examples[@]}]: " choice
    clear
    case $choice in
        [0-9] ) if [ "$choice" -gt "${#examples[@]}" ]
                then
                    runExamples
                    examplesMenu
                else
                    runGradle ${examples[$(($choice - 1))]}
                    runKobalt ${examples[$(($choice - 1))]}
                    runMaven $(echo "${examples[$(($choice - 1))]}" | cut -d " " -f 1) $maven_args
                    examplesMenu
                fi ;;
        * ) return ;;
    esac
}

validateCopyrights() {
    clear
    echo -e "${cyan}Validating copyrights...${std}"
    for f in LICENSE.TXT ${src}/*${ext} ${test}/*${ext}
    do
        checkCopyright "$f"
    done
    pause
}

everything() {
    updateWrappers
    checkDeps
    gradleCheck
    runExamples
    validateCopyrights
}

showMenu() {
    clear
    echo "${cyan}Preflight Check${std}"
    echo "    1. Update Wrappers"
    echo "    2. Check Dependencies"
    echo "    3. Check Gradle Build"
    echo "    4. Run Examples"
    echo "    5. Validate Copyrights"
    echo "    6. Check Everything"
}

readOptions() {
	local choice
	read -p "Enter choice [1-6]: " choice
	case $choice in
		1) updateWrappers ;;
		2) checkDeps ;;
        3) gradleCheck ;;
        4) examplesMenu ;;
        5) validateCopyrights ;;
        6) everything ;;
		*) exit 0 ;;
	esac
}

while true
do
	showMenu
	readOptions
done
