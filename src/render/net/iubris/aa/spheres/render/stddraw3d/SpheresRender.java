package net.iubris.aa.spheres.render.stddraw3d;

import java.awt.Color;

import net.iubris.aa.spheres.model.Point;
import net.iubris.aa.spheres.model.Sphere;
import net.iubris.aa.spheres.volume.sequential.SpheresVolumeSequential;
import edu.princeton.stddraw3d.StdDraw3D;

public class SpheresRender extends AbstractRender {
	
	public SpheresRender(SpheresVolumeSequential spheresVolume) {
		super(spheresVolume);
	}

	public void draw() {
		StdDraw3D.setCanvasSize(1280, 800);
		
		drawBoundingBox();
		
		drawAxis();
		
		drawSpheres();
		
		drawPoints();

		StdDraw3D.finished();
		
		System.out.println("");
	}
	
	private void drawSpheres() {
		for (Sphere s: spheresVolume.getSpheres()) {
			StdDraw3D.wireSphere(s.getX(), s.getY(), s.getZ(), s.getRadius()).setColor(Color.BLUE, 50);
		}
	}
	private void drawPoints() {
		Point[] randomPoints = spheresVolume.getRandomPoints();
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
