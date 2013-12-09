package net.iubris.aa.spheres;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import net.iubris.aa.spheres.model.BoundingBox;
import net.iubris.aa.spheres.model.DimensionBounds;
import net.iubris.aa.spheres.model.RandomPoint;
import net.iubris.aa.spheres.model.Sphere;

import org.apache.commons.io.FileUtils;

public class SpheresVolume  {


	private Sphere[] spheres= null;
	private BoundingBox boundingBox = null;
	
	private final String inFileName;
	private final double howRandoms;
	private List<String> lines;
	private double maxVolumeFound = 0;
	private Sphere sphereWithMaxVolume;
	private Double boundingBoxVolume;
	private Sphere sphereWithMaxRandomPoints;
	
	/*public static void run(String inFileName) {		
		double howRandoms = 1e5;
		SpheresVolume sv = new SpheresVolume(inFileName,howRandoms);
		try {
			sv.parseInFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sv.initSpheresCollection();
		sv.initBoundingBox();
		sv.calculateVolume();
		
		System.out.println("");
	}
	
	public static void main(String[] args) {
		run("ds/sfere1.in");
		run("ds/sfere2.in");
		run("ds/sfere3.in");
		
		// rendering section
//		sv.start();
	}*/
	
	public SpheresVolume(String inFileName, double howRandoms) {
		this.inFileName = inFileName;
		this.howRandoms = howRandoms;
	}

	protected void parseInFile() throws IOException {
		this.lines = FileUtils.readLines( new File( this.inFileName ) );
	}
	
	protected void initSpheresCollection() {
		int spheresNumber = Integer.parseInt(this.lines.get(0));
		System.out.println("# spheres = "+spheresNumber);
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
		Double boundingBoxXMax = null;
		
		Double boundingBoxYMin = null;
		Double boundingBoxYMax = null;
		
		Double boundingBoxZMin = null;
		Double boundingBoxZMax = null;
				
		for (int s=0;s<spheres.length;s++) {
			
			Sphere sphere = spheres[s];
			double cx = sphere.getX();
			double cy = sphere.getY();
			double cz = sphere.getZ();
			double radius = sphere.getRadius();
					
			boundingBoxXMax = calculateHigherBound(cx,radius,boundingBoxXMax);
			boundingBoxXMin = calculateLowerBound(cx,radius,boundingBoxXMin);
			
			boundingBoxYMax = calculateHigherBound(cy,radius,boundingBoxYMax);
			boundingBoxYMin = calculateLowerBound(cy,radius,boundingBoxYMin);
			
			boundingBoxZMax = calculateHigherBound(cz,radius,boundingBoxZMax);
			boundingBoxZMin = calculateLowerBound(cz,radius,boundingBoxZMin);
		}
		
		this.boundingBox = new BoundingBox(
				new DimensionBounds(boundingBoxXMin, boundingBoxXMax),
				new DimensionBounds(boundingBoxYMin, boundingBoxYMax),
				new DimensionBounds(boundingBoxZMin, boundingBoxZMax)
				);
	
		this.boundingBoxVolume = boundingBox.getVolume();
		System.out.println("bounding box: "+boundingBox+" with volume: "+this.boundingBoxVolume);
	}
		
	private static RandomPoint buildRandomPoint(double random, BoundingBox boundingBox) {
		double xr = boundingBox.getxDimension().getMin() 
				+ random*((boundingBox.getxDimension().getMax()-boundingBox.getxDimension().getMin())+1);
		double yr = boundingBox.getyDimension().getMin() 
				+ random*((boundingBox.getyDimension().getMax()-boundingBox.getyDimension().getMin())+1);
		double zr = boundingBox.getzDimension().getMin() 
				+ random*((boundingBox.getzDimension().getMax()-boundingBox.getzDimension().getMin())+1);

		return new RandomPoint(xr, yr, zr);		
	}

	protected void calculateVolume() {
			
		Random randomGenerator = new Random();
			
		int maxRandomPointsFoundWithinSphereBetweenAllSpheres = 0;
					
		for (int r=0;r<howRandoms;r++) {
						
			RandomPoint rp = buildRandomPoint(randomGenerator.nextDouble(),this.boundingBox);
			
			for (Sphere s: spheres) {
				if (rp.isInSphere(s)) {
					int actual = s.incrementContainedRandomPoints();						
					if (actual>maxRandomPointsFoundWithinSphereBetweenAllSpheres) {
						maxRandomPointsFoundWithinSphereBetweenAllSpheres = actual;
						sphereWithMaxRandomPoints = s;
					}
				}					
				continue;
			}
		}
		
		System.out.println("Sfera maggiore: "+sphereWithMaxVolume);
		System.out.println("Punti random nella sfera maggiore: "+sphereWithMaxVolume.getContainedRandomPoints());
		
		
		System.out.println("Max random points found within one sphere: "
				+maxRandomPointsFoundWithinSphereBetweenAllSpheres
				+", in sphere: "+sphereWithMaxRandomPoints);
		System.out.println(maxRandomPointsFoundWithinSphereBetweenAllSpheres
				+"/"+howRandoms+"="
				+maxRandomPointsFoundWithinSphereBetweenAllSpheres*100.0f/howRandoms+"%" );
	
		double volumeTotalMontecarlo = (
				(0.0+maxRandomPointsFoundWithinSphereBetweenAllSpheres) / (0.0+howRandoms))*boundingBoxVolume;
		
		double volumeTotal = 0;
		for (Sphere s: spheres) {
			volumeTotal += s.getVolume();
		}
		System.out.println("Volume totale di " +spheres.length+" sfere: "+volumeTotal);
		System.out.println("Volume totale by Montecarlo: ("
				+maxRandomPointsFoundWithinSphereBetweenAllSpheres+"/"+howRandoms+")*"+boundingBoxVolume
				+" = "
				+volumeTotalMontecarlo);
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
	
}
