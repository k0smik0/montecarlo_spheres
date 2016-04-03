#!/bin/bash

MY_CLASSPATH=libs/jamp/framework/jcudacompiler.jar:libs/jamp/framework/jcudaclassloader.jar:libs/commons-io-2.4.jar:bin_jamp
/data/fucina/develop/usr/apps/cuda/jcuda/bin/jcuda_java $MY_CLASSPATH net.iubris.aa.volume.SpheresVolume "ds/sfere1.in" 1e7

