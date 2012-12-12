/**
 * 
 */
package renderer;

/**
 * Rozhrani, jehoz implementace definuje chovani pri rozhodovani o vykresleni
 * pixelu na zaklade hodnoty jeho souradnice z.
 * 
 */
public interface ZTest {
	/**
	 * Metoda rozhodujici o vykresleni pixelu v zavislosti na hodnote jeho
	 * aktualni a ze z-bufferu nactene souradnice z. Pokud nedochazi k uprave
	 * souradnice z v pixel shaderu, souradnice nejsou perspektivne korigovany.
	 * 
	 * @param newZ
	 *            Aktualni hodnota souradnice z.
	 * @param oldZ
	 *            Hodnota nactena ze z-bufferu z odpovidajicich souradnic
	 *            pixelu.
	 * @return Pokud metoda vrati true, pixel bude vykreslen.
	 */
	boolean test(double newZ, double oldZ);
}
