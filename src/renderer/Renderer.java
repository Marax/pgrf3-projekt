/**
 * 
 */
package renderer;

import java.util.Vector;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec1D;

public class Renderer {
	/**
	 * Pole vrcholu, nesmi byt null.
	 */
	public Vector vertices;
	/**
	 * Pole indexu, nesmi byt null.
	 */
	public Vector<Integer> indices;

	/**
	 * Odkaz na implementaci interface vertexoveho programu. Nesmi byt null.
	 */
	public VertexShader vertexShader;
	/**
	 * Odkaz na implementaci interface pixeloveho programu. Nesmi byt null.
	 */
	public PixelShader pixelShader;
	/**
	 * Odkaz na implementaci interface testu alfa kanalu pixelu. Muze byt null.
	 */
	public AlphaTest alphaTest;
	/**
	 * Odkaz na implementaci interface testu souradnice z pixelu. Muze byt null.
	 */
	public ZTest zTest;
	/**
	 * Odkaz na implementaci interface michani barvy pixelu. Muze byt null.
	 */
	public Blend blend;

	/**
	 * Dvourozmerna textura pro barevny vystup, nesmi byt null.
	 */
	public Texture2D<Col> imageBuffer;
	/**
	 * Dvourozmerna textura pro vystup souradnice z, nesmi byt null.
	 */
	public Texture2D<Vec1D> zBuffer;

	public Line line;

	public boolean wireframe = true, zWrite = true, colorWrite = true;

	/**
	 * Interpretuje pole indexu jako seznam trojuhelniku. Pro kazdy sestaveny
	 * trojuhelnik vola metodu shadeVertices().
	 * 
	 * @param startIndex
	 *            Index prvniho zpracovaneho indexu.
	 * @param primitiveCount
	 *            Pocet geometrickych primitiv (trojuhelniku) k vykresleni.
	 */
	public void renderList(int startIndex, int primitiveCount) {
		for (int i = 0; i < primitiveCount; i++) {
			Object v1, v2, v3;
			v1 = vertices.get(indices.get(startIndex + 3 * i).intValue());
			v2 = vertices.get(indices.get(startIndex + 3 * i + 1).intValue());
			v3 = vertices.get(indices.get(startIndex + 3 * i + 2).intValue());
			shadeVertices(v1, v2, v3);
		}
	}

	/**
	 * Interpretuje pole indexu jako pas trojuhelniku. Pro kazdy sestaveny
	 * trojuhelnik vola metodu shadeVertices().
	 * 
	 * @param startIndex
	 *            Index prvniho zpracovaneho indexu.
	 * @param primitiveCount
	 *            Pocet geometrickych primitiv (trojuhelniku) k vykresleni.
	 */
	public void renderStrip(int startIndex, int primitiveCount) {

		for(int i = startIndex; i < (startIndex + primitiveCount); i++){
			
			if(indices.get(i + 2) != null && vertices.get(indices.get(i + 2).intValue()) != null){
				
				Object v1, v2, v3;
				v1 = vertices.get(indices.get(i).intValue());
				v2 = vertices.get(indices.get(i + 1).intValue());
				v3 = vertices.get(indices.get(i + 2).intValue());
				shadeVertices(v1, v2, v3);
				
			}
		}
	}

	/**
	 * Interpretuje pole indexu jako vejir trojuhelniku. Pro kazdy sestaveny
	 * trojuhelnik vola metodu shadeVertices().
	 * 
	 * @param startIndex
	 *            Index prvniho zpracovaneho indexu.
	 * @param primitiveCount
	 *            Pocet geometrickych primitiv (trojuhelniku) k vykresleni.
	 */
	public void renderFan(int startIndex, int primitiveCount) {
		
		for(int i = startIndex; i < (startIndex + primitiveCount); i++){
			
			if(indices.get(i + 2) != null && vertices.get(indices.get(i + 2).intValue()) != null){
				
				Object v1, v2, v3;
				v1 = vertices.get(indices.get(startIndex).intValue());
				v2 = vertices.get(indices.get(i + 1).intValue());
				v3 = vertices.get(indices.get(i + 2).intValue());
				shadeVertices(v1, v2, v3);
				
			}
		}
	}

	/**
	 * Pro kazdy vrchol trojuhelnika provede nastaveny vertex shader.
	 * 
	 * @param av1,
	 *            av2, av3 Vrcholy trojuhelnika v instancich nactenych ze
	 *            seznamu vrcholu.
	 */
	void shadeVertices(Object av1, Object av2, Object av3) {
		VVector v1, v2, v3;
		v1 = vertexShader.process(av1);
		v2 = vertexShader.process(av2);
		v3 = vertexShader.process(av3);
		clipHomo(v1, v2, v3);
	}

