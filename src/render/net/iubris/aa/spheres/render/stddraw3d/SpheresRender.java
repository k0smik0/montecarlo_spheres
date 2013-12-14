package net.iubris.aa.spheres.render.stddraw3d;

import java.awt.Color;

import net.iubris.aa.spheres.SpheresVolume;
import net.iubris.aa.spheres.model.Point;
import net.iubris.aa.spheres.model.Sphere;
import edu.princeton.stddraw3d.StdDraw3D;

public class SpheresRender extends AbstractRender {
	
	private final SpheresVolume spheresVolume;
	
	public SpheresRender(SpheresVolume spheresVolume) {
		this.spheresVolume = spheresVolume;
	}

	public void draw() {
		StdDraw3D.setCanvasSize(1280, 800);
		
		int maxScale = drawBoundingBox(spheresVolume.getBoundingBox());
		
		drawAxis( maxScale );
		
		drawSpheres(spheresVolume);
		
		drawPoints(spheresVolume);

		StdDraw3D.finished();
		
		System.out.println("");
	}
	
	private static void drawSpheres(SpheresVolume sv) {
		for (Sphere s: sv.getSpheres()) {
			StdDraw3D.wireSphere(s.getX(), s.getY(), s.getZ(), s.getRadius()).setColor(Color.BLUE, 50);
		}
	}
	private static void drawPoints(SpheresVolume sv) {
		Point[] randomPoints = sv.getRandomPoints();
		int randomPointsHow = randomPoints.length;
		double[] rxs = new double[randomPointsHow];
		double[] rys = new double[randomPointsHow];
		double[] rzs = new double[randomPointsHow];
		for (int i=0;i<randomPointsHow;i++) {
			Point randomPoint = randomPoints[i];
			rxs[i] = randomPoint.getX();
			rys[i] = randomPoint.getY();
			rzs[i] = randomPoint.getZ();
		}
		StdDraw3D.points(rxs, rys, rzs).setColor(Color.GREEN, 80);
	}
}
