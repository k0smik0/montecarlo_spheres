/*
 * 	montercarlo_spheres_c_mpi.c
 *  Copyleft 2014, Massimiliano Leone - maximilianus@gmail.com
 *  licensed under GPL v3.0
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdarg.h>
#include <ctype.h>
#include <unistd.h>
#include <time.h>
#include <sys/utsname.h>
#include <mpi/mpi.h>

#define TAG_MPI "MPI"

#ifdef _OPENMP
#include <omp.h>
#define TAG_OMP "OMP"
void tag_omp_print(const char *format, ...) {
	va_list args;
	printf("%s: ", TAG_OMP);
	va_start( args, format );
	vprintf( format, args );
	va_end( args );
}
#endif

//#include "ms/montecarlo_spheres_core.h"
/* core header begin */
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
//	int threads;
} Arguments;

typedef struct Timing {
	double start;
	double stop;
	double elapsed;
} Timing;

typedef struct Timings {
	struct Timing timingInitBoundingBox;
	struct Timing timingCheckPoints;
	struct Timing timingRankZeroGenSeed;
} Timings;

struct Arguments *checkArgs(int argc, char *argv[]);
struct FileParsingResult* readFile(char s[]);

double calculateHigherBound(double coordinate, double radius, double actualMax);
double calculateLowerBound(double coordinate, double radius, double actualMin);
struct BoundingBox *initBoundingBox(int spheresNumber, Sphere spheres[]);

unsigned int generateSeed();
//struct Point **initRandomPoints(int randomPointsUsed, BoundingBox * boundingBox, unsigned int seed);
struct Point *buildRandomPoint(double randomX, double randomY, double randomZ, BoundingBox * boundingBox);

int checkPoints(int spheresNumber, Sphere spheres[], int randomPointsUsed, /*Point **randomPoints,*/ BoundingBox * boundingBox, unsigned int * seed, int verbose);

double calculateVolume(int randomPointsFounded, int randomPointsUsed, double boundingBoxVolume);

void timing_start(Timing *timing);
void timing_stop(Timing *timing);
void tag_mpi_print(const char *format, ...);
/* core header end */

/*
long seedgen(void)  {
    long s, seed, pid;

    pid = getpid();
    s = time ( NULL ); // get CPU seconds since 01/01/1970

    seed = abs(((s*181)*((pid-83)*359))%104729);
    return seed;
}
*/


