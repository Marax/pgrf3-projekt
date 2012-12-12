package renderer;

/**
 * Rozhrani, jehoz implementace definuje chovani pri rozhodovani o vykresleni
 * pixelu na zaklade hodnoty jeho alfa-kanalu.
 * 
 */
public interface AlphaTest {
	/**
	 * Metoda rozhodujici o vykresleni pixelu v zavislosti na hodnote jeho alfa
	 * kanalu.
	 * 
	 * @param alpha
	 *            Hodnota alfa kanalu vykreslovaneho pixelu.
	 * @return Pokud metoda vrati true, pixel bude vykreslen.
	 */
	boolean test(double alpha);
}
