package net.iubris.aa.spheres.volume.sequential;

import net.iubris.aa.spheres.model.Point;
import net.iubris.aa.spheres.model.Sphere;
import net.iubris.aa.spheres.volume.AbstractSpheresVolume;

public class SpheresVolumeSequential extends AbstractSpheresVolume {
		
	public SpheresVolumeSequential(String inFileName, double randomPointsUsed) {
		super(inFileName, randomPointsUsed);
	}
	
//	@Override
	public void calculate() {
		checkPointsSequential();
		calculateVolume();
	}
	
	protected void checkPointsSequential() {
		int howSpheres = spheres.length;		
		for (int i=0;i<randomPointsUsed;i++) {
			Point rp = randomPoints[i];
			for (int j=0;j<howSpheres;j++) {
				Sphere s = spheres[j];
				if (s.contains(rp)) {
					randomPointsFoundedNumber++;					
					// minimal code end here. below is just for info and rendering
//					storeMoreInfo(s,rp);
					break;
				}
			}
		}
	}
}