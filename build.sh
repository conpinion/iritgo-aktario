#!/bin/bash

set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

optClean=
optStage=2
optFormat=
optSite=
optSettings=
optMavenSettings=
optBatchMode=

shortOptions=c12h
longOptions=format,site,settings:,batchmode

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
			--settings) shift
				optSettings="--settings $1"
				optMavenSettings="-s $HOME/.m2/$1-settings.xml"
				;;
			--batchmode)
			        optBatchMode=1;;
			-h) printf "Usage: %s: [options]\n" $0
			    printf "Options:\n"
			    printf " -c                              Perform a clean build\n"
			    printf " -1                              Start from stage 1\n"
			    printf " -2                              Start from stage 2\n"
			    printf " --format                        Format the source code\n"
			    printf " --site                          Update the project web site\n"
			    printf " --settings                      Alternate Maven settings file\n"
			    printf "                                 (Leave out the path and the extension)\n"
			    printf " --batchmode                     Run maven in batch mode\n"
			    printf " -h                              Print this help\n"
			    exit 0
			    ;;
		esac
		shift
	done
}

processOptions $options

MVN="mvn"
if [ ! -z "$optMavenSettings" ]
then
	MVN="$MVN $optMavenSettings"
fi
if [ ! -z "$optBatchMode" ]
then
	MVN="$MVN -B"
fi

BUILD="./build.sh"
if [ ! -z "$optSettings" ]
then
	BUILD="$BUILD $optSettings"
fi
if [ ! -z "$optBatchMode" ]
then
	BUILD="$BUILD --batchmode"
fi

shift $(($OPTIND - 1))

if [ ! -z "$optFormat" ]
then
	$MVN java-formatter:format
	$MVN license:format
	exit 0
fi

if [ ! -z "$optSite" ]
then
	$MVN site:site
	$MVN site:deploy
	exit 0
fi

if [ "$optStage" -lt "2" ]
then
	if [ ! -z "$optClean" ]
	then
		cd ../iritgo-simplelife
		$MVN clean
		cd ../iritgo-jdicext
		$MVN clean
	fi
	cd ../iritgo-simplelife
	$MVN install
	if [ $? != 0 ]
	then
		exit $?
	fi
	cd ../iritgo-jdicext
	$MVN install
	if [ $? != 0 ]
	then
		exit $?
	fi
	cd ../iritgo-aktario
fi

if [ ! -z "$optClean" ]
then
	$MVN clean
fi

$MVN install
