package net.iubris.aa.spheres.model;

public class DimensionBounds {

	private double min;
	private double max;
	
	public DimensionBounds() {}
	
	public DimensionBounds(double min, double max) {
		this.min = min;
		this.max = max;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	
	public String toString() {
		return "("+min+","+max+")";
	}
}
