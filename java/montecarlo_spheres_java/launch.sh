#!/bin/bash 

LIBS=$PWD/libs
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$LIBS/i386
CLASSPATH=$LIBS/j3d/j3d-core.jar:$LIBS/j3d/j3dutils.jar:$LIBS/j3d/vecmath.jar:$LIBS/stdlib.jar:$LIBS/commons-io-2.4.jar:$PWD/bin:.

cd bin
[ -L ds ] || ln -s ../ds
#$LD_LIBRARY_PATH 
java -Djava.library.path=../libs/i386 -cp $CLASSPATH net.iubris.aa.spheres.SVMain
