#!/bin/bash

libs/jamp/bin/jampCuda -nowarn -d bin_jamp -cp libs/jamp/framework/jcudacompiler.jar:libs/jamp/framework/jcudaclassloader.jar:libs/commons-io-2.4.jar:bin_jamp `find src/volume -iname *java`

