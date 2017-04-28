#!/bin/bash
if [ -z "$1" ]
then
   echo "Please specify the launch file"
   exit 1
fi

PWD=$(pwd)
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ "$PWD" != "$DIR" ]
then
	echo "This script does not support execution from other directories"
	exit 2
fi

mvn $(cat "$1" | grep "M2_GOALS" | tr "\"" "\n" | tail -2 - | head -1 -)
