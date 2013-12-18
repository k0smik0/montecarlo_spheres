package net.iubris.aa.spheres.volume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import net.iubris.aa.spheres.model.BoundingBox;
import net.iubris.aa.spheres.model.DimensionBounds;
import net.iubris.aa.spheres.model.Point;
import net.iubris.aa.spheres.model.Sphere;
import net.iubris.aa.spheres.model.Volume;

public abstract class AbstractSpheresVolume /*implements ISpheresVolume*/ {
	
	protected Sphere[] spheres= null;
	protected BoundingBox boundingBox = null;
	
	protected String inFileName;
	protected double randomPointsUsed;
	protected final Random randomGenerator;	
	protected List<String> lines;
	protected double maxVolumeFound = 0;
	protected Sphere sphereWithMaxVolume;
	protected Sphere sphereWithMaxRandomPoints;
	protected List<Point> randomPointsFound;
	protected Point[] randomPoints;
	protected int randomPointsFoundedNumber = 0;
	
	protected Volume volume;
	private String sphereTopology;
	
	
	public AbstractSpheresVolume(String inFileName, double randomPointsUsed) {
		this.inFileName = inFileName;
		this.randomPointsUsed = randomPointsUsed;
		this.randomPoints = new Point[(int) randomPointsUsed];
		this.randomPointsFound = new ArrayList<Point>();
		this.randomGenerator = new Random();
		
		this.sphereTopology = "";
	}
	public AbstractSpheresVolume(String inFileName, double randomPointsUsed, String sphereTopology) {
		this(inFileName,randomPointsUsed);
		this.sphereTopology = " "+sphereTopology;
	}
	
	public void init() {
		initSpheresCollection();
		initBoundingBox();
		initRandomPoints();
	}
	
	public void parseInFile() throws IOException {
		this.lines = FileUtils.readLines( new File( this.inFileName ) );
		System.out.print("** "+inFileName+": ");
	}
	
	protected void initSpheresCollection() {
		int spheresNumber = Integer.parseInt(this.lines.get(0));
		this.spheres = new Sphere[spheresNumber];
		System.out.println(spheres.length+ " sfere"+sphereTopology+", "+randomPointsUsed+" punti **");
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
	}
	private double calculateHigherBound(double coordinate, double radius, Double actualMax) {
		double currentMax = coordinate+radius;
		if (actualMax==null)
			return currentMax;
		if (currentMax>actualMax)
			return currentMax;
		return actualMax;
	}
	private double calculateLowerBound(double coordinate,double radius, Double actualMin) {
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
	
	protected void initRandomPoints() {
		for (int r=0;r<randomPointsUsed;r++) {
			Point rp = buildRandomPoint(randomGenerator.nextDouble(),
					randomGenerator.nextDouble(),
					randomGenerator.nextDouble(),
					this.boundingBox);
			randomPoints[r]=rp;
		}
	}
	private Point buildRandomPoint(double randomX, double randomY, double randomZ, BoundingBox boundingBox) {
		double xr = boundingBox.getxDimensionBounds().getMin()+randomX*boundingBox.getWidth();
		double yr = boundingBox.getYDimensionBounds().getMin()+randomY*boundingBox.getDepth();
		double zr = boundingBox.getZDimensionBounds().getMin()+randomZ*boundingBox.getHeight();
		
		Point point = new Point(xr, yr, zr);
		return point;
	}
	
//	protected void setTotalFoundPoints() {}
	
	/*protected void checkPointsSequential() {
		int howSpheres = spheres.length;		
		for (int i=0;i<randomPointsUsed;i++) {
			Point rp = randomPoints[i];
			for (int j=0;j<howSpheres;j++) {
				Sphere s = spheres[j];
				if (s.contains(rp)) {
					randomPointsFoundedNumber++;					
					// minimal code end here. below is just for info and rendering
//					storeMoreInfo(s,rp);
					break;
				}
			}
		}
	}*/
	protected void storeMoreInfo(Sphere s, Point p) {
		int pointsWithinCurrentSphere = s.incrementFoundPoints();
		
		// store which sphere has max points
		if (sphereWithMaxRandomPoints!=s) {
			if (pointsWithinCurrentSphere==randomPointsFoundedNumber) {
				sphereWithMaxRandomPoints = s;
			}
		}					
		// store all random points founded, for later uses
		randomPointsFound.add(p);
	}
	
	protected void calculateVolume() {
		double volumeTotalMontecarlo = ( (0.0+randomPointsFoundedNumber) / (0.0+randomPointsUsed) )*boundingBox.getVolume();
		double volumeTotal = 0;
		for (Sphere s: spheres) {
			volumeTotal += s.getVolume();
		}
			
//		printSomeInfoDuringCalculating(randomPointsFoundedNumber,volumeTotal,volumeTotalMontecarlo);		
		
		this.volume =  new Volume(volumeTotal, volumeTotalMontecarlo,randomPointsUsed);
	}
	
	protected void printSomeInfoDuringCalculating(int totalFoundPoints, double volumeTotalBySum,double volumeTotalByMontecarlo) {
		System.out.println("Punti random massimi trovati all'interno delle sfere: "+totalFoundPoints+"\n"
				+"di cui "+sphereWithMaxVolume.getContainedRandomPoints()
				+" nella sfera maggiore: ["+sphereWithMaxVolume+"].\n"
				+"La sfera contenente più punti ("+sphereWithMaxRandomPoints.getContainedRandomPoints()
				+") è: ["+sphereWithMaxRandomPoints+"]");
		System.out.println("Percentuale: "+totalFoundPoints
			+"/"+randomPointsUsed+" = "+totalFoundPoints*100.0f/randomPointsUsed+"%" );
	
		System.out.println("Volume totale di " +spheres.length+" sfere: "+volumeTotalBySum);
		System.out.println("Volume totale by Montecarlo: ("
			+totalFoundPoints+"/"+randomPointsUsed+")*"+boundingBox.getVolume()
			+" = "
			+volumeTotalByMontecarlo);		
	}
	
	
	public String getResult() {
		double percent = randomPointsFoundedNumber*100.0f/randomPointsUsed;
		Double boundingBoxVolume = boundingBox.getVolume();
		String r = "Punti trovati: "+randomPointsFoundedNumber
				+"/"+randomPointsUsed+" = "+percent+"%\n" ;
		r+="Volume BoundingBox: "+boundingBoxVolume+"\n"
				+"Volume Totale (somma dei volumi): "+volume.getVolumeTotal()+"\n"
				+"Volume con metodo Montecarlo: "+volume.getVolumeMontecarlo()+" ("+percent+"% * "+boundingBoxVolume+")";
		return r;
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
	
	public abstract void calculate();
}
