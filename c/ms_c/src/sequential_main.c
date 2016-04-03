/*
 * sequential_main.c
 *
 *  Created on: 24/gen/2014
 *      Author: k0smik0
 */

#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <time.h>

#include "montecarlo_spheres/montecarlo_spheres_core.h"

int main(int argc, char *argv[]) {

	// fake
	int rank=time(0);

	Arguments *arguments = checkArgs(argc,argv);
	if (arguments == NULL)
		return -1;

	char *filename = arguments->filename;
	double randomPointsUsed = arguments->howRandomPoints;
	int verbose = arguments->verbose;
//	char **nodes = arguments->nodes;
	free(arguments);

	printf("Using data from %s, with %f points\n", filename, randomPointsUsed);

	struct timeval stop, start;
	gettimeofday(&start, NULL);
	FileParsingResult * fpr = readFile(filename);
	if (fpr == NULL)
		abort();

	int spheresNumber =  fpr->spheresNumber;
	Sphere *spheres = fpr->spheres;
	free(fpr);
	printf("Founded %d spheres\n",spheresNumber);

	BoundingBox *boundingBox = initBoundingBox(spheresNumber,spheres);
	printf("BoundingBox volume: %f\n",boundingBox->volume);

	Point **randomPoints = initRandomPoints(randomPointsUsed, boundingBox, rank);

	int randomPointsFounded = checkPoints(spheresNumber,spheres,randomPointsUsed,randomPoints,verbose);
	double volume = calculateVolume(randomPointsFounded,randomPointsUsed,boundingBox->volume);
	gettimeofday(&stop, NULL);
	long time = stop.tv_usec - start.tv_usec;
	printf("Volume: %f, in %f sec\n",volume,time/1e6);


	return 0;
}
