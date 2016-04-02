package net.iubris.aa.spheres.volume.pyjama;

import java.io.IOException;

import pj.Pyjama;

public class SVMainPyjama {
	
	public static void main(String[] args) {
		
		System.out.println("*** Parallel Version by PyJama ***");
		
//		Pyjama.omp_set_num_threads(8);
		
		for (double r=1e0;r<1e8; r=r*10 ) {
			run("ds/sfere1.in", r, 4);
		}
//		run("ds/sfere1.in", 1e8, 8);
	}
	
	private static void run(String fileName, double randomPoints, int numThreads) {
		
//		SpheresVolumeMPJomp sv = new SpheresVolumeMPJomp( args[0], Double.parseDouble(args[1]) );
//		Pyjama.omp_set_num_threads(numThreads);
//		System.out.println( Pyjama.omp_get_max_threads() );
//		System.out.println( Pyjama.omp_get_thread_num()+" " );
		SpheresVolumeMPPyjama sv = new SpheresVolumeMPPyjama( fileName, randomPoints );
		long start = System.currentTimeMillis();
		try {
			sv.parseInFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sv.init();
		sv.calculate();
		System.out.println( sv.getResult() );
		long end = System.currentTimeMillis();
		long time = end-start;
		System.out.println("in: "+time/1000.0f+" sec");
	}
		
}
