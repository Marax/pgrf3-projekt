package renderer;

import transforms.Col;

/**
 * Rozhrani, jehoz implementace definuje chovani pri skladani barvy
 * vykreslovaneho pixelu s barvou v imageBufferu.
 * 
 */
public interface Blend {
	/**
	 * Metoda michajici barvu vykreslovaneho pixelu s barvou naposled
	 * vykresleneho pixelu na odpovidajicich souradnicich.
	 * 
	 * @param newCol
	 *            Barva prave vykreslovaneho pixelu.
	 * @param oldCol
	 *            Barva naposled vykresleneho pixelu nactena z imageBufferu.
	 * @return Nova barva vykreslovaneho pixelu, ktera bude zapsana do
	 *         imageBufferu.
	 */
	Col blend(Col newCol, Col oldCol);
}
