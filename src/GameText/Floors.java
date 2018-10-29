package GameText;
import java.util.ArrayList;
import java.lang.reflect.*;
public class Floors {
	private ArrayList<Objects> objectsList;
	private String floorName;
	public void addObjects(ArrayList<Objects> obj){
		this.objectsList = obj;
	}
	public void setName(String newName) {
		this.floorName = newName;
	}
	public String getName() {
		return this.floorName;
	}
	public boolean runMethods(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Object currentObj = Class.forName(className).newInstance();
		Method method = currentObj.getClass().getMethod("floorTask", ArrayList.class);
		boolean result = (boolean) method.invoke(currentObj, this.objectsList);
		return result;
	}
	public ArrayList<Objects> getObjects(){
		return this.objectsList;
	}
	public Floors() {}
	
}
