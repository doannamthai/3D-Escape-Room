package game.EventHandler;

import static GameGraphic.CoordsAndData.VELOCITY;
import static GameGraphic.CoordsAndData.xPointOnAD;
import static GameGraphic.CoordsAndData.zPointOnAB;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWKeyCallback;

import game.Camera.Camera;
import game.Character.Character;

public class KeyboardInput extends InEventData {
	
	public KeyboardInput(long window) {
		super(window);
	}
	
	/**
	 * keyboardMove method
	 * Get input from user (arrow keys) 
	 * Update the camera's view
	 */
	public void updateKeyboard(Camera camera, Character character) {
		GLFWKeyCallback keyCallback;
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
        	@Override
        	public void invoke(long window, int key, int scancode, int action, int mods) {
        		if (key == GLFW_KEY_DOWN && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
        			// If the key is press and hold
        			Vector3f distance = new Vector3f(0.0f, 0.0f, -zPointOnAB*VELOCITY);  // Moving in Z distance
        			int[] nextPos = character.getNextPosition(distance);                 // Calculate the next position of Character (in Row and Column)
        			character.move(nextPos, distance);                                   // Move Character
        		}
        		else if (key == GLFW_KEY_UP && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
        			Vector3f distance = new Vector3f(0.0f, 0.0f, zPointOnAB*VELOCITY);
        			int[] nextPos = character.getNextPosition(distance);
        			character.move(nextPos, distance);
        		}
        		else if (key == GLFW_KEY_LEFT && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
        			Vector3f distance = new Vector3f(xPointOnAD*VELOCITY, 0.0f, 0.0f);
        			int[] nextPos = character.getNextPosition(distance);
        			character.move(nextPos, distance);
        		}
        		else if (key == GLFW_KEY_RIGHT && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
        			Vector3f distance = new Vector3f(-xPointOnAD*VELOCITY, 0.0f, 0.0f);
        			int[] nextPos = character.getNextPosition(distance);
        			character.move(nextPos, distance);
        		}
        	}
        });
	}
	

}
