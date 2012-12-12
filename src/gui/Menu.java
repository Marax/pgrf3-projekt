package gui;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import objects.Cyl1;
import objects.Cyl2;
import objects.IObject;
import objects.Snake;
import objects.Sp1;
import objects.Sp2;
import objects.Sphere;

public class Menu extends JFrame implements ActionListener {

	private App app;
	private JComboBox objectList, textureList;
	JCheckBox normalMap;
	public Menu(App app) {
		this.app = app;
		setupGui();
	}

	private void setupGui() {

		setTitle("Ovládání");

		setSize(200, 300);
		LayoutManager m = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
		setLayout(m);

		JPanel objectPanel = new JPanel();
		objectList = new JComboBox(new ObjectList());
		objectList.setSize(100, 20);
		objectPanel.add(new JLabel("Objekt:"));
		objectPanel.add(objectList);
		add(objectPanel);

		JPanel pTxt = new JPanel();
		textureList = new JComboBox(new TextureList());
		textureList.setSize(100, 20);
		pTxt.add(new JLabel("Textura:"));
		pTxt.add(textureList);
		add(pTxt);

		normalMap = new JCheckBox("Normal Mapping");
		
		
		JPanel pButtons = new JPanel();
		JButton apply = new JButton("Render solid");
		pButtons.add(normalMap);
		pButtons.add(apply);
		apply.addActionListener(this);
		add(pButtons);

		pack();
		setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		IObject object = null;
		switch (objectList.getSelectedIndex()) {
		case 0:
			object = new Sphere();
			break;
		case 1:
			object = new Snake();
			break;
		case 2:
			object = new Sp1();
			break;
		case 3:
			object = new Sp2();
			break;
		case 4:
			object = new Cyl1();
			break;
		case 5:
			object = new Cyl2();
			break;
		}

	
			//app.texturing = true;

			/*if (texNo != textureList.getSelectedIndex()) {
				texNo = textureList.getSelectedIndex();
				app.t.load(texNo);
			}*/
			//if (vertical.isSelected()) {
			//	app.t.setHorizontal(false);
			//} else {
			//	app.t.setHorizontal(true);
		//	}
		app.normalMap = normalMap.isSelected();
		app.setFce(object);

		app.renderSolid();

	}

}
