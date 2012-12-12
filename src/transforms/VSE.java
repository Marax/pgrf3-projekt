package transforms;

/**
 * Abstraktni genericka trida reprezentujici prvek vektoroveho prostoru (Vector
 * Space Element), tedy deklarujici abstraktni metodu add() pro operaci scitani
 * dvou vektoru a abstraktni metodu mul() pro operaci nasobeni vektoru skalarem.
 * Potomci obsahujici pouze instancni promenne primitivnich typu jsou
 * klonovatelni.
 * 
 * @param <T>
 *            Parametrizujici trida, potomci VSE parametrizuji sami sebou.
 */
public abstract class VSE<T extends VSE<T>> implements Cloneable {
	/**
	 * Metoda pro soucet dvou vektoru.
	 * 
	 * @param rhs
	 *            Pravy operand - vektor.
	 * @return Vysledek operace v nove instanci.
	 */
	public abstract T add(T rhs);

	/**
	 * Metoda pro nasobeni vektoru skalarem.
	 * 
	 * @param rhs
	 *            Pravy operand - skalar.
	 * @return Vysledek operace v nove instanci.
	 */
	public abstract T mul(double rhs);

	public Object clone() {
		Object res = null;
		try {
			res = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return res;
	}
}
