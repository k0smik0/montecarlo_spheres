package net.iubris.aa.spheres.render.stddraw3d;

import java.awt.Color;

import net.iubris.aa.spheres.model.Point;
import net.iubris.aa.spheres.volume.AbstractSpheresVolume;
import edu.princeton.stddraw3d.StdDraw3D;

public class SpheresByPointsRender extends AbstractRender {

	public SpheresByPointsRender(AbstractSpheresVolume spheresVolume) {
		super(spheresVolume);
	}
	
	public void draw() {
		StdDraw3D.setCanvasSize(1280, 800);
		
		drawBoundingBox();
		
		drawAxis();
		
		drawSpheresByPoints();
		
		StdDraw3D.finished();	
	}
	
	private void drawSpheresByPoints() {
		Point[] randomPoints = spheresVolume.getRandomPointsFound().toArray( new Point[0] );
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
