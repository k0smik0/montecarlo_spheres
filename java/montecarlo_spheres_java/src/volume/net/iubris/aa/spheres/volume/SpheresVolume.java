package net.iubris.aa.spheres.volume;

import java.io.IOException;
import java.util.List;

import net.iubris.aa.spheres.model.BoundingBox;
import net.iubris.aa.spheres.model.Point;
import net.iubris.aa.spheres.model.Sphere;
import net.iubris.aa.spheres.model.Volume;

public interface SpheresVolume {
	void calculateVolume();
	public String getResult();
	public BoundingBox getBoundingBox();
	public Sphere[] getSpheres();
	public Point[] getRandomPoints();
	public Volume getVolume();
	public List<Point> getRandomPointsFound();
	void parseInFile() throws IOException;
	void init();
}
