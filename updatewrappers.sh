#!/bin/bash

#
# Version: 1.0.3
#

# set the examples directories
declare -a dirs=(
	"${PWD##*/}"
	"examples/java"
	"examples/kotlin"
	"examples/annotation-processor/java"
	"examples/annotation-processor/kotlin")

###

pwd=$PWD
cyan=$(tput setaf 6)
green=$(tput setaf 2)
red=$(tput setaf 1)
std=$(tput sgr0)

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
