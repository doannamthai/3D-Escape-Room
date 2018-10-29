package game.EventHandler;

import org.joml.Vector2d;

import game.Camera.Camera;
import game.Character.Character;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public abstract class InEventData {
	protected long window = 0;
	protected static int LEFT_MOUSE_PRESTATE = GLFW_RELEASE;
	protected static int LEFT_MOUSE_STATE = GLFW_RELEASE;
	protected static int COPIED_STATE = LEFT_MOUSE_STATE;
	protected static Vector2d MOUSE_POS = new Vector2d(-1.0, -1.0);
	
	public InEventData(long window) {
		this.window = window;
	}
}

