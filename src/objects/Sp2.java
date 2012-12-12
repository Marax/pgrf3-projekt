package objects;

import transforms.Vec3D;

public class Sp2 implements IObject {

	@Override
	public Vec3D compute(double x, double y) {
	/*	double s = Math.PI * 0.5 - Math.PI * y *2;
		double t = 2 * Math.PI * x *2; 
		double r = 1+Math.sin(4*t);
		r *= 8; 
		
		return new Vec3D(
				Math.sin(t) * Math.cos(s) * r ,
				Math.sin(t) * Math.sin(s) * r , 
				Math.cos(s) * r );
	*/
		double s = Math.PI * y *2;
		double t = Math.PI * x; 
		double r = 1+Math.sin(4*t);
		r *= 8; 
		
		return new Vec3D(
				3+Math.cos(4*s) ,
				t*Math.sin(s) , 
				s );
	}

}
