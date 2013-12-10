package net.iubris.aa.spheres.render.stddraw3d;

import java.awt.Color;

import net.iubris.aa.spheres.SpheresVolume;
import net.iubris.aa.spheres.model.BoundingBox;
import net.iubris.aa.spheres.model.RandomPoint;
import net.iubris.aa.spheres.model.Sphere;
import edu.princeton.stddraw3d.StdDraw3D;

public class SpheresRender {

	public static void draw(SpheresVolume sv) {
		BoundingBox bb = sv.getBoundingBox();
		
		/*double maxXY = Math.max(bb.getXDimension().getMax()-bb.getXDimension().getMin(), 
				bb.getYDimension().getMax()-bb.getYDimension().getMin());
		double max = Math.max(maxXY,
				bb.getZDimension().getMax()-bb.getZDimension().getMin());*/
		StdDraw3D.setCanvasSize(1280, 800);
		StdDraw3D.setScale(-10,10);
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
		RandomPoint[] randomPoints = sv.getRandomPoints();
		int randomPointsHow = randomPoints.length;
		double[] rxs = new double[randomPointsHow];
		double[] rys = new double[randomPointsHow];
		double[] rzs = new double[randomPointsHow];
//		for (int r=0;i<ranRandomPoint r: sv.getRandomPoints()) {
		for (int i=0;i<randomPointsHow;i++) {
		
//			System.out.println(rn+": "+r);
//			rn++;
//			StdDraw3D.point(r.getX(), r.getY(), r.getZ()).setColor(Color.GREEN, 255);
			RandomPoint randomPoint = randomPoints[i];
			System.out.println(randomPoint);
			rxs[i] = randomPoint.getX();
			rys[i] = randomPoint.getY();
			rzs[i] = randomPoint.getZ();
		}
		StdDraw3D.points(rxs, rys, rzs);
		StdDraw3D.finished();
//		StdDraw3D.wireSphere(0, 0, 0, 1);
		
		System.out.println("");
	}
}
