import fim.utils.Application;
import fim.utils.Console;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage; //import java.awt.*;
import java.text.Normalizer;
import java.util.*;

import samples.ClockApp;
import samples.GraphApp;
import samples.I18NApplication;
import samples.KochApp;
import samples.SortApp;
import transforms.*;
import renderer.*;
import renderer.Texture2D.AddressMode;

public class App extends Application {

	BufferedImage im;

	int nRows = 22;
	int nColumns = 22;

	Vector vertices = new Vector();
	Vector<Integer> indices = new Vector<Integer>();

	MyVertexShader vs = new MyVertexShader();
	MyPixelShader ps = new MyPixelShader();
	MyAlphaTest at = new MyAlphaTest();
	MyZTest zt = new MyZTest();
	MyBlend bl = new MyBlend();

	MyLine line = new MyLine();

	Texture2D<Col> imageBuffer;
	Texture2D<Vec1D> zBuffer;

	Texture2D<Col> texture, texture_n;

	Renderer ren = new Renderer();

	Camera cam = new Camera();
	Mat4 mat, trans;

	int typ = 999;
	float texturaBarva = 1;

	boolean pressed = false;

	Vec3D zdroj_svetla;
	boolean nastavit_svetlo = true;

	int ox, oy;

	class MyVertexShader implements VertexShader {
		public MyVertexShader() {
		}

		public Vec3D computeVertex(Object vertex) {
			return Telesa.compute(((Vec2D) vertex).x, ((Vec2D) vertex).y, typ);
		}

		public VVector process(Object vertex) {
			VVector result = new VVector();

			if (texturaBarva > 1) {

				double d = 0.001;

				Point3D pos = new Point3D(computeVertex((Vec2D) vertex));
				result.data.add(pos.mul(mat)); // get(0)

				// binormal
				Vec3D T = computeVertex(((Vec2D) vertex).add(new Vec2D(d, 0)))
						.add(computeVertex(
								((Vec2D) vertex).add(new Vec2D(-d, 0))).mul(-1))
						.normalized();

				// tangent
				Vec3D B = computeVertex(((Vec2D) vertex).add(new Vec2D(0, d)))
						.add(computeVertex(
								((Vec2D) vertex).add(new Vec2D(0, -d))).mul(-1))
						.normalized();

				// normal
				Vec3D N = T.cross(B);
				N = B.cross(T);
				B = T.cross(N);

				Vec3D f_normal = new Vec3D(-N.y, N.x, N.z);
				Mat3 tang = new Mat3(N, N, N);
				tang = tang.transpose();

				result.data.add(f_normal); // get(1)
				result.data.add(new Vec3D(pos)); // get(2)
				result.data.add(tang); // get(3)
				result.data.add((Vec2D) vertex); // get(4)

				// zdroj_svetla = new Vec3D(cam.getPosition().x,
				// cam.getPosition().y + 50, cam.getPosition().z + 50);
				zdroj_svetla = new Vec3D(200, -200, 0);

				result.data.add(zdroj_svetla.mul(tang)); // get(5)
				result.data.add(cam.getEye().mul(tang)); // get(6)

			} else {

				/*
				 * Cvièení 14. 12.
				 */

				Vec3D eyepos = cam.getEye();

				double x = ((Vec2D) vertex).x;
				double y = ((Vec2D) vertex).y;

				Vec3D v = new Vec3D(computeVertex((Vec2D) vertex));
				zdroj_svetla = new Vec3D(200, -200, 0);

				Vec3D eyeVec = eyepos.add(v.mul(-1)).normalized(); // eyepos - v
				Vec3D lightVec = zdroj_svetla.add(v.mul(-1)).normalized();

				double dif = 1e-5;

				Vec3D dx = Telesa.compute(x + dif, y, typ).add(
						Telesa.compute(x - dif, y, typ).mul(-1));
				Vec3D dy = Telesa.compute(x, y + dif, typ).add(
						Telesa.compute(x, y - dif, typ).mul(-1));
				Vec3D n = dy.cross(dx).normalized(); // nebo obrácenì

				double diffuse = lightVec.dot(n);
				diffuse = Math.max(diffuse, 0);

				result.data.add(new Point3D(v).mul(mat));
				result.data.add((Vec2D) vertex);
				result.data.add(new Vec1D(diffuse));

			}

			return result;
		}
	}

	class MyPixelShader implements PixelShader {
		public MyPixelShader() {
		}

