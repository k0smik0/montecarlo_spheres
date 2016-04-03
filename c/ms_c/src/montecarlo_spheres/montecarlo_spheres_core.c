#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>
#include <time.h>    // time()
#include <ctype.h>
#include <unistd.h>

//#ifdef OMP_H
#include <omp.h>
//#endif

#include "montecarlo_spheres_core.h"

/*#define UNINIT -1E7f
#define size(a) (sizeof(a) / sizeof(*a))

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

int contains2(Sphere* sphere, Point* point) {
	double cx = sphere->x;
	double cy = sphere->y;
	double cz = sphere->z;
	double radius = sphere->radius;
	double px = point->x;
	double py = point->y;
	double pz = point->z;

	double sxEquationMember = (px-cx)*(px-cx) + (py-cy)*(py-cy) + (pz-cz)*(pz - cz);
	double dxEquationMember = radius*radius;

	if ( sxEquationMember <= dxEquationMember )
		return 1;
	return 0;
}

typedef struct DimensionsBounds {
	double min;
	double max;
} DimensionsBounds;

typedef struct BoundingBox {

	DimensionsBounds xDimensionsBounds;
	DimensionsBounds yDimensionsBounds;
	DimensionsBounds zDimensionsBounds;

	Sphere xMinSphere;
	Sphere xMaxSphere;
	Sphere yMinSphere;
	Sphere yMaxSphere;
	Sphere zMinSphere;
	Sphere zMaxSphere;

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
	double howRandomPoints;
	char **nodes;
} Arguments;

struct FileParsingResult* readFile(char s[]);

//double calculateHigherBound(double coordinate, double radius, double actualMax);
//double calculateLowerBound(double coordinate, double radius, double actualMin);

struct BoundingBox *initBoundingBox(int spheresNumber, Sphere spheres[]);

struct Point **initRandomPoints(int randomPointsUsed, BoundingBox * boundingBox);

//struct Point *buildRandomPoint(double randomX, double randomY, double randomZ, BoundingBox * boundingBox);

int checkPointsSequential(int spheresNumber, Sphere spheres[], int randomPointsUsed, Point **randomPoints);

double calculateVolume(int randomPointsFounded, int randomPointsUsed, double boundingBoxVolume);

struct Arguments *checkArgs(int argc, char *argv[]);*/

/*int main2(int argc, char *argv[]) {

	// fake
	int rank=time(0);

	Arguments *arguments = checkArgs(argc,argv);
	if (arguments == NULL)
		return -1;

	char *filename = arguments->filename;
	double randomPointsUsed = arguments->howRandomPoints;
//	char **nodes = arguments->nodes;
	free(arguments);

	printf("Using data from %s, with %f points\n",filename,randomPointsUsed);

	struct timeval stop, start;
	gettimeofday(&start, NULL);
	FileParsingResult * fpr = readFile(filename);
	if (fpr != NULL) {
		int spheresNumber =  fpr->spheresNumber;
		Sphere *spheres = fpr->spheres;

		printf("Founded %d spheres\n",spheresNumber);

		BoundingBox *boundingBox = initBoundingBox(spheresNumber,spheres);
		printf("BoundingBox volume: %f\n",boundingBox->volume);

		Point **randomPoints = initRandomPoints(randomPointsUsed, boundingBox, rank);

		int randomPointsFounded = checkPoints(spheresNumber,spheres,randomPointsUsed,randomPoints);
		double volume = calculateVolume(randomPointsFounded,randomPointsUsed,boundingBox->volume);
		gettimeofday(&stop, NULL);
		long time = stop.tv_usec-start.tv_usec;
		printf("Volume: %f, in %f sec\n",volume,time/1E6f);
	}
	free(fpr);
	return 0;
}*/

void showUsage() {
	printf("Usage: montecarlo_spheres <dataset_file_path> <how_many_random_point>\n"
			"(if third option is not provided, computation will be launched only on localhost)\n");
}

