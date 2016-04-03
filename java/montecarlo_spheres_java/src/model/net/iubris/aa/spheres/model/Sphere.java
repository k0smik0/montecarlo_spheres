package net.iubris.aa.spheres.model;

public class Sphere {

	private final double cx;
	private final double cy;
	private final double cz;
	private final double radius;

	private double volume;
	private int containedRandomPoints = 0;

	public Sphere(double cx, double cy, double cz, double radius) {
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
		this.radius = radius;

		this.volume = (4.0 / 3.0)
				* Math.PI 
//	 		  * Math.pow(radius,3);
				* radius*radius*radius;
	}
	
	public boolean contains(Point point) {
		
		double px = point.getX();
		double py = point.getY();
		double pz = point.getZ();
		
		double sxEquationMember = (px-cx)*(px-cx) + (py-cy)*(py-cy) + (pz-cz)*(pz - cz);
		double dxEquationMember = radius*radius;
		
		if ( sxEquationMember <= dxEquationMember )
			return true;
		return false;		
	}
	

	public double getVolume() {
		return volume;
	}

	public double getX() {
		return cx;
	}
	public double getY() {
		return cy;
	}
	public double getZ() {
		return cz;
	}
	public double getRadius() {
		return radius;
	}
	
	public String toString() {
		return "("+cx+", "+cy+", "+cz+"),\tradius = "+radius+",\tvolume = "+volume;
	}

	public int incrementFoundPoints() {
		containedRandomPoints++;
		return containedRandomPoints;
	}
	
	public int getContainedRandomPoints() {
		return containedRandomPoints;
	}
}