package net.iubris.aa.spheres;

public class Sphere {

   private final double cx;
   private final double cy;
   private final double cz;
   private final double radius;

   private double volume;

   public Sphere(double cx, double cy, double cz, double radius) {
      this.cx = cx;
      this.cy = cy;
      this.cz = cz;
      this.radius = radius;

      this.volume = (4.0 / 3.0) 
    		  * Math.PI 
//    		  * Math.pow(radius,3);
      		* radius*radius*radius;
   }

   public double getVolume() {
      return volume;
   }
}