struct Arguments* checkArgs(int argc, char *argv[]) {
	if (argc<3) {
		showUsage();
		return NULL;
	}
	Arguments *arguments = malloc(sizeof(Arguments));
	if (argc==3) {
		arguments->filename=argv[1];
		arguments->howRandomPoints= (int) (atof(argv[2]));
		arguments->verbose=0;
	}
	if (argc==4) {
		char *argv1 = argv[1];
		if (strcmp(argv1,"-v")!=0) {
			showUsage();
			return NULL;
		}
		arguments->verbose=1;
		arguments->filename=argv[2];
		arguments->howRandomPoints= (int) (atof(argv[3]));
	}
	return arguments;
}

struct FileParsingResult* readFile(char fileName[]) {
	FileParsingResult *fpr;
	fpr = malloc( sizeof(FileParsingResult) );
	FILE* fp = NULL;
	fp = fopen (fileName, "rt");
	if (!fp) {
		char msg[256];
		snprintf(msg, sizeof msg, "%s%s", "File open error!\n", fileName);
		perror (msg);
		return NULL;
	}

	int nlines;
	fscanf(fp,"%d", &nlines);

	int s=0;
	Sphere *spheres = (Sphere *)malloc( nlines*sizeof(Sphere) );

	while (!feof(fp)) {

		char coordinates_as_string[4][10];
		fscanf(fp,"%s %s %s %s\n",
				coordinates_as_string[0],
				coordinates_as_string[1],
				coordinates_as_string[2],
				coordinates_as_string[3] );

		Sphere sphere;
		sphere.x = atof(coordinates_as_string[0]);
		sphere.y = atof(coordinates_as_string[1]);
		sphere.z = atof(coordinates_as_string[2]);
		sphere.radius = atof(coordinates_as_string[3]);

		spheres[s] = sphere;
		s++;
	}

	(*fpr).spheresNumber = nlines;
	(*fpr).spheres = spheres;

	fclose (fp);
	return fpr;
}

double calculateHigherBound(double coordinate, double radius, double actualMax) {
	double currentMax = coordinate+radius;
	if (actualMax==UNINIT)
		return currentMax;
	if (currentMax>actualMax)
		return currentMax;
	return actualMax;
}
double calculateLowerBound(double coordinate, double radius, double actualMin) {
	double currentMin = coordinate-radius;
	if (actualMin==UNINIT)
		return currentMin;
	// currentMin and actualMin are in different quadrants
	if (currentMin<0 && actualMin>0) {
		return currentMin;
	}
	// currentMin and actualMin are in different quadrants
	if (currentMin>0 && actualMin<0) {
		return actualMin;
	}
	// currentMin and actualMin are in same quadrant
	if (currentMin<actualMin) {
		return currentMin;
	}
	// currentMin and actualMin are in different quadrants
	// and actualMin < currentMin
	return actualMin;
}
struct BoundingBox *initBoundingBox(int spheresNumber, Sphere * spheres) {

	double boundingBoxXMin = UNINIT;
//	Sphere xMinSphere;
	double boundingBoxXMax = UNINIT;
//	Sphere xMaxSphere;

	double boundingBoxZMin = UNINIT;
//	Sphere zMinSphere;
	double boundingBoxZMax = UNINIT;
//	Sphere zMaxSphere;

	double boundingBoxYMin = UNINIT;
//	Sphere yMinSphere;
	double boundingBoxYMax = UNINIT;
//	Sphere yMaxSphere;

