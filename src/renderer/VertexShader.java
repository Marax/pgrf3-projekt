package renderer;


/**
 * Rozhrani, jehoz implementace definuje operace provadene na vrcholech.
 * 
 */
public interface VertexShader {
	/**
	 * Provadi operace s vrcholy nacitanymi ze seznamu vrcholu. Vraci seznam
	 * atributu vrcholu, ktere budou interpolovany pri rasterizaci trojuhelniku
	 * a ktere jsou vstupem pixel shaderu. PRVNI POLOZKA SEZNAMU MUSI BYT
	 * INSTANCE TRIDY Point3D.
	 * 
	 * @param vertex
	 *            Instance tridy, ktera popisuje veskere vlastnosti vrcholu
	 *            ocekavane v teto metode. Pro dany seznam vrcholu musi byt
	 *            pouzita spravna implementace rozhrani VertexShader.
	 * @return Nova instance tridy VVector, jejiz pole data je naplneno atributy
	 *         k dalsimu zpracovani. PRVNI POLOZKA MUSI BYT INSTANCE TRIDY
	 *         Point3D.
	 */
	VVector process(Object vertex);
}
