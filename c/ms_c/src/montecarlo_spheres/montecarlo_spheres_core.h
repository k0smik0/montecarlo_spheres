/*
 * sequential.h
 *
 *  Created on: 24/gen/2014
 *      Author: k0smik0
 */

#ifndef SEQUENTIAL_H_
#define SEQUENTIAL_H_

#ifndef RAND_MAX
#define RAND_MAX 32767
#endif

#define UNINIT -1E7f
//#define size(a) (sizeof(a) / sizeof(*a))

typedef struct Point {
	double x;
	double y;
	double z;
} Point;

typedef struct Sphere {
	double x;
	double y;
	double z;
	double radius;
} Sphere;



typedef struct DimensionsBounds {
	double min;
	double max;
} DimensionsBounds;

typedef struct BoundingBox {

	DimensionsBounds xDimensionsBounds;
	DimensionsBounds yDimensionsBounds;
	DimensionsBounds zDimensionsBounds;
	/*
	Sphere xMinSphere;
	Sphere xMaxSphere;
	Sphere yMinSphere;
	Sphere yMaxSphere;
	Sphere zMinSphere;
	Sphere zMaxSphere;
	*/
	double width; // x
	double height; // z
	double depth; // y
	double volume;
} BoundingBox;

typedef struct FileParsingResult {
	int spheresNumber;
	Sphere *spheres;
} FileParsingResult ;

typedef struct Arguments {
	char *filename;
	int howRandomPoints;
	char **nodes;
	int verbose;
} Arguments;

struct Arguments *checkArgs(int argc, char *argv[]);
struct FileParsingResult* readFile(char s[]);

double calculateHigherBound(double coordinate, double radius, double actualMax);
double calculateLowerBound(double coordinate, double radius, double actualMin);
struct BoundingBox *initBoundingBox(int spheresNumber, Sphere spheres[]);

unsigned int generateSeed();
struct Point **initRandomPoints(int randomPointsUsed, BoundingBox * boundingBox, unsigned int seed);
struct Point *buildRandomPoint(double randomX, double randomY, double randomZ, BoundingBox * boundingBox);

int checkPoints(int spheresNumber, Sphere spheres[], int randomPointsUsed, Point **randomPoints, int verbose);

double calculateVolume(int randomPointsFounded, int randomPointsUsed, double boundingBoxVolume);


#endif /* SEQUENTIAL_H_ */
