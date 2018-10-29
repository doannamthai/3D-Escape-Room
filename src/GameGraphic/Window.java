package GameGraphic;

import static GameGraphic.CoordsAndData.HEIGHT;
import static GameGraphic.CoordsAndData.WIDTH;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Window {
	/** 
	 * Initialize the window
	 **/
	private long window;
	
	public void init(long window) {
		GLFWErrorCallback.createPrint(System.err).set();
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");
		// Configure GLFW
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);            // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);          // the window will not be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);       // set the context version
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);

		
		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "Escape Room", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
      	glfwSetWindowPos(window,(vidmode.width() - WIDTH) / 2,(vidmode.height() - HEIGHT) / 2);
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);
		// Make the window visible
		glfwShowWindow(window);
		this.window = window;
	}
	
	public long getWindow() {
		return this.window;
	}
	
	/**
	 * getProjectionMatrix method
	 * 3D screen
	 * @return Matrix of projection view
	 */
	public static Matrix4f getProjectionMatrix() {
		final float FOV = (float) 45.0f;
		final float Z_NEAR = 0.01f;
		final float Z_FAR = 1000f;
		float aspectRatio = (float) WIDTH / HEIGHT;
		Matrix4f projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
		return projectionMatrix;
	}
}
