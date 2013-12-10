package net.iubris.aa.spheres;

import java.awt.Color;
import java.io.IOException;

import edu.princeton.stddraw3d.StdDraw3D;
import net.iubris.aa.spheres.model.BoundingBox;
import net.iubris.aa.spheres.model.RandomPoint;
import net.iubris.aa.spheres.model.Sphere;
import net.iubris.aa.spheres.render.stddraw3d.SpheresRender;

public class SVMain {
	
	private static void run(String inFileName,String title) {
		System.out.println(""+title);
		double howRandoms = 1e5;
		SpheresVolume sv = new SpheresVolume(inFileName,howRandoms);
		try {
			sv.parseInFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sv.initSpheresCollection();
		sv.initBoundingBox();
		sv.calculateVolume();
		
//		SpheresRender.draw(sv);
	}
	
	public static void main(String[] args) {
		run("ds/sfere1.in","sfere NON intersecanti NÉ tangenti");
//		run("ds/sfere2.in");
//		run("ds/sfere3.in");
		
		// rendering section
//		sv.start();
	}

}