int main(int argc, char *argv[]) {

	int	rank;
	int	num_procs;

	int randomPointsFoundedTotal;

	MPI_Init(&argc, &argv);
	/*
	int provided;
	char *required_string = "MPI_THREAD_FUNNELED";
	MPI_Init_thread(&argc, &argv, MPI_THREAD_FUNNELED, &provided);
	*/
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	/*
	char *provided_string = NULL;
	switch (provided) {
		case 0:
			provided_string = "MPI_THREAD_SINGLE";
			break;
		case 1;
			provided_string = "MPI_THREAD_FUNNELED";
			break;
		case 2:
			provided_string = "MPI_THREAD_MULTIPLE";
			break;
		case 3:
			provided_string = "MPI_THREAD_FUNNELED";
			break;
		default:
			break;
	}
	printf("rank: %d\trequired: %s\t provided:%s\n", rank, required_string, provided_string);
	*/

	MPI_Barrier(MPI_COMM_WORLD);
	MPI_Comm_size(MPI_COMM_WORLD, &num_procs);


	Arguments *arguments = checkArgs(argc,argv);

	int randomPointsUsed = arguments->howRandomPoints;
	int verbose = arguments->verbose;

	FileParsingResult * fpr = readFile(arguments->filename);

	struct utsname name;
	uname(&name);

	// in effetti tutti i nodi leggono i file, ma per pulizia dei print
		// ma per pulizia dei print si fa stampare solo al nodo 0
	//	if (verbose)
	if (rank==0)
		printf("Using data from %s, with %d points\n", arguments->filename, randomPointsUsed);
	free(arguments);

	int spheresNumber =  fpr->spheresNumber;
	Sphere *spheres = fpr->spheres;
	free(fpr);
	// anche qui, per pulizia dei print, si fa stampare solo al nodo 0
//	if (verbose)
		if (rank==0)
			printf("Founded %d spheres ", spheresNumber);
	Timings timings;

	timing_start( &(timings.timingInitBoundingBox) );
	// questa funzione crea la struttura rappresentante il parallelepipedo "BoundingBox",
	// che racchiude le sfere
	BoundingBox *boundingBox = initBoundingBox(spheresNumber,spheres);
	// e anche qui, sempre per pulizia dei print, si fa stampare solo al nodo 0
		if (rank==0)
			printf("in a bounding box of volume: %f\n", boundingBox->volume);
	timing_stop( &(timings.timingInitBoundingBox) );
	if (verbose==1)
		tag_mpi_print("rank %d (%s), read file and builded bounding box in: %f sec\n",rank, name.nodename, timings.timingInitBoundingBox.elapsed);

	uint seed;
	// faccio generare il seed base al rank 0
	timings.timingRankZeroGenSeed.elapsed = 0;
	if (rank==0) {
		timing_start( &(timings.timingRankZeroGenSeed) );
		seed = generateSeed();
		timing_stop( &(timings.timingRankZeroGenSeed) );
		timings.timingRankZeroGenSeed.elapsed = timings.timingRankZeroGenSeed.stop - timings.timingRankZeroGenSeed.start;
		if (verbose==1)
			tag_mpi_print("Rank %d (%s): seed %u in %f sec\n", rank, name.nodename, seed, timings.timingRankZeroGenSeed.elapsed);

		// mando il seed agli altri rank
		MPI_Bcast(&seed,
			 1,
 //			 MPI_UINT32_T, // data type - il seed è uint
			 // uso MPI_UNSIGNED per compatibilità con architetture 32bit
			 MPI_UNSIGNED,
			 0, // mittente
			 MPI_COMM_WORLD
			 );
	}
	else { // sono negli altri nodi, quindi ricevo
		MPI_Bcast(&seed,
			 1,
			 MPI_UNSIGNED,
			 0, // mittente
			 MPI_COMM_WORLD
			 );

		// affinchè le serie dei numeri casuali siano sufficientemente diverse,
		// aggiungo l'id del rank
//		seed+=time(NULL)*rank;
		seed+=rank;
		if (verbose==1)
			tag_mpi_print("Rank %d (%s): seed %u\n", rank, name.nodename, seed);
	}

	// divido il quantitativo dei punti casuali da utilizzare
	// per il numero dei nodi
	int randomPointsUsedEachNode = randomPointsUsed / num_procs;

	// eventualmente, se il numero suddetto non è multiplo esatto
	// del numero dei nodi, assegno il resto al master
	if (rank==0) {
		int surplus = randomPointsUsed % num_procs;
		if (surplus>0) {
			randomPointsUsedEachNode += surplus;
			if (verbose==1)
				tag_mpi_print("Rank %d (%s), %d random points used, with a surplus of %d\n",
						rank, name.nodename, randomPointsUsedEachNode, surplus);
		} else
			if (verbose==1)
			tag_mpi_print("Rank %d (%s), %d random points used\n",
						rank, name.nodename, randomPointsUsedEachNode);
	} else {
		if (verbose==1)
			tag_mpi_print("rank %d (%s), %d random points used\n",rank, name.nodename, randomPointsUsedEachNode);
	}

	// la computazione effettiva è nelle 4 righe sottostanti
	timing_start( &(timings.timingCheckPoints) );
	// genero i punti, normalizzati nel range delle coordinate del BoundingBox
//	Point **randomPoints = initRandomPoints(randomPointsUsedEachNode, boundingBox, seed);
	// verifico l'inclusione dei punti all'interno delle sfere
	int randomPointsFounded = checkPoints(spheresNumber,spheres,randomPointsUsedEachNode,/*randomPoints,*/ boundingBox, &seed, verbose);
	timing_stop( &(timings.timingCheckPoints) );
	if (verbose==1) {
		double percent = (double)randomPointsFounded/(double)randomPointsUsed*1e2;
		tag_mpi_print("rank %d (%s), founded %d/%d (= %.2f %%) random points within %d spheres, in %f sec\n",
				rank, name.nodename,
				randomPointsFounded, randomPointsUsed, percent,
				spheresNumber,
				timings.timingCheckPoints.elapsed);
	}
//	free(randomPoints);

	// i vari nodi mandano il risultato al master
	if (verbose==1)
		tag_mpi_print("rank %d, sending %d to 0\n", rank, randomPointsFounded);
	MPI_Reduce(&randomPointsFounded,
			&randomPointsFoundedTotal,
			1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD );


	// il master somma usando la reduce
	if (rank==0) {
		double volume = calculateVolume(randomPointsFoundedTotal,randomPointsUsed,boundingBox->volume);
		double total = timings.timingInitBoundingBox.elapsed + timings.timingRankZeroGenSeed.elapsed + timings.timingCheckPoints.elapsed;
		tag_mpi_print("-> Total time for Rank 0\t(%s):\t%f sec (sum of: initBoundingBox, generateSeed, checkPoints: %f, %f, %f)\n",
				name.nodename,
				total,
				timings.timingInitBoundingBox.elapsed,  timings.timingRankZeroGenSeed.elapsed, timings.timingCheckPoints.elapsed );
		double percent = (double)randomPointsFoundedTotal/(double)randomPointsUsed*1e2;
		printf("Volume: %f, using %d/%d points (=%.2f%%) \n\n",
				volume,
				randomPointsFoundedTotal,
				randomPointsUsed,
				percent
				);
	} else {
		tag_mpi_print("total time for rank %d\t(%s):\t%f sec (sum of: initBoundingBox, checkPoints: %f, %f)\n",
				rank, name.nodename,
				timings.timingInitBoundingBox.elapsed + timings.timingCheckPoints.elapsed,
				timings.timingInitBoundingBox.elapsed, timings.timingCheckPoints.elapsed);
	}

	MPI_Finalize();
	return 0;
}


