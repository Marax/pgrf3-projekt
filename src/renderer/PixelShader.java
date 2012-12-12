package renderer;

import transforms.*;

/**
 * Rozhrani, jehoz implementace definuje operace provadene na pixelech.
 * 
 */
public interface PixelShader {
	/**
	 * Metoda vypocitavajici barvu pixelu a pripadne upravujici jeho souradnici
	 * z.
	 * 
	 * @param interpolate
	 *            Vysledek interpolace obsahu vrcholu pres trojuhelnik.
	 *            Semanticky shodny s vystupem z vertex shaderu.
	 * @param outColor
	 *            Vystupni barva pixelu. outColor.ref musi byt naplnena instanci
	 *            tridy Col.
	 * @param outZ
	 *            Volitelny vystup souradnice z. outZ.ref jiz odkazuje na
	 *            instanci obsahujici souradnici z vydelenou w (perspektivne
	 *            opravenou souradnici z je mozne ziskat z prvni polozky
	 *            atributu ainterpolate).
	 */
	void process(VVector interpolate, Ref<Col> outColor, Ref<Vec1D> outZ);
}
