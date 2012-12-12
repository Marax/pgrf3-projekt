package renderer;

import transforms.VSE;

/**
 * Genericka trida spravujici dvourozmerne pole prvku vektoroveho prostoru.
 * Umoznuje vyplneni danou hodnotou, zapis a cteni na celociselnych souradnicich
 * i bilinearne interpolovane cteni na realnych souradnicich v intervalu <0;1>.
 * 
 * @param <T>
 *            Parametrizujici trida, ktera ma vlastnosti prvku vektoroveho
 *            prostoru - implementuje metody add() a mul() zdedene od VSE.
 */
public class Texture2D<T extends VSE<T>> {
	VSE buff[][] = null;
	int width = 0, height = 0;
	T border = null;

	/**
	 * Mozne zpusoby adresovani v pripade, ze souradnice do textury jsou mimo
	 * oblast textury. WRAP: textura se opakuje, MIRROR: podobne jako WRAP,
	 * textura se pri tom preklapi, CLAMP: souradnice jsou orezany na povoleny
	 * rozsah - opakuji se krajni texely, BORDER: vraci se hodnota nastavena
	 * metodou setBorder(). Tyka se pouze metod read() a sample(), metoda
	 * write() vzdy souradnice orezava.
	 * 
	 */
	public enum AddressMode {
		WRAP, MIRROR, CLAMP, BORDER
	};

	/**
	 * Aktualne nastaveny rezim adresovani. Viz AddressMode.
	 */
	public AddressMode addressMode = AddressMode.WRAP;

	
	Texture2D() {
	}

	/**
	 * Vytvori pole se zadanou sirkou a vyskou a vyplni jej zadanou hodnotou.
	 * 
	 * @param awidth
	 *            Pozadovana sirka pole;
	 * @param aheight
	 *            Pozadovana vyska pole;
	 * @param val
	 *            Instance, jejimiz klony bude vytvorene pole vyplneno. Nesmi
	 *            byt null.
	 */
	public Texture2D(int awidth, int aheight, T val) {
		width = awidth;
		height = aheight;
		buff = new VSE[height][width];
		border = (T) val.clone();
		clear(val);
	}

	/**
	 * Maze texturu danou hodnotou.
	 * 
	 * @param val
	 *            Instance, kterou bude textura vyplnena. Na kazdy texel je
	 *            ulozen novy klon. Nesmi byt null;
	 */
	public void clear(T val) {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				buff[i][j] = (VSE<T>) val.clone();
			}
	}

	public Object clone() {
		Texture2D<T> res = new Texture2D<T>();
		res.width = width;
		res.height = height;
		res.buff = new VSE[height][width];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				res.buff[i][j] = (VSE<T>) buff[i][j].clone();
			}
		return res;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * Vraci prvek na celociselnych souradnicich v intervalech <0;sirka-1>,
	 * <0;vyska-1>. Adresovani mimo tyto intervaly se ridi promennou addressMode.
	 * Vraci pouze referenci, ne naklonovanou instanci.
	 * 
	 * @param x
	 *            Souradnice sloupecku.
	 * @param y
	 *            Souradnice radku.
	 * @return Reference na element na zadanych souradnicich.
	 */
	public T read(int x, int y) {
		switch (addressMode) {
		case WRAP:
			if (x < 0)
				x = width - (-x % width) - 1;
			if (x >= width)
				x %= width;
			if (y < 0)
				y = height - (-y % height) - 1;
			if (y >= height)
				y %= height;
			return (T) buff[y][x];
		case MIRROR:
			if (x < 0)
				if ((-x / width) % 2 == 0)
					x = -x % width;
				else
					x = width - (-x % width) - 1;
			if (x >= width)
				if ((x / width) % 2 == 0)
					x %= width;
				else
					x = width - (x % width) - 1;
			if (y < 0)
				if ((-y / height) % 2 == 0)
					y = -y % height;
				else
					y = height - (-y % height) - 1;
			if (y >= height)
				if ((y / height) % 2 == 0)
					y %= height;
				else
					y = height - (y % height) - 1;
			return (T) buff[y][x];
		case CLAMP:
			if (x < 0)
				x = 0;
			if (x >= width)
				x = width - 1;
			if (y < 0)
				y = 0;
			if (y >= height)
				y = height - 1;
			return (T) buff[y][x];
		case BORDER:
			if (x < 0 || x >= width || y < 0 || y >= height)
				return border;
			else
				return (T) buff[y][x];
		}
		return null;
	}

	/**
	 * Vraci prvek navzorkovany bilinearni interpolaci na realnych souradnic.
	 * Cela textura je dostupna na souradnicich v intervalu <0;1>, navratova
	 * hodnota pri souradnicich mimo interval zavisi na nastaveni promenne
	 * addressMode.
	 * 
	 * @param x
	 *            Souradnicim v intervalu <0;1> odpovidaji adresy <0;sirka-1>.
	 * @param y
	 *            Souradnicim v intervalu <0;1> odpovidaji adresy <0;vyska-1>.
	 * 
	 */
	public T sample(double x, double y) {
		x *= width - 1.0;
		y *= height - 1.0;
		int sx = (int) Math.signum(x);
		int sy = (int) Math.signum(y);
		x *= sx;
		y *= sy;
		int fx = (int) x, cx = fx + 1, fy = (int) y, cy = fy + 1;
		double dx = x - fx, dy = y - fy;
		return (T) read(sx * fx, sy * fy).mul((1.0 - dx) * (1.0 - dy)).add(
				read(sx * cx, sy * fy).mul(dx * (1.0 - dy))).add(
				read(sx * fx, sy * cy).mul((1.0 - dx) * dy)).add(
				read(sx * cx, sy * cy).mul(dx * dy));
	}

	/**
	 * Nastavuje hodnotu, ktera je vracena pri cteni mimo texturu, pokud je
	 * addressMode nastaven na AddressMode.BORDER.
	 * 
	 * @param val
	 *            Hodnota vracena pri cteni mimo texturu. Je pouzit klon;
	 */
	public void setBorder(T val) {
		border = (T) val.clone();
	}

	/**
	 * Ulozi referenci na instanci na zadane souradnice v intervalech
	 * <0;sirka-1>, <0;vyska-1>.
	 * 
	 * @param x
	 *            Souradnice sloupecku.
	 * @param y
	 *            Souradnice radku.
	 * @param val
	 *            Instance, ktera ma byt ulozena. Ulozena bude pouze reference,
	 *            ne klon.
	 */
	public void write(int x, int y, T val) {
		if (x < 0)
			x = 0;
		if (x >= width)
			x = width - 1;
		if (y < 0)
			y = 0;
		if (y >= height)
			y = height - 1;
		buff[y][x] = (VSE<T>) val;
	}

}
