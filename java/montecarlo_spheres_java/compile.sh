#!/bin/bash
 
LIBS=$PWD/libs
CLASSPATH=$LIBS/j3d/j3d-core.jar:$LIBS/j3d/j3dutils.jar:$LIBS/j3d/vecmath.jar:$LIBS/stdlib.jar:$LIBS/commons-io-2.4.jar:$PWD/bin:.

sources="`find src/main -iname *.java` `find src/render -iname *.java | grep std`"
javac -d ./bin -cp ${CLASSPATH} ${sources}