	int i=0;
	for (i=0;i<spheresNumber;i++) {

		Sphere sphere = spheres[i];
		double radius = sphere.radius;

		double cx = sphere.x;
		double computedXLB = 	calculateLowerBound(cx,radius,boundingBoxXMin);
		if (boundingBoxXMin==UNINIT || computedXLB != boundingBoxXMin) {
			boundingBoxXMin = computedXLB;
//			xMinSphere = sphere;
		}

		double computedXHB = calculateHigherBound(cx,radius,boundingBoxXMax);
		if (boundingBoxXMax==UNINIT || computedXHB != boundingBoxXMax) {
			boundingBoxXMax = computedXHB;
//			xMaxSphere = sphere;
		}

		double cz = sphere.z;
		double computedZLB = calculateLowerBound(cz,radius,boundingBoxZMin);
		if (boundingBoxZMin==UNINIT || computedZLB != boundingBoxZMin) {
			boundingBoxZMin = computedZLB;
//			zMinSphere = sphere;
		}
		double computedZHB = calculateHigherBound(cz,radius,boundingBoxZMax);
		if (boundingBoxZMax==UNINIT || computedZHB != boundingBoxZMax) {
			boundingBoxZMax = computedZHB;
//			zMaxSphere = sphere;
		}

		double cy = sphere.y;
		double computedYLB = calculateLowerBound(cy,radius,boundingBoxYMin);
		if (boundingBoxYMin==UNINIT ||computedYLB != boundingBoxYMin) {
			boundingBoxYMin = computedYLB;
//			yMinSphere = sphere;
		}
		double computedYHB = calculateHigherBound(cy,radius,boundingBoxYMax);
		if (boundingBoxYMax==UNINIT || computedYHB != boundingBoxYMax) {
			boundingBoxYMax = computedYHB;
//			yMaxSphere = sphere;
		}
	}


	DimensionsBounds xDimensionsBounds;
	xDimensionsBounds.min = boundingBoxXMin;
	xDimensionsBounds.max = boundingBoxXMax;

	DimensionsBounds yDimensionsBounds;
	yDimensionsBounds.min = boundingBoxYMin;
	yDimensionsBounds.max = boundingBoxYMax;

	DimensionsBounds zDimensionsBounds;
	zDimensionsBounds.min = boundingBoxZMin;
	zDimensionsBounds.max = boundingBoxZMax;


	BoundingBox *boundingBox = (BoundingBox *)malloc(sizeof(BoundingBox));
	boundingBox->xDimensionsBounds = xDimensionsBounds;
	boundingBox->yDimensionsBounds = yDimensionsBounds;
	boundingBox->zDimensionsBounds = zDimensionsBounds;
	/*
	boundingBox->xMinSphere = xMinSphere;
	boundingBox->xMaxSphere = xMaxSphere;
	boundingBox->yMinSphere = yMinSphere;
	boundingBox->yMaxSphere = yMaxSphere;
	boundingBox->zMinSphere = zMinSphere;
	boundingBox->zMaxSphere = zMaxSphere;
*/
	double width = boundingBoxXMax-boundingBoxXMin;
	double depth = boundingBoxYMax-boundingBoxYMin;
	double height = boundingBoxZMax-boundingBoxZMin;

	boundingBox->width = width;
	boundingBox->depth = depth;
	boundingBox->height = height;

	boundingBox->volume = width * height * depth;

	return boundingBox;
}

unsigned int generateSeed() {
	srand( time(0) );
	uint seed = rand();
	return seed;
}
struct Point *buildRandomPoint(double randomX, double randomY, double randomZ, BoundingBox * boundingBox) {
	double xr = boundingBox->xDimensionsBounds.min+randomX*boundingBox->width;
	double yr = boundingBox->yDimensionsBounds.min+randomY*boundingBox->depth;
	double zr = boundingBox->zDimensionsBounds.min+randomZ*boundingBox->height;

//	printf("%f %f %f\n",xr,yr,zr);
	Point *point = malloc(sizeof(Point));
	point->x = xr;
	point->y = yr;
	point->z = zr;

	return point;
}
struct Point **initRandomPoints(int randomPointsUsed, BoundingBox * boundingBox, unsigned int seed) {
	int r=0;
//	printf("%d",randomPointsUsed);
	Point **randomPoints = malloc( sizeof(Point *)*randomPointsUsed );
//	Point randomPoints[randomPointsUsed];
//	srand(rank);
//	uint seed = rand();
//	printf("size %d\n",sizeof(randomPoints)/sizeof(Point));

