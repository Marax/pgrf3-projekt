package gui;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class ObjectList implements ComboBoxModel {

	private String s = "(Carthesian)Sphere";
	private int id = 0;
	
	public void addListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub

	}

	
	public Object getElementAt(int arg0) {
		switch(arg0){
			case 0: return "(Carthesian)Sphere";
			case 1: return "(Carthesian)Snake";
			case 2: return "(Spherical) 1";
			case 3: return "(Spherical) 2";
			case 4: return "(Cylindrical) 1";
			case 5: return "(Cylindrical) 2";		
		}
		return "error";
	}

	
	public int getSize() {
	
		return 6;
	}

	
	public void removeListDataListener(ListDataListener arg0) {
		

	}

	
	public Object getSelectedItem() {
		
		return s;
	}

	
	public void setSelectedItem(Object arg0) {
		s = String.valueOf(arg0);

	}

}
