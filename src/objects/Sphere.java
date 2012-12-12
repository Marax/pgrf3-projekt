package objects;

import transforms.Vec3D;

public class Sphere implements IObject {

	@Override
	public Vec3D compute(double x, double y) {
		double s = Math.PI * 0.5 - Math.PI * y; // zenit
		double t = 2* Math.PI * x; // azimiut
		double r = 20;
		
		return new Vec3D(
				Math.cos(t) * Math.cos(s) * r,
				Math.sin(t) * Math.cos(s) * r, 
				Math.sin(s) * r);
	}

}