		public void process(VVector interpolate, Ref<Col> outColor,
				Ref<Vec1D> outZ) {

			if (texturaBarva == 2) {
				/*
				 * ============== normal mapping ==============
				 */

				// N
				Vec3D N = (Vec3D) interpolate.data.get(1);
				N = N.normalized();

				// svetlo
				Vec3D L = ((Vec3D) (interpolate.data.get(5))).add(
						(Vec3D) interpolate.data.get(2).mul(-1)).normalized();

				// pozorovatel
				Vec3D V = ((Vec3D) (interpolate.data.get(6))).add(
						(Vec3D) interpolate.data.get(2).mul(-1)).normalized();

				// barvy
				Col barva_a = new Col(255, 255, 255);
				Col barva_d = new Col(255, 255, 255);
				Col barva_s = new Col(255, 255, 255);
				Col barva_textura = texture.sample(
						((Vec2D) interpolate.data.get(4)).x,
						((Vec2D) interpolate.data.get(4)).y);

				// intenzita
				double Is = 0.5; // spek
				double Id = 0.7; // difu
				double Ia = 0.5; // ambi

				// vypocet difuzni slozky
				Col norm_c = texture_n.sample(
						((Vec2D) interpolate.data.get(4)).x,
						((Vec2D) interpolate.data.get(4)).y);
				Vec3D norm_d = new Vec3D(-1 + (2 * norm_c.r), -1
						+ (2 * norm_c.g), -1 + (2 * norm_c.b)).normalized();
				norm_d = norm_d.mul(-1);
				double Dd = Math.max(0, norm_d.dot(L.mul(-1)));

				// vypocet spektakularni slozky
				Vec3D R = norm_d.mul(2 * (L.dot(norm_d)));
				R = R.add(L.mul(-1));
				R = R.normalized();
				double Dr = Math.max(0, R.dot(V.mul(-1)));
				Dr = Math.pow(Dr, 5);

				// vysledna barva = ambientni + difuzni + spekularni
				Col C = new Col(Ia * barva_a.r + Dd * Id * barva_d.r + Dr * Is
						* barva_s.r, Ia * barva_a.g + Dd * Id * barva_d.g + Dr
						* Is * barva_s.g, Ia * barva_a.b + Dd * Id * barva_d.b
						+ Dr * Is * barva_s.b).saturate();

				// pøidání barvy z textury
				C.r *= barva_textura.r;
				C.g *= barva_textura.g;
				C.b *= barva_textura.b;

				outColor.ref = C;

			} else if (texturaBarva == 3) {
				/*
				 * ============== barva ==============
				 */
				outColor.ref = new Col(((Vec2D) interpolate.data.get(4)).x,
						((Vec2D) interpolate.data.get(4)).y, 0);

			} else if (texturaBarva == 4) {
				/*
				 * ============== pouze textura ==============
				 */
				outColor.ref = texture.sample(
						((Vec2D) interpolate.data.get(4)).x,
						((Vec2D) interpolate.data.get(4)).y);

			} else {
				/*
				 * ============== Gouraud ==============
				 */
				double d = ((Vec1D) interpolate.data.get(2)).x;
				if (texturaBarva == 0) {
					// s texturou
					outColor.ref = texture.sample(
							((Vec2D) interpolate.data.get(1)).x,
							((Vec2D) interpolate.data.get(1)).y).mul(d);
				} else {
					// bez textury
					outColor.ref = new Col(d, d, d);
				}
			}
		}
	}

	class MyAlphaTest implements AlphaTest {
		public MyAlphaTest() {
		}

		public boolean test(double alpha) {
			return true;
		}
	}

	class MyZTest implements ZTest {
		public MyZTest() {
		}

		public boolean test(double newZ, double oldZ) {
			return newZ < oldZ;
		}
	}

	/**
	 * 
	 * stará se o dohromady skloubila barva, která prijde z pixel shaderu, a
	 * stara barva je, která uz tam byla
	 * 
	 * @author starepa1
	 * 
	 */

	class MyBlend implements Blend {
		public MyBlend() {
		}

		public Col blend(Col newCol, Col oldCol) {
			return newCol;

		}
	}

	class MyLine implements Line {
		public MyLine() {
		}

		public int getHeight() {
			return im.getHeight();
		}

		public void line(int x1, int y1, int x2, int y2) {
			im.createGraphics().drawLine(x1, y1, x2, y2);
		}

