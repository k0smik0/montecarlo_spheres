package net.iubris.aa.spheres.volume.jomp;

import java.io.IOException;


import jomp.runtime.OMP;

public class SVMainJomp {
	
	public static void main(String[] args) {
		
		System.out.println("*** Parallel Version by Jomp***");

//		run("ds/sfere1.in", 1e1, 8);
		for (double r=1e0;r<1e8; r=r*10 ) {
			run("ds/sfere1.in", r, 8);
//			run("ds/sfere1.in", 1e1, 8);
			/*try {
				System.out.println(Thread.currentThread().getName());
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
//			System.out.println("");
		}
	}

	private static void run(String fileName, double randomPoints, int numThreads) {
		
		OMP.setNumThreads(8);
//		SpheresVolumeMPJomp sv = new SpheresVolumeMPJomp( args[0], Double.parseDouble(args[1]) );
		SpheresVolumeMPJomp sv = new SpheresVolumeMPJomp( fileName, randomPoints );
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