/* core source begin */
void showUsage() {
	printf("Usage: montecarlo_spheres [options] <dataset_file_path> <how_many_random_point>\n"
			"options: -v: verbose\n");
//			"\t -t: an_integer_value: the integer parameter specify how threads will used for omp\n");
}

struct Arguments* checkArgs(int argc, char *argv[]) {

	if (argc<3 || argc>6) {
		showUsage();
		exit(EXIT_FAILURE);
		return NULL;
	}

//	char *tvalue = NULL;
	int index;

	Arguments *arguments = malloc(sizeof(Arguments));

	opterr = 0;
	int c;
	while ((c = getopt (argc, argv, "hv")) != -1)
	switch (c) {
	case 'h':
		showUsage();
		free(arguments);
		exit(EXIT_FAILURE);
		break;
	case 'v':
		arguments->verbose = 1;
		break;
	/*
	case 't':
		tvalue = optarg;
//		printf("tvalue: %s\n",tvalue);
//		char **ptr_tail;
		threads = strtol(tvalue,NULL,10);
		strtol(tvalue,NULL,10);
//		printf("t: %d, ptrtrail: %d\n",t,0);

		// bad check, but it works
		if ( !(threads>0 && threads<9) ) {
		   fprintf (stderr, "Option '-t' requires an integer between 1 and 9\n");
		   free(arguments);
		   exit(EXIT_FAILURE);
		   break;
		}

		arguments->threads = threads;
		break;
		*/
	case '?':
		if (isprint (optopt))
			fprintf (stderr, "Unknown option `-%c'.\n", optopt);
		else
			fprintf (stderr, "Unknown option character `\\x%x'.\n", optopt);
		free(arguments);
		exit(EXIT_FAILURE);
		break;
	default:
//			showUsage();
		break;
	}

	index = optind;
	if ((index==3 && argc!=5)||(index==4 && argc!=6)) {
		showUsage();
		exit(EXIT_FAILURE);
	}

	arguments->filename = argv[index];
	arguments->howRandomPoints = (int) (atof(argv[index+1]));

	return arguments;
}
/*
struct Arguments* checkArgsO(int argc, char *argv[]) {
	if (argc<3 || argc>4) {
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
		char* argv1 = argv[1];
		if (strcmp(argv1,"-v")!=0) {
			showUsage();
			return NULL;
		}
		arguments->verbose=1;
		arguments->filename=argv[2];
		arguments->howRandomPoints= (int) (atof(argv[3]));
	}
	return arguments;
}*/

