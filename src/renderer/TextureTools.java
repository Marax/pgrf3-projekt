package renderer;

import transforms.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Pomocna trida, ktera obsahuje staticke metody pro dalsi praci s texturami,
 * jako je nacteni textury ze souboru.
 * 
 */
public abstract class TextureTools {
	/**
	 * Vytvori novou instanci tridy Texture2D parametrizovane tridou Col a
	 * naplni ji obsahem z BufferedImage v argumentu.
	 * 
	 * @param img
	 *            Instance tridy BufferedImage, jejimz obsahem bude nova textura
	 *            naplnena.
	 * @return Nova instance tridy Texture2D parametrizovane tridou Col.
	 */
	public static Texture2D<Col> createFromBufferedImage(BufferedImage img) {
		Texture2D<Col> res = new Texture2D<Col>(img.getWidth(), img
				.getHeight(), new Col(0, 0, 0));
		for (int y = 0; y < img.getHeight(); y++)
			for (int x = 0; x < img.getWidth(); x++) {
				res.write(x, y, new Col(img.getRGB(x, y), true));
			}
		return res;
	}

	/**
	 * Vytvori novou instanci tridy Texture2D parametrizovane tridou Col a
	 * naplni ji obsahem nactenym ze souboru.
	 * 
	 * @param fname
	 *            Jmeno souboru, ze ktereho bude nacten obsah textury. O
	 *            podporovanych formatech vice viz. javax.imageio.ImageIO.
	 * @return Nova instance tridy Texture2D parametrizovane tridou Col.
	 */
	public static Texture2D<Col> createFromFile(String fname) {
		try {
			return createFromBufferedImage(ImageIO.read(new File(fname)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
