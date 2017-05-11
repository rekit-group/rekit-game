#!/bin/bash
PWD=$(pwd)
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
if [ "$PWD" != "$DIR" ]
then
        echo "This script does not support execution from other directories"
        exit 2
fi

cd project
./build.sh ./SingleJar.launch
cp game/target/build/linux/ReKiT.jar ..
cd ..
