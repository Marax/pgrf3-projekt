package transforms;

public class Vec1D extends VSE<Vec1D> {
	public double x;

	/**
	 * Vytvoreni instance 1D vektoru (0)
	 */
	public Vec1D() {
		x = 0.0f;
	}

	/**
	 * Vytvoreni instance 1D vektoru (x)
	 * 
	 * @param ax
	 *            souradnice x
	 */
	public Vec1D(double ax) {
		x = ax;
	}

	/**
	 * Vytvoreni instance 1D vektoru (x)
	 * 
	 * @param vec
	 *            souradnice (x)
	 */
	public Vec1D(Vec1D vec) {
		x = vec.x;
	}

	/**
	 * Pricteni vektoru
	 * 
	 * @param rhs
	 *            vektor (x)
	 * @return nova instance Vec1D
	 */
	public Vec1D add(Vec1D rhs) {
		return new Vec1D(x + rhs.x);
	}

	/**
	 * Nasobeni skalarem
	 * 
	 * @param rhs
	 *            skalar
	 * @return nova instance Vec1D
	 */
	public Vec1D mul(double rhs) {
		return new Vec1D(x * rhs);
	}
}
