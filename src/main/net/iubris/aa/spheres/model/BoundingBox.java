package net.iubris.aa.spheres.model;

public class BoundingBox {
	
	private final DimensionBounds xDimensionBounds;
	private final DimensionBounds yDimensionBounds;
	private final DimensionBounds zDimensionBounds;
	private final double volume;
	private final Sphere xMinSphere;
	private final Sphere xMaxSphere;
	private final Sphere yMinSphere;
	private final Sphere yMaxSphere;
	private final Sphere zMinSphere;
	private final Sphere zMaxSphere;
	private final double width; // x
	private final double height; // z
	private final double depth; // y

	public BoundingBox(DimensionBounds xDimensionBounds, DimensionBounds yDimensionBounds, DimensionBounds zDimensionBounds,
			Sphere xMinSphere, Sphere xMaxSphere,
			Sphere yMinSphere, Sphere yMaxSphere,
			Sphere zMinSphere, Sphere zMaxSphere			
			) {
 		this.xDimensionBounds = xDimensionBounds;
		this.zDimensionBounds = zDimensionBounds;
		this.yDimensionBounds = yDimensionBounds;
		this.width = xDimensionBounds.getMax()-xDimensionBounds.getMin();
		this.height = zDimensionBounds.getMax()-zDimensionBounds.getMin();
		this.depth = yDimensionBounds.getMax()-yDimensionBounds.getMin();
		this.volume = width * height * depth;		
		this.xMinSphere = xMinSphere;
		this.xMaxSphere = xMaxSphere;
		this.yMinSphere = yMinSphere;
		this.yMaxSphere = yMaxSphere;
		this.zMinSphere = zMinSphere;
		this.zMaxSphere = zMaxSphere;
	}

	public DimensionBounds getXDimension() {
		return xDimensionBounds;
	}
	public DimensionBounds getYDimension() {
		return yDimensionBounds;
	}
	public DimensionBounds getZDimension() {
		return zDimensionBounds;
	}
	
	public Double getVolume() {
		return volume;
	}
	
	public DimensionBounds getxDimensionBounds() {
		return xDimensionBounds;
	}
	public DimensionBounds getyDimensionBounds() {
		return yDimensionBounds;
	}
	public DimensionBounds getzDimensionBounds() {
		return zDimensionBounds;
	}

	public Sphere getxMinSphere() {
		return xMinSphere;
	}
	public Sphere getxMaxSphere() {
		return xMaxSphere;
	}
	public Sphere getyMinSphere() {
		return yMinSphere;
	}
	public Sphere getyMaxSphere() {
		return yMaxSphere;
	}
	public Sphere getzMinSphere() {
		return zMinSphere;
	}
	public Sphere getzMaxSphere() {
		return zMaxSphere;
	}
	
	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getDepth() {
		return depth;
	}

	public String toString() {
		String s = "Bounding box with bounds spheres:\n"
				+"\tsphere on xMin: "+xMinSphere+"\n"
				+"\tsphere on xMax: "+xMaxSphere+"\n"
				+"\tsphere on yMin: "+yMinSphere+"\n"
				+"\tsphere on yMax: "+yMaxSphere+"\n"
				+"\tsphere on zMin: "+zMinSphere+"\n"
				+"\tsphere on zMax: "+zMaxSphere+"\n"				
				+"\twith bounds: x:"+xDimensionBounds+" y:"+yDimensionBounds+" z:"+zDimensionBounds+",\n"
				+"\tof dimensions: "+width+" * "+depth+" * "+height+" and volume = "+volume+",\n";
		return s;
	}
}
