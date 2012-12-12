package objects;

import transforms.Vec3D;

public class Snake implements IObject {

	@Override
	public Vec3D compute(double x, double y) {
		double s = Math.PI * 0.5 - Math.PI * y;
		double t = 2 * Math.PI * x;

		return new Vec3D(2 * (1 - s) * (3 + Math.cos(t))
				* Math.cos(2 * Math.PI * s), 4 * (1 - s) * (3 + Math.cos(t))
				* Math.sin(2 * Math.PI * s), 10 * s + (1 - s) * Math.sin(t));
	}

}
