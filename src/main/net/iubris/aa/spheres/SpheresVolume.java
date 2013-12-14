package net.iubris.aa.spheres;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.iubris.aa.spheres.model.BoundingBox;
import net.iubris.aa.spheres.model.DimensionBounds;
import net.iubris.aa.spheres.model.Point;
import net.iubris.aa.spheres.model.Sphere;
import net.iubris.aa.spheres.model.Volume;

import org.apache.commons.io.FileUtils;

public class SpheresVolume  {

	private Sphere[] spheres= null;
	private BoundingBox boundingBox = null;
	
	private final String inFileName;
	private final double howRandoms;
	private List<String> lines;
	private double maxVolumeFound = 0;
	private Sphere sphereWithMaxVolume;
//	private Double boundingBoxVolume;
	private Sphere sphereWithMaxRandomPoints;
	private Point[] randomPoints;
	private Volume volume;
	private List<Point> randomPointsFound;
	
	public SpheresVolume(String inFileName, double howRandoms) {
		this.inFileName = inFileName;
		this.howRandoms = howRandoms;
		this.randomPoints = new Point[(int) howRandoms];
		randomPointsFound = new ArrayList<Point>();
	}

	protected void parseInFile() throws IOException {
		this.lines = FileUtils.readLines( new File( this.inFileName ) );
	}
	
	protected void initSpheresCollection() {
		int spheresNumber = Integer.parseInt(this.lines.get(0));
		this.spheres = new Sphere[spheresNumber];
		for (int l=1;l<=spheresNumber;l++) {
			String line = lines.get(l);
			String[] sphereCoordinatesLine = line.split(" ");
			double cx = Double.parseDouble( sphereCoordinatesLine[0] );
			double cy = Double.parseDouble( sphereCoordinatesLine[1] );
			double cz = Double.parseDouble( sphereCoordinatesLine[2] );
			double radius = Double.parseDouble(sphereCoordinatesLine[3]);
			
			Sphere sphere = new Sphere(cx, cy, cz, radius);
			spheres[l-1]=sphere;
			
			double volume = sphere.getVolume();
//			spheresVolumes[i]=volume;
					
			if (volume > maxVolumeFound ) {
				maxVolumeFound = volume;
				sphereWithMaxVolume = sphere;
			}
		}
	}
	
	protected void initBoundingBox() {
		
		Double boundingBoxXMin = null;
		Sphere xMinSphere = null;
		Double boundingBoxXMax = null;
		Sphere xMaxSphere = null;
		
		Double boundingBoxZMin = null;
		Sphere zMinSphere = null;
		Double boundingBoxZMax = null;
		Sphere zMaxSphere = null;
		
		Double boundingBoxYMin = null;
		Sphere yMinSphere = null;
		Double boundingBoxYMax = null;
		Sphere yMaxSphere = null;
		
				
		for (int i=0;i<spheres.length;i++) {
			
			Sphere sphere = spheres[i];
			double radius = sphere.getRadius();

			double cx = sphere.getX();
			double computedXLB = 	calculateLowerBound(cx,radius,boundingBoxXMin);
			if (boundingBoxXMin==null || computedXLB != boundingBoxXMin) {
				boundingBoxXMin = computedXLB;
				xMinSphere = sphere;
			}			
			double computedXHB = calculateHigherBound(cx,radius,boundingBoxXMax);
			if (boundingBoxXMax==null || computedXHB != boundingBoxXMax) {
				boundingBoxXMax = computedXHB;
				xMaxSphere = sphere;
			}
			
			double cz = sphere.getZ();
			double computedZLB = calculateLowerBound(cz,radius,boundingBoxZMin);
			if (boundingBoxZMin==null || computedZLB != boundingBoxZMin) {
				boundingBoxZMin = computedZLB;
				zMinSphere = sphere;
			}
			double computedZHB = calculateHigherBound(cz,radius,boundingBoxZMax);
			if (boundingBoxZMax==null || computedZHB != boundingBoxZMax) {
				boundingBoxZMax = computedZHB;
				zMaxSphere = sphere;
			}
			
			double cy = sphere.getY();
			double computedYLB = calculateLowerBound(cy,radius,boundingBoxYMin);
			if (boundingBoxYMin==null ||computedYLB != boundingBoxYMin) {
				boundingBoxYMin = computedYLB;
				yMinSphere = sphere;
			}
			double computedYHB = calculateHigherBound(cy,radius,boundingBoxYMax);
			if (boundingBoxYMax==null || computedYHB != boundingBoxYMax) {
				boundingBoxYMax = computedYHB;
				yMaxSphere = sphere;
			}
		}
		
		this.boundingBox = new BoundingBox(
				new DimensionBounds(boundingBoxXMin, boundingBoxXMax),
				new DimensionBounds(boundingBoxYMin, boundingBoxYMax),
				new DimensionBounds(boundingBoxZMin, boundingBoxZMax),
				xMinSphere, xMaxSphere,
				yMinSphere, yMaxSphere,
				zMinSphere, zMaxSphere
				);
	
		System.out.println(boundingBox);
	}
	private static double calculateHigherBound(double coordinate, double radius, Double actualMax) {
		double currentMax = coordinate+radius;
		if (actualMax==null)
			return currentMax;
		if (currentMax>actualMax)
			return currentMax;
		return actualMax;
	}
	private static double calculateLowerBound(double coordinate,double radius, Double actualMin) {
		double currentMin = coordinate-radius;
		if (actualMin==null)
			return currentMin;
		// currentMin and actualMin are in different quadrants
		if (currentMin<0 && actualMin>0) {
			return currentMin;
		}
		// currentMin and actualMin are in different quadrants
		if (currentMin>0 && actualMin<0) {
			return actualMin;
		}
		// currentMin and actualMin are in same quadrants
		if (currentMin<actualMin) {
			return currentMin;
		}
		// currentMin and actualMin are in different quadrants
		// and actualMin < currentMin
		return actualMin;
	}
	
