package transforms;

public class Vec2D extends VSE<Vec2D> {
	public double x, y;

	/**
	 * Vytvoreni instance 2D vektoru (0,0)
	 */
	public Vec2D() {
		x = y = 0.0f;
	}

	/**
	 * Vytvoreni instance 2D vektoru (x,y)
	 * 
	 * @param ax
	 *            souradnice x
	 * @param ay
	 *            souradnice y
	 */
	public Vec2D(double ax, double ay) {
		x = ax;
		y = ay;
	}

	/**
	 * Vytvoreni instance 2D vektoru (x,y)
	 * 
	 * @param vec
	 *            souradnice (x,y)
	 */
	public Vec2D(Vec2D vec) {
		x = vec.x;
		y = vec.y;
	}

	/**
	 * Pricteni vektoru.
	 * 
	 * @param rhs
	 *            vektor (x,y)
	 * @return nova instance Vec2D
	 */
	public Vec2D add(Vec2D rhs) {
		return new Vec2D(x + rhs.x, y + rhs.y);
	}

	/**
	 * Nasobeni skalarem
	 * 
	 * @param rhs
	 *            skalar
	 * @return nova instance Vec2D
	 */
	public Vec2D mul(double rhs) {
		return new Vec2D(x * rhs, y * rhs);
	}

	/**
	 * Nasobeni vektorem
	 * 
	 * @param rhs
	 *            vektor (x,y)
	 * @return nova instance Vec2D
	 */
	public Vec2D mul(Vec2D rhs) {
		return new Vec2D(x * rhs.x, y * rhs.y);
	}

	/**
	 * Skalarni soucin vektoru
	 * 
	 * @param rhs
	 *            vektor (x,y)
	 * @return nova instance Vec2D
	 */
	public double dot(Vec2D rhs) {
		return x * rhs.x + y * rhs.y;
	}

	/**
	 * Normalizace vektoru
	 * 
	 * @return nova instance Vec2D
	 */
	public Vec2D normalized() {
		double l = length();
		if (l == 0.0f)
			return null;
		return new Vec2D(x / l, y / l);
	}

	/**
	 * Velikost vektoru
	 * 
	 * @return velikost
	 */
	public double length() {
		return (double) Math.sqrt((double) (x * x + y * y));
	}

}
