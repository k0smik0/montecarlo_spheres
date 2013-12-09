package net.iubris.aa.spheres.model;

public class RandomPoint {

	private double x;
	private double y;
	private double z;

	public RandomPoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;		
	}
	
	public boolean isInSphere(Sphere sphere) {
		
		double sx = sphere.getX();
		double sy = sphere.getY();
		double sz = sphere.getZ();
		double sr = sphere.getRadius();
		
		double sxEquationMember = (x - sx)*(x - sx) + (y - sy)*(y - sy) + (z - sz)*(z - sz);
		
		if ( sxEquationMember <= (sr*sr) )
			return true;
		return false;		
	}
	
	public String toString() {
		return "("+x+", "+y+", "+z+")";
	}

}
