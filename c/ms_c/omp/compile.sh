#!/bin/sh

H=/home/k0smik0/workspace/montecarlo_spheres/c/ms_c

gcc -o $H/omp/ms_omp -fopenmp -Wall $H/src/montecarlo_spheres/montecarlo_spheres_core.c $H/src/sequential_main.c -lgomp

gcc -o $H/omp/ms_no_omp -Wno-unknown-pragmas -Wall $H/src/montecarlo_spheres/montecarlo_spheres_core.c $H/src/sequential_main.c
