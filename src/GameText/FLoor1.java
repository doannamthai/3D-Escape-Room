package GameText;

import java.util.ArrayList;

class Floor1{
	public boolean floorTask(ArrayList<Objects> objectsList) {
		// Get the Key to access the Door
		Objects Character = objectsList.get(0);
		ArrayList<Objects> theBag = Character.getBag();
		for (int index = 0; index < theBag.size(); index++) {
			Objects currentObj = theBag.get(index);
			if (currentObj.getName().equals("Key")) {
				return true;
			}
		}
		return false;
	}
}