	/**
	 * Oreze trojuhelnik tak, aby souradnice w vsech vrcholu byly vetsi nez
	 * nejaka kladna konstanta blizka nule.
	 * 
	 * @param av1,
	 *            av2, av3 Vrcholy trojuhelnika tak, jak je vratil vertex
	 *            shader.
	 */
	void clipHomo(VVector av1, VVector av2, VVector av3) {
		//orezavat pomoci W 
		//pøed kamerou je -z
		
		//TODO - setøídit podle W a udìlat nové podmínky
		
		//TODO render strip... , cpilhomo, rasterize triangle
		
		
		
		
		Point3D p1 = (Point3D) av1.data.get(0).clone();
		Point3D p2 = (Point3D) av2.data.get(0).clone();
		Point3D p3 = (Point3D) av3.data.get(0).clone();
		
		final float wmin = 0.1f;
		Point3D pom = null;

		if(p1.w < p2.w){
			pom = p1;
			p1 = p2;
			p2 = pom;
		}
		if(p1.w < p3.w){
			pom = p1;
			p1 = p3;
			p3 = pom;
		}
		if(p2.w < p3.w){
			pom = p2;
			p2 = p3;
			p3 = pom;
		}
		
		//System.out.println();
		//System.out.println(p1.w + " / " + p2.w + " / " + p3.w);
		
		if(p3.w > wmin){
			//je vidìt celej
			dehomog(av1, av2, av3);
			
		}else
		if(p2.w > wmin){
			//dva body jsou vidìt => vytvoøit novej trojúhelník
			VVector v1 = new VVector();
			VVector v2 = new VVector();
			VVector vb = new VVector();
			VVector vc = new VVector();
			v1.data.add(p1);
			v2.data.add(p2);

			//první - delší strana {P1, P3}
			double t = (p1.w - wmin) / (double) (p1.w - p3.w);
			Point3D p = p1.mul(1-t).add(p3.mul(t));
			vb.data.add(p);
			
			//druhá - kratší strana {P2, P3}
			t = (p2.w - wmin) / (double) (p2.w - p3.w);
			p = p2.mul(1-t).add(p3.mul(t));
			vc.data.add(p);

			dehomog(v1, vb, vc);
			dehomog(v1, v2, vc);
			
		}else
		if(p1.w > wmin){
			//jeden je vidìt
			//System.out.println("Jeden vidìt");
			VVector v1 = new VVector();
			VVector va = new VVector();
			VVector vb = new VVector();
			v1.data.add(p1);
			
			// {P1, P3}
			double t = (p1.w - wmin) / (double) (p1.w - p3.w);
			Point3D p = p1.mul(1-t).add(p3.mul(t));
			va.data.add(p);

			// {P1, P2}
			t = (p1.w - wmin) / (double) (p1.w - p2.w);
			p = p1.mul(1-t).add(p2.mul(t));
			vb.data.add(p);

			dehomog(v1, va, vb);

		}else{
			//není vidìt
			//System.out.println("Není vidìt");
		}
		
		//pokud se má udìlat novej trojúhelník va = v1.mul(1-t).add(v2.mil(t));
		
		/*
		if(p1.w>=wmin && p2.w>=wmin && p3.w>=wmin ){
			//pøed kamerou
		}
		else if(p1.w<wmin && p2.w<wmin && p3.w<wmin ){
			//za kamerou
		} 
		else if((p1.w<wmin && p2.w<wmin) || (p2.w<wmin && p3.w<wmin) || (p3.w<wmin && p1.w<wmin) ){
			//pøed kamerou 1
		} 
		else 
		{
			//pøed kamerou 2
		} 
		*/
		
	}

	/**
	 * Ke vsem vrcholum doplni konstantu 1 (v podobe instance tridy Vec1D, ktera
	 * ma vlastnosti prvku vektoroveho prostoru (rozsiruje VSE)) - prvni krok
	 * perspektivni korekce. Potom kazdy vrchol vydeli souradnici w prvni
	 * polozky vrcholu (prvni polozka tedy musi byt tridy Point3D) -
	 * dehomogenizace.
	 * 
	 * @param av1,
	 *            av2, av3 Vrcholy orezaneho trojuhelnika.
	 */
	void dehomog(VVector av1, VVector av2, VVector av3) {
		VVector v1, v2, v3;
		v1 = (VVector) av1.clone();
		v2 = (VVector) av2.clone();
		v3 = (VVector) av3.clone();
		v1.data.add(new Vec1D(1.0));
		v2.data.add(new Vec1D(1.0));
		v3.data.add(new Vec1D(1.0));
		v1 = (VVector) v1.mul(1.0 / ((Point3D) v1.data.get(0)).w);
		v2 = (VVector) v2.mul(1.0 / ((Point3D) v2.data.get(0)).w);
		v3 = (VVector) v3.mul(1.0 / ((Point3D) v3.data.get(0)).w);
		testTriangle(v1, v2, v3);
	}

