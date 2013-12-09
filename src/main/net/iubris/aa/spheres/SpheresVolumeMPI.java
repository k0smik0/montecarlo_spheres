package net.iubris.aa.spheres;

//import java.io.BufferedReader;
//import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;







import net.iubris.aa.spheres.model.BoundingBox;
import net.iubris.aa.spheres.model.DimensionBounds;
import net.iubris.aa.spheres.model.RandomPoint;
import net.iubris.aa.spheres.model.Sphere;

import org.apache.commons.io.FileUtils;

import com.jme3.app.SimpleApplication;

public class SpheresVolumeMPI extends SimpleApplication {


	private Sphere[] spheres= null;
	private BoundingBox boundingBox = null;
	
	private final String inFileName;
	private final double howRandoms;
	private List<String> lines;
	private double maxVolumeFound = 0;
	private Sphere sphereWithMaxVolume;
	private Double boundingBoxVolume;
	private Sphere sphereWithMaxRandomPoints;
	
	public static void main(String[] args) {
		String inFileName = "ds/sfere1.in";
		double howRandoms = 1e4;
		SpheresVolumeMPI sv = new SpheresVolumeMPI(inFileName,howRandoms);
		try {
			sv.parseInFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		MP
		sv.initSpheresCollection();
		sv.initBoundingBox();
		sv.calculateVolume();

		// rendering section
//		sv.start();
	}
	
	public SpheresVolumeMPI(String inFileName, double howRandoms) {
		this.inFileName = inFileName;
		this.howRandoms = howRandoms;
	}

	private void parseInFile() throws IOException {
		this.lines = FileUtils.readLines( new File( this.inFileName ) );
	}
	
	private void initSpheresCollection() {
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
	
	private void initBoundingBox() {
		
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

	private void calculateVolume() {
			
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
	
		//	double volumeTotal = Collections.max( Arrays.asList( spheresVolumes ));
//			double volumeTotal = maxVolumeFound;
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
	
	
	
	
	/*
	private void run() {
      try {
           init();
           render(); // render graphics
           Display.sync(70); // sync to fps
           Display.update(); // update the view/screen
      } catch (Exception e) {
          e.printStackTrace();
          System.exit(1);
      }
	}
      private void render() {
         glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
         glLoadIdentity();
         glTranslatef(0.0f, 0.0f, zTranslation);
         renderSphere(-2f, -0.5f, -1f);
         renderSphere(-1f, -0.5f, -2f);
         renderSphere(-0f, -0.5f, -3f);
         renderSphere(1f, -0.5f, -4f);
         renderSphere(2f, -0.5f, -5f);
    }
   private void renderSphere(float x, float y, float z) {
         glPushMatrix();
         glTranslatef(x, y, z);
         org.lwjgl.util.glu.Sphere s = new org.lwjgl.util.glu.Sphere();
         for (Sphere ms: spheres) {
         	s.draw(arg0, arg1, arg2);
         }
         s.draw(0.4f, 16, 16);
         glPopMatrix();
   }
   private void init() {
      createWindow();
      initGL();
  }

  private void createWindow() {
      try {
          Display.setDisplayMode(DISPLAY_MODE);
          Display.setTitle(WINDOW_TITLE);
          Display.create();
      } catch (LWJGLException e) {
          e.printStackTrace();
      }
  }

  private void initGL() {
      glClearDepth(1.0f); // clear depth buffer
      glEnable(GL_DEPTH_TEST); // Enables depth testing
      glDepthFunc(GL_LEQUAL); // sets the type of test to use for depth
      // testing
      glMatrixMode(GL_PROJECTION); // sets the matrix mode to project
      float fovy = 45.0f;
      float aspect = DISPLAY_MODE.getWidth()
      / (float) DISPLAY_MODE.getHeight();
      float zNear = 0.1f;
      float zFar = 100.0f;
      GLU.gluPerspective(fovy, aspect, zNear, zFar);
      glMatrixMode(GL_MODELVIEW);
      glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
  }
   
	*/
	
	
	// jme3 section

	@Override
	public void simpleInitApp() {
//		calculateVolume("ds/sfere1.in");
		
		
		/*
		
		Box b = new Box(1, 1, 1);		
		Geometry geom = new Geometry("Box", b);
//		Material mat = new Material();
//		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		geom.setMaterial(mat);
		rootNode.attachChild(geom);
		
		
		// A simple textured cube -- in good MIP map quality. 
	    Box cube1Mesh = new Box( 1f,1f,1f);
	    Geometry cube1Geo = new Geometry("My Textured Box", cube1Mesh);
	    cube1Geo.setLocalTranslation(new Vector3f(-3f,1.1f,0f));
	    Material cube1Mat = new Material();
//	    Material cube1Mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//	    Texture cube1Tex = assetManager.loadTexture("Interface/Logo/Monkey.jpg");
//	    cube1Mat.setTexture("ColorMap", cube1Tex);
	    cube1Geo.setMaterial(cube1Mat);
	    rootNode.attachChild(cube1Geo);
	 
	    
	    // A translucent/transparent texture, similar to a window frame. 
	    Box cube2Mesh = new Box( 1f,1f,0.01f);
	    Geometry cube2Geo = new Geometry("window frame", cube2Mesh);
	    Material cube2Mat = new Material();
//	    Material cube2Mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//	    cube2Mat.setTexture("ColorMap", assetManager.loadTexture("Textures/ColoredTex/Monkey.png"));
//	    cube2Mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
//	    cube2Geo.setQueueBucket(Bucket.Transparent);
	    cube2Geo.setMaterial(cube2Mat);
	    rootNode.attachChild(cube2Geo);
	 
	    // A bumpy rock with a shiny light effect.
	    com.jme3.scene.shape.Sphere sphereMesh = new com.jme3.scene.shape.Sphere(32,32, 2f);
	    Geometry sphereGeo = new Geometry("Shiny rock", sphereMesh);
	    sphereMesh.setTextureMode(com.jme3.scene.shape.Sphere.TextureMode.Projected); // better quality on spheres
	    Material sphereMat = new Material();
//	    TangentBinormalGenerator.generate(sphereMesh);           // for lighting effect
//	    Material sphereMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
////	    sphereMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
////	    sphereMat.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond_normal.png"));
//	    sphereMat.setBoolean("UseMaterialColors",true);    
//	    sphereMat.setColor("Diffuse",ColorRGBA.White);
//	    sphereMat.setColor("Specular",ColorRGBA.White);
//	    sphereMat.setFloat("Shininess", 64f);  // [0,128]
	    sphereGeo.setMaterial(sphereMat);
	    sphereGeo.setLocalTranslation(0,2,-2); // Move it a bit
	    sphereGeo.rotate(1.6f, 0, 0);          // Rotate it a bit
	    rootNode.attachChild(sphereGeo);
	 
	    // Must add a light to make the lit object visible! 
	    DirectionalLight sun = new DirectionalLight();
	    sun.setDirection(new Vector3f(1,0,-2).normalizeLocal());
	    sun.setColor(ColorRGBA.White);
	    rootNode.addLight(sun);
	    */
	}

}
