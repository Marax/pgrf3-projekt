package objects;

import transforms.Vec3D;

public class Cyl1 implements IObject {

	@Override
	public Vec3D compute(double x, double y) {
		double s = Math.PI * 0.5 - Math.PI * y *2;
		double t = 2 * Math.PI * x;
		
		double r = (1+Math.max(Math.sin(t),0))*0.5*t;
		double z = 3-t;
		
		return new Vec3D(
				t*Math.cos(s),
				t*Math.sin(s),
				2*Math.sin(t));
	}

}
