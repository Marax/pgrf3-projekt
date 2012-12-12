package objects;

import transforms.Vec3D;

public class Cyl2 implements IObject {

	@Override
	public Vec3D compute(double x, double y) {
		double s = Math.PI * 0.5 - Math.PI * y; 
		double t = 2 * Math.PI * x; 
		
		double r = Math.cos(t*.5)*Math.sin(s*3)+1;
		
		return new Vec3D(
				r*Math.cos(s) *2,
				r*Math.sin(s) *2,
				2*t);
	}

}
