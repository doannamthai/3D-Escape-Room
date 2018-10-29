package game.Items;

import static GameGraphic.CoordsAndData.*;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;

import game.Engine.Loader;
import game.Engine.OBJreader;
import game.Engine.StaticModelLoader;

/** 
 * This Class contains a lot of magic numbers (translate Objects and rotates object)
 * Reasons: I haven't calculated the size of objects as well as the coordinates of the center to set object's position
 * @author Thai Doan
 * @CPSC233
 */
public class GameModels {
	private StaticModelLoader itemLoader = new StaticModelLoader();
	private static List<Item> listItems = new ArrayList<Item>();
	private final float margin = 0.01f;
	private final float scale = 0.5f + margin;
	
	/** Default Constructor
	 * Call methods that set up and save models to the array list
	 */
	public GameModels(Loader loader) {
		setUpLoader(loader);
		exitBlock();
		house();
		//model3D();
		door();
		//ceilingFan();
		bookshelf();
		bed();
		desktop();
		shoesShelf();
		balcony();
		closet();
		smallObjects();
	}
	
	public void setUpLoader(Loader loader) {
		this.itemLoader.addLoader(loader);
	}
	
	public List<Item> getListGameItems(){
		for (Item item: listItems) {
			item.scale(scale);
		}
		return listItems;
	}
	
	public void exitBlock() {
		Mesh[] exitBlockMeshes = itemLoader.load("models/Exit.obj", "textures/Exit.png");
		Item exitBlock = new Item(exitBlockMeshes);
		exitBlock.setName("ExitBlock");
		listItems.add(exitBlock);
	}
	
	public void smallObjects() {
		Mesh[] smallMeshes = itemLoader.load("models/smallObj.obj", "");
		Item smallObj = new Item(smallMeshes);
		smallObj.setName("Small Objects");
		listItems.add(smallObj);
	}
	
	public void balcony() {
		Mesh[] balconyMeshes = itemLoader.load("models/balcony.obj", "textures/Balcony.png");
		Item balcony = new Item(balconyMeshes);
		balcony.setName("Balcony");
		listItems.add(balcony);
	}
	
	public void house() {
		Mesh[] houseMeshes = itemLoader.load("models/House.obj", "textures/House.png");
		Item house = new Item(houseMeshes);
		house.setName("House");
		listItems.add(house);
	}
	
	public void shoesShelf() {
		Mesh[] shoesShelfMeshes = itemLoader.load("models/ShoesShelf.obj", "textures/ShoesShelf.png");
		Item shoesShelf = new Item(shoesShelfMeshes);
		shoesShelf.setName("Shoes Shelf");
		listItems.add(shoesShelf);
	}
	
	public void desktop() {
		Mesh[] desktopMeshes = itemLoader.load("models/DesktopArea.obj", "textures/DesktopArea.png");
		Item desktop = new Item(desktopMeshes);
		desktop.setName("Desktop");
		listItems.add(desktop);
	}

	public void door() {
		Mesh[] doorMeshes = itemLoader.load("models/door.obj", "textures/white.jpg");
		Item door = new Item(doorMeshes);
		door.setName("Door");
		listItems.add(door);
	}
	
	public void bookshelf() {
		Mesh[] bookshelfMeshes = itemLoader.load("models/BookShelf.obj", "textures/BookShelf.png");
		Item bookshelf = new Item(bookshelfMeshes);
		bookshelf.setName("Book Shelf");
		listItems.add(bookshelf);
	}
	
	public void bed() {
		Mesh[] bedMeshes = itemLoader.load("models/Bed.obj", "textures/Bed.png");
		Item bed = new Item(bedMeshes);
		bed.setName("Bed");
		listItems.add(bed);
	}
	public void closet() {
		Mesh[] closetMeshes = itemLoader.load("models/Closet.obj", "textures/Closet.png");
		Item closet = new Item(closetMeshes);
		closet.setName("Closet");
		listItems.add(closet);
	}
}
