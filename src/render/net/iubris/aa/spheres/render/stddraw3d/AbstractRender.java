package net.iubris.aa.spheres.render.stddraw3d;

import java.awt.Color;

import net.iubris.aa.spheres.model.BoundingBox;
import net.iubris.aa.spheres.model.DimensionBounds;
import edu.princeton.stddraw3d.StdDraw3D;
import edu.princeton.stddraw3d.StdDraw3D.Shape;

public abstract class AbstractRender {
	
	protected int drawBoundingBox(BoundingBox bb) {
		DimensionBounds xDimension = bb.getxDimensionBounds();
		DimensionBounds yDimension = bb.getYDimensionBounds();
		DimensionBounds zDimension = bb.getZDimensionBounds();
		
		double xMax = xDimension.getMax();
		double yMax = yDimension.getMax();
		double zMax = zDimension.getMax();
		
		double xMin = xDimension.getMin();
		double yMin = yDimension.getMin();
		double zMin = zDimension.getMin();
		
		double wbXCenter = xMin+(xMax-xMin)/2;
		double wbYCenter = yMin+(yMax-yMin)/2;
		double wbZCenter = zMin+(zMax-zMin)/2;
		
		//System.out.println(xMin+"+("+xMax+"-"+xMin+")"+"/2");

		double wbWidth = bb.getWidth()/2;
		double wbHeight = bb.getHeight()/2;
		double wbDepth = bb.getDepth()/2;
		
		System.out.println("(wbXCenter,wbYCenter,wbZCenter) wbWidth*wbHeight*wbDepth");
		
		System.out.println("("+wbXCenter+","+wbYCenter+","+wbZCenter+") "+wbWidth+"*"+wbDepth+"*"+wbHeight);
		
		StdDraw3D.wireBox(
				wbXCenter,
				wbYCenter,
				wbZCenter,				
				wbWidth,
				wbDepth,
				wbHeight
				).setColor(Color.RED, 130);
		
		int maxScale = (int) Math.max( Math.max(xMax, yMax), zMax);
		return maxScale;
	}
	
	protected void drawAxis(int scale) {
		StdDraw3D.setScale(-2*scale/3,2*scale/3);
		
		double[] axesUsed = new double[2];
		axesUsed[0]=-scale-10;
		axesUsed[1]=scale+10;
		double[] axesNotUsed = new double[2]; //(0, 0);

		StdDraw3D.lines(axesUsed, axesNotUsed, axesNotUsed)
			.setColor(Color.YELLOW);
		StdDraw3D.lines(axesNotUsed, axesUsed, axesNotUsed)
			.setColor(Color.YELLOW);
		StdDraw3D.lines(axesNotUsed, axesNotUsed, axesUsed)
			.setColor(Color.YELLOW);
		
		Shape textCenter = StdDraw3D.text3D(0, 0, 0, "0");
		textCenter.setColor(Color.YELLOW);
		textCenter.scale(10);
		for (int l=-scale;l<=scale;l=l+2) {
			StdDraw3D.text3D(l, 0, 0, ""+l).scale(10);
			StdDraw3D.text3D(0, l, 0, ""+l).scale(10);
			StdDraw3D.text3D(0, 0, l, ""+l).scale(10);
		}
	}

}
