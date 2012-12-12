package transforms;

public class Mat4 extends VSE<Mat4> {
	public double mat[][] = new double[4][4];

	/**
	 * Vytvari nulovou matici 4x4
	 */
	public Mat4() {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				mat[i][j] = 0.0f;
	}

	/**
	 * Vytvari matici 4x4 ze ctyr ctyrslozkovych vektoru - po radcich
	 * 
	 * @param p1
	 *            vektor (M00, M01, M02, M03)
	 * @param p2
	 *            vektor (M10, M11, M12, M13)
	 * @param p3
	 *            vektor (M20, M21, M22, M23)
	 * @param p4
	 *            vektor (M30, M31, M32, M33)
	 */
	public Mat4(Point3D p1, Point3D p2, Point3D p3, Point3D p4) {
		mat[0][0] = p1.x;
		mat[0][1] = p1.y;
		mat[0][2] = p1.z;
		mat[0][3] = p1.w;
		mat[1][0] = p2.x;
		mat[1][1] = p2.y;
		mat[1][2] = p2.z;
		mat[1][3] = p2.w;
		mat[2][0] = p3.x;
		mat[2][1] = p3.y;
		mat[2][2] = p3.z;
		mat[2][3] = p3.w;
		mat[3][0] = p4.x;
		mat[3][1] = p4.y;
		mat[3][2] = p4.z;
		mat[3][3] = p4.w;
	}

	/**
	 * Vytvari matici 4x4
	 * 
	 * @param aMat
	 *            Matice 4x4
	 */
	public Mat4(Mat4 aMat) {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				mat[i][j] = aMat.mat[i][j];
	}

	/**
	 * Scitani matic 4x4
	 * 
	 * @param rhs
	 *            Matice 4x4
	 * @return nova instance Mat4
	 */
	public Mat4 add(Mat4 rhs) {
		Mat4 hlp = new Mat4();
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				hlp.mat[i][j] = mat[i][j] + rhs.mat[i][j];
		return hlp;
	}

	/**
	 * Nasobeni matice 4x4 skalarem
	 * 
	 * @param rhs
	 *            skalar
	 * @return nova instance Mat4
	 */
	public Mat4 mul(double rhs) {
		Mat4 hlp = new Mat4();
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				hlp.mat[i][j] = mat[i][j] * rhs;
		return hlp;
	}

	/**
	 * Nasobeni matic 4x4 zprava
	 * 
	 * @param rhs
	 *            matice 4x4
	 * @return nova instance Mat4
	 */
	public Mat4 mul(Mat4 rhs) {
		Mat4 hlp = new Mat4();
		double sum;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++) {
				sum = 0.0f;
				for (int k = 0; k < 4; k++)
					sum += mat[i][k] * rhs.mat[k][j];
				hlp.mat[i][j] = sum;
			}
		return hlp;
	}
}
