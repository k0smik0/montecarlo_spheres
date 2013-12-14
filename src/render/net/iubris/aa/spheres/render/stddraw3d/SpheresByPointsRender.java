package net.iubris.aa.spheres.render.stddraw3d;

import net.iubris.aa.spheres.SpheresVolume;
import edu.princeton.stddraw3d.StdDraw3D;

public class SpheresByPointsRender extends AbstractRender {

	private final SpheresVolume spheresVolume;

	public SpheresByPointsRender(SpheresVolume spheresVolume) {
		this.spheresVolume = spheresVolume;
	}
	
	public void draw() {
		StdDraw3D.setCanvasSize(1280, 800);
		
		int maxScale = drawBoundingBox(spheresVolume.getBoundingBox());
		
		drawAxis( maxScale );
		
		StdDraw3D.finished();	
	}
}
