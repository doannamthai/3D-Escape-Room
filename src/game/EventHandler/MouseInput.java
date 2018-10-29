package game.EventHandler;

import static GameGraphic.CoordsAndData.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFWCursorPosCallback;

import game.Camera.Camera;
import game.Character.Character;

public class MouseInput extends InEventData {
	
	public MouseInput(long window) {
		super(window);
	}
	
	
	/**
	 * mouseMove method
	 * Check position of the mouse in the current window
	 * Update the camera's view when mouse is moved
	 */
	public void updateMouse(Camera camera, Character character) {
		GLFWCursorPosCallback posCallback;
		LEFT_MOUSE_STATE  = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT);
		glfwSetCursorPosCallback(window, posCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				//Vector3f mousePos = mouse.getMouseRay(camera, (float) xpos, (float) ypos);
				if (LEFT_MOUSE_STATE  == GLFW_RELEASE) {					         // If mouse is released 
					MOUSE_POS.x = xpos;										 // Save the current position
					MOUSE_POS.y = ypos;
					if (LEFT_MOUSE_STATE != COPIED_STATE) {
						LEFT_MOUSE_PRESTATE = GLFW_PRESS;
					}
				}
				else if (LEFT_MOUSE_STATE == GLFW_PRESS || LEFT_MOUSE_STATE == GLFW_REPEAT) { // If mouse is pressed
					double diffX = xpos - MOUSE_POS.x;                       // The current position and the previous position of mouse
					double diffY = ypos - MOUSE_POS.y;
					camera.moveRotation((float) diffY*ROTATE_VELOCITY, 0.0f, 0.0f); // Rotate around X;
					camera.moveRotation(0.0f, (float) diffX*ROTATE_VELOCITY, 0.0f); // Rotate around Y;
					MOUSE_POS.x = xpos;                                     
					MOUSE_POS.y = ypos;
					if (LEFT_MOUSE_STATE != COPIED_STATE) {
						LEFT_MOUSE_PRESTATE = GLFW_RELEASE;
					}
				}
				COPIED_STATE = LEFT_MOUSE_STATE;
			}
		});
	}
	
	public int getLeftMouseState() {
		return LEFT_MOUSE_STATE;
	}
	
	
	public Vector2d getCursorPos() {
		return MOUSE_POS;
	}
	
	public int getLeftMousePreState() {
		return LEFT_MOUSE_PRESTATE;
	}
}
