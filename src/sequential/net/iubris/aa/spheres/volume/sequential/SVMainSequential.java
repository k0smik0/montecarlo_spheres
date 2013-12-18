package net.iubris.aa.spheres.volume.sequential;

import java.io.IOException;

public class SVMainSequential {
	
	public static void main(String[] args) {

		System.out.println("*** Sequential Version ***");
		for (double r=1e0;r<1e8; r=r*10 ) {
			run("ds/sfere1.in", r);
		}
		System.out.println("");
	}
	
private static void run(String fileName, double randomPoints) {
		
//		SpheresVolumeMPJomp sv = new SpheresVolumeMPJomp( args[0], Double.parseDouble(args[1]) );
		SpheresVolumeSequential sv = new SpheresVolumeSequential( fileName, randomPoints );
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
