#!/bin/bash

export LIBS_HOME=../../../../../../../../libs/

CLASSPATH=$LIBS_HOME/jomp1.0b.jar:$LIBS_HOME/commons-io-2.4.jar

java -cp $CLASSPATH jomp.compiler.Jomp $(basename $1 .jomp)
