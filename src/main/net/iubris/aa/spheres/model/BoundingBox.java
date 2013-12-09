package net.iubris.aa.spheres.model;

public class BoundingBox {
	
	private DimensionBounds xDimension;
	private DimensionBounds yDimension;
	private DimensionBounds zDimension;
	
	public BoundingBox() {}
	
	public BoundingBox(DimensionBounds xDimension, DimensionBounds yDimension, DimensionBounds zDimension) {
		this.xDimension = xDimension;
		this.yDimension = yDimension;
		this.zDimension = zDimension;
	}

	public DimensionBounds getxDimension() {
		return xDimension;
	}

	public DimensionBounds getyDimension() {
		return yDimension;
	}

	public DimensionBounds getzDimension() {
		return zDimension;
	}
	
	public Double getVolume() {
		return ( (xDimension.getMax()-xDimension.getMin())
		* (yDimension.getMax()-yDimension.getMin())
		* (zDimension.getMax()-zDimension.getMin()) );
	}
	
	public String toString() {
		return xDimension+" "+yDimension+" "+zDimension;
	}
}
