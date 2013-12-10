package net.iubris.aa.spheres.model;

public class Sphere {

	private final double x;
	private final double y;
	private final double z;
	private final double radius;

	private double volume;
	private int containedRandomPoints = 0;

	public Sphere(double x, double y, double z, double radius) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;

		this.volume = (4.0 / 3.0)
				* Math.PI 
//	 		  * Math.pow(radius,3);
				* radius*radius*radius;
	}

	public double getVolume() {
		return volume;
	}

	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	public double getRadius() {
		return radius;
	}
	
	public String toString() {
		return "("+x+", "+y+", "+z+"),\tradius = "+radius+",\tvolume = "+volume;
	}

	public int incrementContainedRandomPoints() {
		containedRandomPoints ++;
		return containedRandomPoints;
	}
	
	public int getContainedRandomPoints() {
		return containedRandomPoints;
	}
}