		public int getWidth() {
			return im.getWidth();
		}

	}

	void setup() {
		ren.vertices = vertices;
		ren.indices = indices;

		ren.vertexShader = vs;
		ren.pixelShader = ps;
		ren.alphaTest = at;
		ren.zTest = zt;
		ren.blend = bl;

		ren.line = line;

		setupGeometry();

		texture = TextureTools.createFromFile("mesic.png");
		texture_n = TextureTools.createFromFile("mesic_n.png");
		texture.addressMode = AddressMode.WRAP;
		texture_n.addressMode = AddressMode.WRAP;

		imageBuffer = new Texture2D<Col>(im.getWidth(), im.getHeight(),
				new Col());
		zBuffer = new Texture2D<Vec1D>(im.getWidth(), im.getHeight(),
				new Vec1D(1.0));

		ren.imageBuffer = imageBuffer;
		ren.zBuffer = zBuffer;

		trans = new Mat4Identity();
	}

	private void setupGeometry() {

		for (int x = 0; x < nRows; x++) {
			for (int y = 0; y < nColumns; y++) {
				vertices.add(new Vec2D((double) y / (nColumns - 1) * 1,
						(double) x / (nRows - 1) * 1));
			}
		}

		for (int x = 0; x < nRows - 1; x++) {
			for (int y = 0; y < nColumns - 1; y++) {
				indices.add(x * nColumns + y);
				indices.add(x * nColumns + y + 1);
				indices.add((x + 1) * nColumns + y);

				indices.add(x * nColumns + y + 1);
				indices.add((x + 1) * nColumns + y);
				indices.add((x + 1) * nColumns + y + 1);
			}
		}

	}

	/**
	 * volano pokazde kdyz se klikne
	 */

	void renderWireframe() {
		out.getCanvas().clear();
		mat = trans.mul(cam.getViewMatrix()).mul(
				new Mat4PerspRH((double) (Math.PI / 4.0), im.getHeight()
						/ (double) im.getWidth(), 0.1f, 2000.0f));

		ren.wireframe = true;

		imageBuffer.clear(new Col(0x333333));
		ren.renderList(0, indices.size() / 3);
		// ren.renderStrip(0, 1);

		out.repaint();
	}

	/**
	 * vola se pokazde kdyz se zmackne ENTER
	 */
	void renderSolid() {
		out.getCanvas().clear();
		mat = trans.mul(cam.getViewMatrix()).mul(
				new Mat4PerspRH((double) (Math.PI / 4.0), im.getHeight()
						/ (double) im.getWidth(), 0.1f, 2000.0f));

		ren.wireframe = false;
		imageBuffer.clear(new Col(0x222222));
		zBuffer.clear(new Vec1D(1.0));
		ren.renderList(0, indices.size() / 3);

		copyImageBuffer();

		out.repaint();
	}

	void copyImageBuffer() {
		for (int y = 0; y < im.getHeight(); y++)
			for (int x = 0; x < im.getWidth(); x++) {
				im.setRGB(x, y, imageBuffer.read(x, y).getRGB());
			}
	}

	public void handleMenu(int zkratka) {
		switch (zkratka) {
		case 10:
			typ = 999;
			renderWireframe();
			break;
		case 20:
			typ = 1;
			renderWireframe();
			break;
		case 91:
			texturaBarva = 1;
			renderSolid();
			break;
		case 92:
			texturaBarva = 2;
			renderSolid();
			break;
		case 93:
			texturaBarva = 3;
			renderSolid();
			break;
		case 94:
			texturaBarva = 4;
			renderSolid();
			break;
		case 96:
			texturaBarva = 0;
			renderSolid();
			break;
		case 30:
			typ = 2;
			renderWireframe();
			break;
		case 40:
			typ = 3;
			renderWireframe();
			break;
		case 50:
			typ = 4;
			renderWireframe();
			break;
		case 60:
			typ = 5;
			renderWireframe();
			break;
		case 70:
			typ = 6;
			renderWireframe();
			break;
		case 110:
			texture = TextureTools.createFromFile("mesic.png");
			texture_n = TextureTools.createFromFile("mesic_n.png");
			texturaBarva = 2;
			renderSolid();
			break;
		case 120:
			texture = TextureTools.createFromFile("Telos.png");
			texture_n = TextureTools.createFromFile("Telos_n.png");
			texturaBarva = 2;
			renderSolid();
			break;
		case 130:
			texture = TextureTools.createFromFile("earth.png");
			texture_n = TextureTools.createFromFile("earth_n.png");
			texturaBarva = 2;
			renderSolid();
			break;
		case 140:
			texture = TextureTools.createFromFile("test_n.png");
			texturaBarva = 2;
			renderSolid();
			break;
		}
	}

