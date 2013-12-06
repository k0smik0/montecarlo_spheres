package net.iubris.aa.spheres;

//import java.io.BufferedReader;
//import java.io.InputStreamReader;
import java.io.*;
import java.util.Random;
import java.util.Arrays;

public class SpheresMain {

   public static void main(String[] args) {

  	Sphere[] spheres= null;
 	double[] spheresVolumes=null;
 	Double[] xs = null;
 	Double[] ys = null;
 	Double[] zs = null;
 	Double[] radiuses = null;
 	Double[][] toSort = null;
 	
	try {  
		File file = new File(args[0]);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		line = br.readLine(); 

		int spheresNumber = Integer.parseInt(line);
	    System.out.println("# spheres = "+spheresNumber);
	   	spheres = new Sphere[spheresNumber];
	   	spheresVolumes = new double[spheresNumber];
	   	xs = new Double[spheresNumber];
	   	ys = new Double[spheresNumber];
	   	zs = new Double[spheresNumber];
	   	radiuses = new Double[spheresNumber];
	   	
	   	toSort = new Double[4][spheresNumber];
	     
	   	int i=0;
	   	for (int l=1;l<1001;l++) {
	   		line = br.readLine();
//	   	}
//	    while ((line = br.readLine()) != null) {
	//       	 System.out.print(line);
	    	String[] xyzr = line.split(" ");
	    	double cx = Double.parseDouble(xyzr[0]);
	    	double cy = Double.parseDouble(xyzr[1]);
	    	double cz = Double.parseDouble(xyzr[2]);
	    	double radius = Double.parseDouble(xyzr[3]);
	    	
	    	xs[i] = cx;
	    	ys[i] = cy;
	    	zs[i] = cz;
	    	radiuses[i] = radius;
	    	
	    	Sphere s = new Sphere(cx, cy, cz, radius);
//	    	System.out.println(" "+s.getVolume()+" ");
	    	spheres[i]=s;
	    	spheresVolumes[i]=s.getVolume();
	    	i++;
	    }
	    
		toSort[0]=xs;
	    toSort[1]=ys;
	    toSort[2]=zs;
	    toSort[3]=radiuses;
	    
	} catch (IOException e) {
		System.out.println(""+e);
    }
	
	for (int i=0;i<4;i++) {
		Arrays.sort( toSort[i] );
	}
	
	double xsMax = xs[xs.length]+0;
	double xsMin = xs[0];
	double ysMax = ys[ys.length];
	double ysMin = ys[0];
	double zsMax = zs[zs.length];
	double zsMin = zs[0];
	double Vbb = (xsMax-xsMin)
			* (ysMax-ysMin)
			* (zsMax-zsMin);
	
	Random r = new Random();
	int howRandoms = 1000;
	for (int i=0;i<howRandoms;i++) {
		double xr = r.nextDouble()*xsMax;
		double yr = r.nextDouble()*ysMax;
		double zr = r.nextDouble()*zsMax;
		
	}
	
	

	double volumeTotal = 0;
	if (spheres!=null) {
		for (Sphere s: spheres) {
			volumeTotal += s.getVolume();
		}
	}
	Arrays.sort(spheresVolumes);

	System.out.println("Volume totale: "+volumeTotal+" "+spheresVolumes[spheresVolumes.length-1]);

   }
}
