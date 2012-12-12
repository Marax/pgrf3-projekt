package renderer;

import java.util.Vector;

import transforms.VSE;

/**
 * Trida, ktera doplnuje tridu Vector o vlastnosti prvku vektoroveho prostoru.
 * Obsahuje instanci tridy Vector a nad ni implementuje metody add() a mul()
 * zdedene od VSE.
 * 
 */
public class VVector extends VSE<VVector> {
	/**
	 * Verejne pristupna instance tridy Vector, nad kterou jsou implementovane
	 * metody add() a mul() zdedene od VSE.
	 */
	public Vector<VSE> data;

	public VVector() {
		data = new Vector<VSE>();
	}

	/**
	 * Metoda pro soucet dvou vektoru. Implementace abstraktni metody zdedene od
	 * VSE.
	 * 
	 * @param rhs
	 *            Pravy operand - vektor. Musi byt instance tridy VVector.
	 * @return Vysledek operace v nove instanci tridy VVector.
	 */
	public VVector add(VVector rhs) {
		VVector res = new VVector();
		res.data = new Vector<VSE>(data.capacity());
		for (int i = 0; i < data.size(); i++)
			res.data.add(data.get(i).add(rhs.data.get(i)));
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see transforms.VSE#mul(double)
	 */
	public VVector mul(double rhs) {
		VVector res = new VVector();
		res.data = new Vector<VSE>(data.capacity());
		for (int i = 0; i < data.size(); i++)
			res.data.add(data.get(i).mul(rhs));
		return res;
	}

	public Object clone() {
		VVector res = new VVector();
		res.data = (Vector<VSE>) data.clone();
		return res;
	}

}
