package net.iubris.aa.spheres.render.stddraw3d;

import java.io.IOException;

import net.iubris.aa.spheres.volume.AbstractSpheresVolume;
import net.iubris.aa.spheres.volume.pyjama.SpheresVolumeMPPyjama;

public class SVMainRenderStddraw3d {
	
	public static void main(String[] args) {
		new SpheresByPointsRenderPyJava( 
				calculateSpheresVolume("ds/sfere1.in", "sfere indipendenti", 1e5 ) )
		.draw();
	}
	
	private static AbstractSpheresVolume calculateSpheresVolume(String inFileName, String title, double howRandoms) {
		System.out.println(""+title+" con "+howRandoms+" punti");
		AbstractSpheresVolume sv = new SpheresVolumeMPPyjama(inFileName,howRandoms);
		try {
			sv.parseInFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sv.init();
		sv.calculate();
		
		return sv;
	}

}
