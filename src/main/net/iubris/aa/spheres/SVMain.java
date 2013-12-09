package net.iubris.aa.spheres;

import java.io.IOException;

public class SVMain {
	
	private static void run(String inFileName) {		
		double howRandoms = 1e4;
		SpheresVolume sv = new SpheresVolume(inFileName,howRandoms);
		try {
			sv.parseInFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sv.initSpheresCollection();
		sv.initBoundingBox();
		sv.calculateVolume();
		
		System.out.println("");
	}
	
	public static void main(String[] args) {
		run("ds/sfere1.in");
		run("ds/sfere2.in");
		run("ds/sfere3.in");
		
		// rendering section
//		sv.start();
	}

}
