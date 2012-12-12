package transforms;

/**
 * trida pro praci s vektory ve 3D
 */

public class Vec3D extends VSE<Vec3D> {
	public double x, y, z;

	/**
	 * Vytvoreni instance 3D vektoru (0,0,0)
	 */
	public Vec3D() {
		x = y = z = 0.0f;
	}

	/**
	 * Vytvoreni instance 3D vektoru (x,y,z)
	 * 
	 * @param ax
	 *            souradnice x
	 * @param ay
	 *            souradnice y
	 * @param az
	 *            souradnice z
	 */
	public Vec3D(double ax, double ay, double az) {
		x = ax;
		y = ay;
		z = az;
	}

	/**
	 * Vytvoreni instance 3D vektoru (x,y,z)
	 * 
	 * @param avec
	 *            souradnice (x,y,z)
	 */
	public Vec3D(Vec3D vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}

	/**
	 * Vytvoreni instance 3D vektoru (x,y,z)
	 * 
	 * @param point
	 *            souradnice (x,y,z,w)
	 */
	public Vec3D(Point3D point) {
		x = point.x;
		y = point.y;
		z = point.z;
	}

	/**
	 * Pricteni vektoru
	 * 
	 * @param rhs
	 *            vektor (x,y,z)
	 * @return nova instance Vec3D
	 */
	public Vec3D add(Vec3D rhs) {
		return new Vec3D(x + rhs.x, y + rhs.y, z + rhs.z);
	}

	/**
	 * Nasobeni skalarem
	 * 
	 * @param rhs
	 *            skalar
	 * @return nova instance Vec3D
	 */
	public Vec3D mul(double rhs) {
		return new Vec3D(x * rhs, y * rhs, z * rhs);
	}

	
	/**
	 * Nasobeni matici zprava
	 * 
	 * @param rhs
	 *            matice 3x3
	 * @return nova instance Vec3D
	 */
	public Vec3D mul(Mat3 rhs) {
		Vec3D res = new Vec3D();
		res.x = rhs.mat[0][0] * x + rhs.mat[1][0] * y + rhs.mat[2][0] * z;
		res.y = rhs.mat[0][1] * x + rhs.mat[1][1] * y + rhs.mat[2][1] * z;
		res.z = rhs.mat[0][2] * x + rhs.mat[1][2] * y + rhs.mat[2][2] * z;
		return res;
	}

	/**
	 * Transformace vektoru quaternionem
	 * 
	 * @param q
	 *            kvaternion
	 * @return nova instance Vec3D
	 */
	public Vec3D mul(Quat q) {
		Quat p=new Quat(0,x,y,z);
		p=q.mulR(p).mulR(q.inv());
		Vec3D oVec = new Vec3D((float)(p.i),(float)(p.j),(float)(p.k));
		return oVec;
	}
	
	
	/**
	 * Nasobeni vektorem po slozkach
	 * 
	 * @param rhs
	 *            vektor (x,y,z)
	 * @return nova instance Vec3D
	 */
	public Vec3D mul(Vec3D rhs) {
		return new Vec3D(x * rhs.x, y * rhs.y, z * rhs.z);
	}

	/**
	 * Skalarni soucin vektoru
	 * 
	 * @param rhs
	 *            vektor (x,y,z)
	 * @return vysledek v plovouci desetinne carce
	 */
	public double dot(Vec3D rhs) {
		return x * rhs.x + y * rhs.y + z * rhs.z;
	}

	/**
	 * Vektorovy soucin vektoru
	 * 
	 * @param rhs
	 *            vektor (x,y,z)
	 * @return nova instance Vec3D
	 */
	public Vec3D cross(Vec3D rhs) {
		return new Vec3D(y * rhs.z - z * rhs.y, z * rhs.x - x * rhs.z, x
				* rhs.y - y * rhs.x);
	}

	/**
	 * Normalizace vektoru
	 * 
	 * @return nova instance Vec3D
	 */
	public Vec3D normalized() {
		double len = length();
		if (len == 0.0f)
			return new Vec3D(0,0,0);
		return new Vec3D(x / len, y / len, z / len);
	}

	/**
	 * Velikost vektoru
	 * 
	 * @return velikost
	 */
	public double length() {
		return (double) Math.sqrt((double) (x * x + y * y + z * z));
	}

}