	for (r=0;r<randomPointsUsed;r++) {
		Point *rp = buildRandomPoint( (float)rand_r(&seed)/RAND_MAX,
				(float)rand_r(&seed)/RAND_MAX,
				(float)rand_r(&seed)/RAND_MAX,
				boundingBox);
//		printf("%d: %f %f %f\n",r,rp.x,rp.y,rp.z);
		randomPoints[r]=rp;
	}

	return randomPoints;
}


/*
void runSequential(char* fileName, double randomPoints) {
	SpheresVolume sv = new SpheresVolumeSequential( fileName, randomPoints );
	try {
		sv.parseInFile();
	} catch (IOException e) {
		e.printStackTrace();
	}
	long start = System.currentTimeMillis();
	sv.init();
	sv.calculateVolume();
	printf("%d", sv.getResult() );
	long end = System.currentTimeMillis();
	long time = end-start;
	printf("in: %f sec",time/1000.0f);
}
*/
int isContained(Sphere* sphere, Point* point) {
	double cx = sphere->x;
	double cy = sphere->y;
	double cz = sphere->z;
	double radius = sphere->radius;
	double px = point->x;
	double py = point->y;
	double pz = point->z;

	double sxEquationMember = (px-cx)*(px-cx) + (py-cy)*(py-cy) + (pz-cz)*(pz - cz);
	double dxEquationMember = radius*radius;

	if ( sxEquationMember <= dxEquationMember )
		return 1;
	return 0;
}
int checkPoints(int spheresNumber, Sphere spheres[], int randomPointsUsed, Point **randomPoints, int verbose) {
	int randomPointsFounded = 0, i,j;
	if (verbose)
		printf("inside checkPoints: using %d points\n",randomPointsUsed);
/*
	typedef struct {
	    struct timespec tstart;
	    struct timespec tstop;
	} Gettime_Timer_T;

	Gettime_Timer_T gettime_timer_t;
*/

#ifdef _OPENMP
//   printf("using %d threads\n", omp_get_max_threads() );
   double time_start = omp_get_wtime();
#else
   /*typedef struct {
	   clock_t  tstart;
	   clock_t  tstop;
   } clock_timer_t;*/

   //clock_timer_t c = clock();
   double time_start = clock();
#endif

	//clock_gettime(CLOCK_MONOTONIC, &(gettime_timer_t.tstart) );

	#pragma omp parallel
    { // begin pragma omp
		#pragma omp for
	   	for (i=0;i<randomPointsUsed;i++) {
	   		Point *rp = randomPoints[i];
			for (j=0;j<spheresNumber;j++) {
				Sphere s = spheres[j];
				if (isContained(&s,rp)) {
					randomPointsFounded++;
					break;
				}
			}
	   	}
    } // end pragma omp

//   clock_gettime(CLOCK_MONOTONIC, &(gettime_timer_t.tstop));
//   double elapsed = gettime_timer_t.tstop.tv_nsec - gettime_timer_t.tstart.tv_nsec;

#ifdef _OPENMP
   double time_stop = omp_get_wtime();
   double elapsed = time_stop - time_start;
   printf("using omp (with %d threads and %d cpus available) takes %f sec\n",
		   omp_get_max_threads(),
		   omp_get_num_procs(),
		   (elapsed) );
#else
   double time_stop = clock();
   double elapsed = time_stop - time_start;
   printf("not using omp takes %f sec\n", (elapsed/1e6));
#endif

   printf("founded %d points\n",randomPointsFounded);
   return randomPointsFounded;
}

double calculateVolume(int randomPointsFounded, int randomPointsUsed, double boundingBoxVolume) {
	double volumeTotalMontecarlo = ( (0.0+randomPointsFounded) / (0.0+randomPointsUsed) )*boundingBoxVolume;
	return volumeTotalMontecarlo;
}
