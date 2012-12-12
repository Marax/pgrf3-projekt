package renderer;

/**
 * Rozhrani, jehoz implementace zajistuje vykreslovani drateneho modelu.
 * 
 */
public interface Line {
	/**
	 * @return Vyska objektu (okna, platna, obrazovkoveho bufferu), na ktery
	 *         bude vykreslen drateny model.
	 */
	int getHeight();

	/**
	 * Vykresli usecku na zadane souradnice.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	void line(int x1, int y1, int x2, int y2);

	/**
	 * @return Sirka objektu (okna, platna, obrazovkoveho bufferu), na ktery
	 *         bude vykreslen drateny model.
	 */
	int getWidth();
}