	/**
	 * Testuje, zda trojuhelnik neni zcela mimo orezavaci kvadr. V zavislosti na
	 * hodnote promenne wireframe bud vykresli potencialne viditelne
	 * trojuhelniky jako drateny model metodou drawTriangle(), nebo je
	 * rasterizuje metodou rasterizeTriangle().
	 * 
	 * @param av1,
	 *            av2, av3 Vrcholy trojuhelnika po prvnim kroku perspektivni
	 *            korekce a po dehomogenizaci.
	 */
	void testTriangle(VVector av1, VVector av2, VVector av3) {
		if (Math.min(Math.min(((Point3D) av1.data.get(0)).x,
				((Point3D) av2.data.get(0)).x), ((Point3D) av3.data.get(0)).x) > 1.0
				|| Math.max(Math.max(((Point3D) av1.data.get(0)).x,
						((Point3D) av2.data.get(0)).x), ((Point3D) av3.data
						.get(0)).x) < -1.0
				|| Math.min(Math.min(((Point3D) av1.data.get(0)).y,
						((Point3D) av2.data.get(0)).y), ((Point3D) av3.data
						.get(0)).y) > 1.0
				|| Math.max(Math.max(((Point3D) av1.data.get(0)).y,
						((Point3D) av2.data.get(0)).y), ((Point3D) av3.data
						.get(0)).y) < -1.0
				|| Math.min(Math.min(((Point3D) av1.data.get(0)).z,
						((Point3D) av2.data.get(0)).z), ((Point3D) av3.data
						.get(0)).z) > 1.0
				|| Math.max(Math.max(((Point3D) av1.data.get(0)).z,
						((Point3D) av2.data.get(0)).z), ((Point3D) av3.data
						.get(0)).z) < 0.0)
			return;
		if (wireframe)
			drawTriangle(av1, av2, av3);
		else
			rasterizeTriangle(av1, av2, av3);
	}

	/**
	 * Vykresli hrany trojuhelnika pomoci objektu line.
	 */
	void drawTriangle(VVector av1, VVector av2, VVector av3) {
		int x1, y1, x2, y2, x3, y3;
		x1 = (int) ((line.getWidth() - 1) * 0.5 * (1.0 + ((Point3D) av1.data
				.get(0)).x));
		y1 = (int) ((line.getHeight() - 1) * 0.5 * (1.0 - ((Point3D) av1.data
				.get(0)).y));
		x2 = (int) ((line.getWidth() - 1) * 0.5 * (1.0 + ((Point3D) av2.data
				.get(0)).x));
		y2 = (int) ((line.getHeight() - 1) * 0.5 * (1.0 - ((Point3D) av2.data
				.get(0)).y));
		x3 = (int) ((line.getWidth() - 1) * 0.5 * (1.0 + ((Point3D) av3.data
				.get(0)).x));
		y3 = (int) ((line.getHeight() - 1) * 0.5 * (1.0 - ((Point3D) av3.data
				.get(0)).y));
		line.line(x1, y1, x2, y2);
		line.line(x1, y1, x3, y3);
		line.line(x3, y3, x2, y2);
	}

	/**
	 * Rasterizuje trojuhelnik, souradnice nacita z prvni polozky jednotlivych
	 * vrcholu a prepocitava je na rozmery imageBufferu. Vlastnosti jednotlivych
	 * pixelu jsou vysledkem interpolace vrcholu. Pixely jsou dale zpracovavany
	 * volanim metody processPixel().
	 */
	boolean uz = true;
	
