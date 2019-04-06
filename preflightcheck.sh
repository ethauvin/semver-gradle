#!/bin/bash

#
# Version: 1.0
#

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
doc=""
# e.g. empty or sonarqube
sonar="sonarqube"

# gradle default command line args
opts="--console=plain --no-build-cache --no-daemon"

###

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
  clear
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
    echo -e "> Project: ${cyan}${1}${std} [Gradle]"
    shift
    ./gradlew $opts clean $@ || exit 1
    cd "$pwd"
}

runKobalt() {
    cd "$1" || exit 1
    if [ -f kobalt/src/Build.kt ]
    then
        read -p "Run Kobalt Example? [y/n]: " choice
        case $choice in
            [Yy] )
                clear
                echo -e "> Project: ${cyan}$1${std} [Kobalt]"
                shift
                ./kobaltw clean $@ ;;
            * ) ;;
        esac
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
    pause
}

gradleCheck() {
    clear
    echo -e "${cyan}Checking Gradle build....${std}"
    gradle $opts clean check $doc $sonar || exit 1
    pause
}

examples() {
    clear
    echo -e "Running examples..."
    for ex in "${examples[@]}"
    do
        runGradle $ex
        runKobalt $ex
        read -p "Continue? [y/n]: " choice
        clear
        case $choice in
            [Yy] ) continue ;;
            * ) return ;;
        esac
    done
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
    examples
    validateCopyrights
}

showMenu() {
    echo "    1. Update Wrappers"
    echo "    2. Check Dependencies"
    echo "    3. Check Gradle Build"
    echo "    4. Run Examples"
    echo "    5. Validate Copyrights"
    echo "    6. Check Everything"
}

readOptions(){
	local choice
	read -p "Enter choice [1-6]: " choice
	case $choice in
		1) updateWrappers ;;
		2) checkDeps ;;
        3) gradleCheck ;;
        4) examples ;;
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
