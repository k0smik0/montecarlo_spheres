package net.iubris.aa.spheres;

import java.awt.Color;
import java.io.IOException;

import edu.princeton.stddraw3d.StdDraw3D;
import net.iubris.aa.spheres.model.BoundingBox;
import net.iubris.aa.spheres.model.RandomPoint;
import net.iubris.aa.spheres.model.Sphere;

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
		
		
		BoundingBox bb = sv.getBoundingBox();
		
		double maxXY = Math.max(bb.getXDimension().getMax()-bb.getXDimension().getMin(), 
				bb.getYDimension().getMax()-bb.getYDimension().getMin());
		double max = Math.max(maxXY,
				bb.getZDimension().getMax()-bb.getZDimension().getMin());
		StdDraw3D.setScale(-max,max);
//		StdDraw3D.box
		double wbXCenter = (bb.getXDimension().getMax()-bb.getXDimension().getMin())/2;
		double wbYCenter = (bb.getYDimension().getMax()-bb.getYDimension().getMin())/2;
		double wbZCenter = (bb.getZDimension().getMax()-bb.getZDimension().getMin())/2;
		double wbWidth = (bb.getXDimension().getMax()-bb.getXDimension().getMin());
		double wbHeight = (bb.getYDimension().getMax()-bb.getYDimension().getMin());
		double wbDepth = (bb.getZDimension().getMax()-bb.getZDimension().getMin());
		System.out.println(wbXCenter+" "+wbYCenter+" "+wbZCenter+" "+wbWidth+" "+wbHeight+" "+wbDepth);
		StdDraw3D.wireBox(
				wbXCenter,
				wbYCenter,
				wbZCenter,
				wbWidth,
				wbHeight,				
				wbDepth			
				).setColor(Color.RED, 130);
		for (Sphere s: sv.getSpheres()) {
//			System.out.println(s);
//			StdDraw3D.wireSphere(s.getX(), s.getY(), s.getZ(), s.getRadius()).setColor(Color.BLUE, 2);
		}
//		int rn=0;
		for (RandomPoint r: sv.getRandomPoints()) {
//			System.out.println(rn+": "+r);
//			rn++;
			StdDraw3D.point(r.getX(), r.getY(), r.getZ()).setColor(Color.GREEN, 255);
		}
		StdDraw3D.finished();
//		StdDraw3D.wireSphere(0, 0, 0, 1);
		
		System.out.println("");
	}
	
	public static void main(String[] args) {
		run("ds/sfere1.in");
//		run("ds/sfere2.in");
//		run("ds/sfere3.in");
		
		// rendering section
//		sv.start();
	}

}
