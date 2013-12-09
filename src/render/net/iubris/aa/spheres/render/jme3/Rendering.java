package net.iubris.aa.spheres.render.jme3;

import com.jme3.app.SimpleApplication;

public class Rendering extends SimpleApplication {

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
