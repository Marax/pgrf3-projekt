package gui;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class TextureList implements ComboBoxModel {

	private String s = "Mìsíc";

	@Override
	public void addListDataListener(ListDataListener arg0) {
	}

	@Override
	public Object getElementAt(int arg0) {
		switch (arg0) {
		case 0:
			return "Mìsíc";
		case 1:
			return "Earth";
		case 2:
			return "Telos";
		}
		return "error";
	}

	@Override
	public int getSize() {
		return 3;
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
	}

	@Override
	public Object getSelectedItem() {

		return s;
	}

	@Override
	public void setSelectedItem(Object arg0) {
		s = String.valueOf(arg0);

	}

}
