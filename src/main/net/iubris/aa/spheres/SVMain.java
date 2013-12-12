package net.iubris.aa.spheres;

import java.io.IOException;

import net.iubris.aa.spheres.render.stddraw3d.SpheresRender;

public class SVMain {
	private static void run(String inFileName,String title, double howRandoms) {
		System.out.println(""+title+" con "+howRandoms+" punti");
		SpheresVolume sv = new SpheresVolume(inFileName,howRandoms);
		try {
			sv.parseInFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sv.initSpheresCollection();
		sv.initBoundingBox();
		sv.calculateVolume();

		// rendering section
		SpheresRender.draw(sv);
	}
	
	public static void main(String[] args) {
		run("ds/sfere1.in", "*** 1000 sfere NON intersecanti NÉ tangenti ***", 1E5);
		System.out.println("\n");
//		run("ds/sfere2.in", "*** 1000 sfere concentriche ***", 1E6);
//		run("ds/sfere3.in", "sfere casuali", 1E6);
	}

}