	void rasterizeTriangle(VVector av1, VVector av2, VVector av3) {
		/*
		 * seøadit podle x od bodu nejvíc nahoøe
		 * av.getdata.get0 -> point3d.getx
		 */


		int x1, y1, x2, y2, x3, y3,pomInt;
		x1 = (int) ((imageBuffer.getWidth() - 1) * 0.5 * (1.0 + ((Point3D) av1.data.get(0)).x));
		y1 = (int) ((imageBuffer.getHeight() - 1) * 0.5 * (1.0 - ((Point3D) av1.data.get(0)).y));
		x2 = (int) ((imageBuffer.getWidth() - 1) * 0.5 * (1.0 + ((Point3D) av2.data.get(0)).x));
		y2 = (int) ((imageBuffer.getHeight() - 1) * 0.5 * (1.0 - ((Point3D) av2.data.get(0)).y));
		x3 = (int) ((imageBuffer.getWidth() - 1) * 0.5 * (1.0 + ((Point3D) av3.data.get(0)).x));
		y3 = (int) ((imageBuffer.getHeight() - 1) * 0.5 * (1.0 - ((Point3D) av3.data.get(0)).y));
		
		VVector pom;



		if(y1 > y2){
		pom = av1;
		av1 = av2;
		av2 = pom;

		pomInt = y1;
		y1 = y2;
		y2 = pomInt;

		pomInt = x1;
		x1 = x2;
		x2 = pomInt;


		}if (y1 > y3){
		pom = av1;
		av1 = av3;
		av3 = pom;

		pomInt = y1;
		y1 = y3;
		y3 = pomInt;

		pomInt = x1;
		x1 = x3;
		x3 = pomInt;
		}if(y2 > y3){
		pom = av2;
		av2 = av3;
		av3 = pom;

		pomInt = y2;
		y2 = y3;
		y3 = pomInt;

		pomInt = x2;
		x2 = x3;
		x3 = pomInt;
		}


		double t1,t2, t, pomDouble;


		for (int y = (int) (y1+1); y <= y2 - 1; y++) {


		t1=(y-y1)/(double)(y2-y1);
		VVector p = av1.mul(1-t1).add(av2.mul(t1));
		double xa = x1 * (1-t1) + x2 * t1;

		t2=(y-y1)/(double)(y3-y1);
		VVector pp = av1.mul(1-t2).add(av3.mul(t2));
		double xb = x1 * (1-t2) + x3 * t2;


		if(xa > xb){
		pom = p ;
		p = pp;
		pp = pom;

		pomDouble = xa;
		xa = xb;
		xb = pomDouble;

		}

		for (int x = (int)xa + 1; x <= xb; x++) {

		t = (x - xa) / (xb - xa);
		VVector vv = p.mul(1-t).add(pp.mul(t));



		processPixel(x, y, vv);
		}
		}


		for (int y = (int) y2; y <= y3; y++) {


		t1=(y-y2)/(double)(y3-y2);
		VVector p = av2.mul(1-t1).add(av3.mul(t1));
		double xa = x2 * (1-t1) + x3 * t1;

		t2=(y-y1)/(double)(y3-y1);
		VVector pp = av1.mul(1-t2).add(av3.mul(t2));
		double xb = x1 * (1-t2) + x3 * t2;


		if(xa > xb){
		pom = p ;
		p = pp;
		pp = pom;

		pomDouble = xa;
		xa = xb;
		xb = pomDouble;

		}

		for (int x = (int)xa + 1; x <= xb; x++) {

		t = (x - xa) / (xb - xa);
		VVector vv = p.mul(1-t).add(pp.mul(t));



		processPixel(x, y, vv);
		}


		}
	}

	/**
	 * Zpracovava jednotlive pixely vystupujici z rasterizace. Nejprve provede
	 * druhy krok perspektivni korekce - vydeli posledni polozkou interpolatu,
	 * ktera byla do vrcholu vlozena v prvnim kroku a nyni obsahuje
	 * interpolovanou prevracenou souradnici w (1/w). Potom provede pixel
	 * shader, ktery z interpolatu vypocte vyslednou barvu. Dale otestuje alfa
	 * kanal barvy a souradnici z, provede smichani barev a nakonec, v
	 * zavislosti na hodnotach promennych zWrite a colorWrite, zapise souradnici
	 * z do zBufferu a barvu do imageBufferu.
	 * 
	 * @param x
	 *            Souradnice x pixelu v imageBufferu.
	 * @param y
	 *            Souradnice y pixelu v imageBufferu.
	 * @param pixel
	 *            Interpolat.
	 */
	void processPixel(int x, int y, VVector pixel) {
		Ref<Col> col = new Ref<Col>();
		Ref<Vec1D> z = new Ref<Vec1D>();
		z.ref = new Vec1D(((Point3D) pixel.data.get(0)).z);
		pixel = pixel.mul(1.0 / ((Vec1D) pixel.data.lastElement()).x);
		pixel.data.removeElementAt(pixel.data.size()-1);
		pixelShader.process(pixel, col, z);
		if (z.ref.x < 0.0 || z.ref.x > 1.0)
			return;
		if (alphaTest != null && !alphaTest.test(col.ref.a))
			return;
		if (zTest != null && !zTest.test(z.ref.x, zBuffer.read(x, y).x))
			return;
		if (colorWrite) {
			if (blend != null)
				col.ref = blend.blend(col.ref, imageBuffer.read(x, y));
			imageBuffer.write(x, y, col.ref);
		}
		if (zWrite)
			zBuffer.write(x, y, z.ref);
	}
}
