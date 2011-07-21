#!/bin/bash

optClean=
optStage=2
optFormat=
optSite=

shortOptions=c12h
longOptions=format,site

options=("$(getopt -u -o $shortOptions --long $longOptions -- $@)")

function processOptions
{
	while [ $# -gt 0 ]
	do
		case $1 in
			-c) optClean=1;;
			-1) optStage=1;;
			-2) optStage=2;;
			--format) optFormat=1;;
			--site) optSite=1;;
			-h) printf "Usage: %s: [options]\n" $0
			    printf "Options:\n"
			    printf " -c       Perform a clean build\n"
			    printf " -1       Start from stage 1\n"
			    printf " -2       Start from stage 2\n"
			    printf " --format Format the source code\n"
			    printf " --site   Update the project web site\n"
			    printf " -h       Print this help\n"
			    exit 0
			    ;;
		esac
		shift
	done
}

processOptions $options

shift $(($OPTIND - 1))

if [ ! -z "$optFormat" ]
then
	mvn java-formatter:format
	mvn license:format
	exit 0
fi

if [ ! -z "$optSite" ]
then
	mvn site:site
	mvn site:deploy
	exit 0
fi

if [ "$optStage" -lt "2" ]
then
	if [ ! -z "$optClean" ]
	then
		cd ../iritgo-simplelife
		mvn clean
		cd ../iritgo-jdicext
		mvn clean
	fi
	cd ../iritgo-simplelife
	mvn install
	if [ $? != 0 ]
	then
		exit $?
	fi
	cd ../iritgo-jdicext
	mvn install
	if [ $? != 0 ]
	then
		exit $?
	fi
	cd ../iritgo-aktario
fi

if [ ! -z "$optClean" ]
then
	mvn clean
fi

mvn install