struct FileParsingResult* readFile(char fileName[]) {
	FileParsingResult *fpr;
	fpr = malloc( sizeof(FileParsingResult) );
	FILE* fp = NULL;
	fp = fopen (fileName, "rt");
	if (!fp) {
		char msg[256];
		snprintf(msg, sizeof msg, "%s%s", "File open error!\n", fileName);
		perror (msg);
		exit(1);
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
struct BoundingBox *initBoundingBox(int spheresNumber, Sphere *spheres) {

	double boundingBoxXMin = UNINIT;
	double boundingBoxXMax = UNINIT;

	double boundingBoxZMin = UNINIT;
	double boundingBoxZMax = UNINIT;

	double boundingBoxYMin = UNINIT;
	double boundingBoxYMax = UNINIT;

	int i=0;
	for (i=0;i<spheresNumber;i++) {

		Sphere sphere = spheres[i];
		double radius = sphere.radius;

		double cx = sphere.x;
		double computedXLB = 	calculateLowerBound(cx,radius,boundingBoxXMin);
		if (boundingBoxXMin==UNINIT || computedXLB != boundingBoxXMin) {
			boundingBoxXMin = computedXLB;
		}
		double computedXHB = calculateHigherBound(cx,radius,boundingBoxXMax);
		if (boundingBoxXMax==UNINIT || computedXHB != boundingBoxXMax) {
			boundingBoxXMax = computedXHB;
		}

		double cz = sphere.z;
		double computedZLB = calculateLowerBound(cz,radius,boundingBoxZMin);
		if (boundingBoxZMin==UNINIT || computedZLB != boundingBoxZMin) {
			boundingBoxZMin = computedZLB;
		}
		double computedZHB = calculateHigherBound(cz,radius,boundingBoxZMax);
		if (boundingBoxZMax==UNINIT || computedZHB != boundingBoxZMax) {
			boundingBoxZMax = computedZHB;
		}

		double cy = sphere.y;
		double computedYLB = calculateLowerBound(cy,radius,boundingBoxYMin);
		if (boundingBoxYMin==UNINIT ||computedYLB != boundingBoxYMin) {
			boundingBoxYMin = computedYLB;
		}
		double computedYHB = calculateHigherBound(cy,radius,boundingBoxYMax);
		if (boundingBoxYMax==UNINIT || computedYHB != boundingBoxYMax) {
			boundingBoxYMax = computedYHB;
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

	Point *point = malloc(sizeof(Point));
	point->x = xr;
	point->y = yr;
	point->z = zr;

	return point;
}
float buildRandomFloat(unsigned int * seed) {
	return (float)rand_r(seed)/RAND_MAX;
}

/*struct Point **initRandomPoints(int randomPointsUsed, BoundingBox * boundingBox, unsigned int seed) {
	int r=0;
	Point **randomPoints = malloc( sizeof(Point *)*randomPointsUsed );

	for (r=0;r<randomPointsUsed;r++) {
		Point *rp = buildRandomPoint( buildRandomFloat(seed) ,
				buildRandomFloat(seed),
				buildRandomFloat(seed),
				boundingBox);
		randomPoints[r]=rp;
	}

	return randomPoints;
}*/

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
int checkPoints(int spheresNumber, Sphere spheres[], int randomPointsUsed, /*Point **randomPoints,*/ BoundingBox * boundingBox, unsigned int * seed, int verbose) {
	int randomPointsFounded = 0, i,j;

	// hic sunt leones?
#ifdef _OPENMP
	// very dirty, but it is just an experiment
	char *num_threads_value = getenv("OMP_NUM_THREADS");
	int num_threads = 1; // default
	if (num_threads_value!=NULL) {
		int nt = atoi(num_threads_value);
		if (nt>1)
			num_threads = nt;
	}
	omp_set_num_threads(num_threads);
	double time_start = omp_get_wtime();
#endif

	#pragma omp parallel
	{ // begin pragma omp
	#pragma omp for
		for (i=0;i<randomPointsUsed;i++) {
//			Point *rp = randomPoints[i];
			Point *rp = buildRandomPoint(
					buildRandomFloat(seed),
					buildRandomFloat(seed),
					buildRandomFloat(seed),
					boundingBox);
			for (j=0;j<spheresNumber;j++) {
				Sphere s = spheres[j];
				if (isContained(&s,rp)) {
					randomPointsFounded++;
					break;
				}
			}
			free(rp);
		}
	} // end pragma omp
	#ifdef _OPENMP
	   double time_stop = omp_get_wtime();
	   if (omp_get_max_threads()>1)
		   tag_omp_print("using %d threads takes %f sec\n", omp_get_max_threads(), (time_stop - time_start));
	#endif

	return randomPointsFounded;
}

double calculateVolume(int randomPointsFounded, int randomPointsUsed, double boundingBoxVolume) {
	double volumeTotalMontecarlo = ( (0.0+randomPointsFounded) / (0.0+randomPointsUsed) )*boundingBoxVolume;
	return volumeTotalMontecarlo;
}

void timing_start(Timing *timing) {
	timing->start = MPI_Wtime();
};
void timing_stop(Timing *timing) {
	timing->stop = MPI_Wtime();
	timing->elapsed = timing->stop - timing->start;
};

void tag_mpi_print(const char *format, ...) {
	va_list args;
	printf("%s: ", TAG_MPI);
	va_start( args, format );
	vprintf( format, args );
	va_end( args );
}
/* core source end */