	public void start() {

		menu.setVisible(true);
		menu.add("Kartez1 (Sphere)", 10);
		menu.add("Kartez2 (Snake)", 20);
		menu.add("Kartez3 ", 70);
		menu.add("Sphe1 (Bumpy Sphlere)", 30);
		menu.add("Sphe2 ", 60);
		menu.add("Cylin1 (Sombrero)", 50);
		menu.add("Cylin2 ", 40);
		menu.addSeparator();
		menu.add("Barva", 93);
		menu.add("Textura", 94);
		menu.add("Gouraud diffuse", 91);
		menu.add("Textura + Gouraud", 96);
		menu.add("Normal mapping", 92);
		menu.addSeparator();
		menu.add("Textura - Mìsíc", 110);
		menu.add("Textura - Telus", 120);
		menu.add("Textura - Zemì", 130);
		menu.add("Test (bezbarvá textura)", 140);

		buttons.setUserToolbarVisible(true);
		buttons.add("Barva", 93);
		buttons.add("Textura", 94);
		buttons.add("Gouraud diffuse", 91);
		buttons.add("Textura + Gouraud", 96);
		buttons.add("Normal mapping", 92);

		out.setTitle("Projekt PGRF3");

		im = out.getCanvas().getImage();

		// out.switchView(Console.VIEW_GRAPH);

		setup();

		cam.setPosition(new Vec3D(50, 2, 10));
		cam.setAzimuth((double) Math.PI);
		cam.setRadius(100);

		renderWireframe();

		out.getCanvas().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent ev) {
				out.getCanvas().requestFocus();
				if (ev.getButton() == MouseEvent.BUTTON1) {
					pressed = true;
					ox = ev.getX();
					oy = ev.getY();
					renderWireframe();
				}
			}

			public void mouseReleased(MouseEvent ev) {
				if (ev.getButton() == MouseEvent.BUTTON1) {
					pressed = false;
					renderWireframe();
				}
			}
		});
		out.getCanvas().addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent ev) {
				if (pressed) {
					cam.addAzimuth((double) Math.PI * (ox - ev.getX())
							/ im.getWidth());
					cam.addZenith((double) Math.PI * (oy - ev.getY())
							/ im.getHeight());
					renderWireframe();
					ox = ev.getX();
					oy = ev.getY();
				}
			}
		});
		out.getCanvas().requestFocus();
		out.getCanvas().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent key) {
				switch (key.getKeyCode()) {
				case KeyEvent.VK_W:
					cam.forward(1);
					break;
				case KeyEvent.VK_D:
					cam.right(1);
					break;
				case KeyEvent.VK_S:
					cam.backward(1);
					break;
				case KeyEvent.VK_A:
					cam.left(1);
					break;
				case KeyEvent.VK_SHIFT:
					cam.down(1);
					break;
				case KeyEvent.VK_CONTROL:
					cam.up(1);
					break;
				case KeyEvent.VK_SPACE:
					cam.setFirstPerson(!cam.getFirstPerson());
					break;
				case KeyEvent.VK_R:
					cam.mulRadius(0.9f);
					break;
				case KeyEvent.VK_F:
					cam.mulRadius(1.1f);
					break;
				case KeyEvent.VK_ENTER:
					renderSolid();
					return;
				case KeyEvent.VK_INSERT:
					trans = trans.mul(new Mat4RotX(0.1));
					break;
				case KeyEvent.VK_DELETE:
					trans = trans.mul(new Mat4RotX(-0.1));
					break;
				case KeyEvent.VK_HOME:
					trans = trans.mul(new Mat4RotY(0.1));
					break;
				case KeyEvent.VK_END:
					trans = trans.mul(new Mat4RotY(-0.1));
					break;
				case KeyEvent.VK_PAGE_UP:
					trans = trans.mul(new Mat4RotZ(0.1));
					break;
				case KeyEvent.VK_PAGE_DOWN:
					trans = trans.mul(new Mat4RotZ(-0.1));
					break;
				default:
					out.print(key.getKeyCode());
				}

				renderWireframe();
			}
		});

	}

	public static void main(String[] args) {
		new App().start();
	}
}