	/*
	private static RandomPoint buildRandomPoint(double random, BoundingBox boundingBox) {
		double xr = boundingBox.getXDimension().getMin()
				+ random*((boundingBox.getXDimension().getMax()-boundingBox.getXDimension().getMin())+1);
		double yr = boundingBox.getYDimension().getMin()
				+ random*((boundingBox.getYDimension().getMax()-boundingBox.getYDimension().getMin())+1);
		double zr = boundingBox.getZDimension().getMin()
				+ random*((boundingBox.getZDimension().getMax()-boundingBox.getZDimension().getMin())+1);
		return new RandomPoint(xr, yr, zr);		
	}*/
	private static Point buildRandomPoint(double randomX, double randomY, double randomZ, BoundingBox boundingBox) {
//		double xr = boundingBox.getXDimension().getMin()
//				+ randomX*((boundingBox.getXDimension().getMax()-boundingBox.getXDimension().getMin())+1);
		double xr = boundingBox.getxDimensionBounds().getMin()+randomX*boundingBox.getWidth();
//		double yr = boundingBox.getYDimension().getMin()
//				+ randomY*((boundingBox.getYDimension().getMax()-boundingBox.getYDimension().getMin())+1);
//		double zr = boundingBox.getZDimension().getMin()
//				+ randomZ*((boundingBox.getZDimension().getMax()-boundingBox.getZDimension().getMin())+1);
		double yr = boundingBox.getYDimensionBounds().getMin()+randomY*boundingBox.getDepth();
		double zr = boundingBox.getZDimensionBounds().getMin()+randomZ*boundingBox.getHeight();
		
		Point point = new Point(xr, yr, zr);
//		System.out.println(point);

		return point;
	}

	protected void calculateVolume() {
			
		Random randomGenerator = new Random();
			
		int totalPointsFound = 0;
		
		int howSpheres = spheres.length;
//		int[] spheresContaining = new int[howSpheres];
		for (int r=0;r<howRandoms;r++) {
						
//			RandomPoint rp = buildRandomPoint(randomGenerator.nextDouble(),this.boundingBox);
			Point rp = buildRandomPoint(randomGenerator.nextDouble(),
					randomGenerator.nextDouble(),
					randomGenerator.nextDouble(),
					this.boundingBox);
			randomPoints[r]=rp;
//			System.out.println("adding "+r);
			
			
			for (int i=0;i<howSpheres;i++) {
				Sphere s = spheres[i];

				if (s.contains(rp)) {
					int pointsWithinCurrentSphere = s.incrementFoundPoints();
					totalPointsFound++;

					// just for store sphere having max points
					if (sphereWithMaxRandomPoints!=s) {
						if (pointsWithinCurrentSphere==totalPointsFound) {
							sphereWithMaxRandomPoints = s;
						}
					}
					
					// store all random points found
					randomPointsFound.add(rp);

					break;
				}
			}
			//howSpheres--;

			/*for (Sphere s: spheres) {
				if (rp.isInSphere(s)) {
//					int actual = s.incrementContainedRandomPoints();						
//					if (actual>maxRandomPointsFoundInAnySphere) {
//						maxRandomPointsFoundInAnySphere = actual;
//						sphereWithMaxRandomPoints = s;
//					}
					s.incrementContainedRandomPoints();
					randomPointsFoundInAnySphere++;
					continue;
				}
//				continue;
			}*/
		}
//		System.out.println("random points generated are: "+randomPoints.length);
		
		System.out.println("Punti random massimi trovati all'interno delle sfere: "+totalPointsFound+"\n"
					+"di cui "+sphereWithMaxVolume.getContainedRandomPoints()
					+" nella sfera maggiore: ["+sphereWithMaxVolume+"].\n"
				+"La sfera contenente più punti ("+sphereWithMaxRandomPoints.getContainedRandomPoints()
					+") è: ["+sphereWithMaxRandomPoints+"],\n");
		System.out.println("Percentuale: "+totalPointsFound
				+"/"+howRandoms+" = "+totalPointsFound*100.0f/howRandoms+"%" );
	
		double volumeTotalMontecarlo = ( (0.0+totalPointsFound) / (0.0+howRandoms) )*boundingBox.getVolume();
		
		double volumeTotal = 0;
		for (Sphere s: spheres) {
			volumeTotal += s.getVolume();
		}
		System.out.println("Volume totale di " +spheres.length+" sfere: "+volumeTotal);
		System.out.println("Volume totale by Montecarlo: ("
				+totalPointsFound+"/"+howRandoms+")*"+boundingBox.getVolume()
				+" = "
				+volumeTotalMontecarlo);
		
		this.volume =  new Volume(volumeTotal, volumeTotalMontecarlo,howRandoms);
	}
	

	
	
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	public Sphere[] getSpheres() {
		return spheres;
	}
	public Point[] getRandomPoints() {
		return randomPoints;
	}
	public Volume getVolume() {
		return volume;
	}
	public List<Point> getRandomPointsFound() {
		return randomPointsFound;
	}
	
}
