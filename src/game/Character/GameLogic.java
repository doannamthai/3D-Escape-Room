package game.Character;

import static org.lwjgl.glfw.GLFW.*;
import java.util.List;

import org.joml.Vector2d;
import org.joml.Vector3f;
import static GameGraphic.CoordsAndData.*;
import static game.Toolbox.Transformation.*;
import game.EventHandler.KeyboardInput;
import game.EventHandler.MouseInput;
import game.GUI.Screen2D;
import game.Items.Item;

public class GameLogic extends Activities{	
	private int sitKey = GLFW_KEY_S;
	private int standKey = GLFW_KEY_W;
	private long window;
	private Screen2D screen;
	private List<Item> listGameItems;
	
	private int timeMovingCloset;
	private boolean closetDoneMoving = false;
	private boolean closetCanMove = false;
	private final float offSet = 0.005f;
	private final float movingScale;
	
	public GameLogic(List<Item> listGameItems, long window) {
		movingScale = Math.abs((zPointOnAB));
		this.window = window;
		this.listGameItems = listGameItems;
	}
	
	public void update(Character character, MouseInput mouse, KeyboardInput keyboard) {
		sitDown(character);
		standUp(character);
		openExit(character);
	}
	
	private void openExit(Character character) {
		if (canOpenExit(character)) {
			closetCanMove = true;
		}
	}
	/**
	 * Update Item position (translation and rotation)
	 * @param item
	 */
	
	public void updateItemsPosition(Item item) {
		if (closetCanMove && item.getName().equals("Closet")) {
			item.translate(new Vector3f(0f, 0f, -offSet));
			if (3*movingScale/offSet == timeMovingCloset) {
				closetDoneMoving = true;
				closetCanMove = false;
				timeMovingCloset = 0;
				GAME_OVER = true;
			}
			else {
				timeMovingCloset++;
			}
		}
	}

	
	private boolean canOpenExit(Character character) {
		if (isInExitRange(character.getData()) && !closetDoneMoving) {
			if (character.getState() == CHAR_SIT) {
				character.setCounter(character.getCounter() + 1);
			}
			if (character.getCounter() == FPS*3) {
				character.setCounter(0);
				return true;
			}
		}
		return false;
	}
	
	private void turnOnComputer(Character character, Screen2D screen2D, MouseInput mouse) {
		if (canTurnOnComputer(character.getData())) {
			screen2D.getItem().turnOnButton();
			Vector2d MOUSE_POS = mouse.getCursorPos();
			int LEFT_MOUSE_STATE = mouse.getLeftMouseState();
			int MOUSE_PREVIOUS_STATE = mouse.getLeftMousePreState();
			//System.out.println("Current state: " + LEFT_MOUSE_STATE + " preState: " + MOUSE_PREVIOUS_STATE);
			boolean inRangeX =  transformX(turnOnButton[0]) <= MOUSE_POS.x && MOUSE_POS.x <= transformX(turnOnButton[0]) + turnOnButton[2]; 
			boolean inRangeY =  transformY(turnOnButton[1]) <=  MOUSE_POS.y &&  MOUSE_POS.y <= transformY(turnOnButton[1]) + turnOnButton[3]; 
			
			if (character.getState() == CHAR_ON_COMPUTER) {
				screen2D.getItem().computerScreen();
			}
			else if ((inRangeX && inRangeY) && (LEFT_MOUSE_STATE == GLFW_PRESS) && (MOUSE_PREVIOUS_STATE == GLFW_RELEASE) && character.getState() != CHAR_ON_COMPUTER){
				character.setState(CHAR_ON_COMPUTER);
			}
			if ((inRangeX && inRangeY) && (LEFT_MOUSE_STATE == GLFW_PRESS ) && MOUSE_PREVIOUS_STATE == GLFW_RELEASE && character.getState() == CHAR_ON_COMPUTER) {
				character.restoreState();
			}	
		}
	}
	/**
	 * Character's siting command
	 * @param window
	 * @param character
	 */
	
	private void sitDown(Character character) {
		if (character.getState() == CHAR_STAND && character.getStep() == 10 ) {   // If Character sat at desired position
			character.setStep(0); 
			character.setProcess(false);
			character.setState(CHAR_SIT);      // Done sitting
		}
		else if ((glfwGetKey(window, sitKey) == GLFW_PRESS || character.isDoing()) && character.getState() == CHAR_STAND) {
			character.setProcess(true);                               // Update state, prevent Character from doing anything
			character.getCamera().movePosition(0.0f, - character.getHeight()/3, 0.0f);
			character.setStep(character.getStep() + 1);
		}
	}
	/**
	 * Character's standing command
	 * @param window
	 * @param character
	 */
	private void standUp(Character character) {
		if (character.getState() == CHAR_SIT && character.getStep() == 10 ) { 
			character.setState(CHAR_STAND);       // Done
			character.setStep(0); 
			character.setProcess(false);
		}
		else if ((glfwGetKey(window, standKey) == GLFW_PRESS || character.isDoing()) && character.getState() == CHAR_SIT) {
			character.setProcess(true);                               // Update state, prevent Character from doing anything
			character.setStep(character.getStep() + 1);
			character.getCamera().movePosition(0.0f, character.getHeight()/3, 0.0f);
		}
	}

	
}
