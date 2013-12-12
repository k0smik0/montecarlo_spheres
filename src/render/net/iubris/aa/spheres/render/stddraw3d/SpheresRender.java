package net.iubris.aa.spheres.render.stddraw3d;

import java.awt.Color;

import net.iubris.aa.spheres.SpheresVolume;
import net.iubris.aa.spheres.model.BoundingBox;
import net.iubris.aa.spheres.model.DimensionBounds;
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
		StdDraw3D.setCanvasSize(800, 600);
		int scale = 20;
		StdDraw3D.setScale(-scale,scale);
		
		double[] axesUsed = new double[scale];
		double[] axesNotUsed = new double[scale];
		for (int i=0;i<scale;i++) {
			axesUsed[i]=i;
			axesNotUsed[i]=0;
		}
		StdDraw3D.lines(axesUsed, axesNotUsed, axesNotUsed).setColor(Color.WHITE);
		StdDraw3D.lines(axesNotUsed, axesUsed, axesNotUsed).setColor(Color.WHITE);
		StdDraw3D.lines(axesNotUsed, axesNotUsed, axesUsed).setColor(Color.WHITE);
		
		StdDraw3D.text3D(0, 0, 0, "0");
		for (int l=-scale;l<=scale;l=l+2) {
			StdDraw3D.text3D(l, 0, 0, ""+l);
			StdDraw3D.text3D(0, l, 0, ""+l);
			StdDraw3D.text3D(0, 0, l, ""+l);
		}
		
		DimensionBounds xDimension = bb.getXDimension();
		DimensionBounds yDimension = bb.getYDimension();
		DimensionBounds zDimension = bb.getZDimension();
		
		double xMax = xDimension.getMax();
		double yMax = yDimension.getMax();
		double zMax = zDimension.getMax();

		double xMin = xDimension.getMin();
		double yMin = yDimension.getMin();
		double zMin = zDimension.getMin();
		
		double wbXCenter = xMin+(xMax-xMin)/2;
		double wbYCenter = yMin+(yMax-yMin)/2;
		double wbZCenter = zMin+(zMax-zMin)/2;

		double wbWidth = bb.getWidth()/2;
		double wbHeight = bb.getHeight()/2;
		double wbDepth = bb.getDepth()/2;
		
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
			StdDraw3D.wireSphere(s.getX(), s.getY(), s.getZ(), s.getRadius()).setColor(Color.BLUE, 50);
			/*Shape wireSphere = StdDraw3D.wireSphere(0, 0, 0, s.getRadius());
			wireSphere.setColor(Color.BLUE, 50);
			Vector3D vector3D = new Vector3D(s.getX(), s.getY(), s.getZ());
			StdDraw3D.pause(5);
			wireSphere.move(vector3D);*/			
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
			rxs[i] = randomPoint.getX();
			rys[i] = randomPoint.getY();
			rzs[i] = randomPoint.getZ();
		}
		StdDraw3D.points(rxs, rys, rzs);
		StdDraw3D.finished();
		
		System.out.println("");
	}
}
