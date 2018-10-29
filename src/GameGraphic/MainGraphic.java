package GameGraphic;

/** Escape Game -  3D Graphic
 * @author: ThaiDoan
 * @CPSC233
 * @library: LWJGL3 (OPENGL, GLFW, JOML)
 **/

import org.lwjgl.opengl.*;

import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static GameGraphic.CoordsAndData.*;
import GameText.Floors;
import GameText.MainText;
import GameText.Objects;
import game.Camera.Camera;
import game.Character.Character;
import game.Character.GameLogic;
import game.Engine.LightRenderer;
import game.Engine.Loader;
import game.Engine.Renderer;
import game.EventHandler.KeyboardInput;
import game.EventHandler.MouseInput;
import game.GUI.Screen2D;
import game.Items.Item;
import game.Items.Mesh;
import game.Items.SkyBox;
import game.Items.GameModels;
import game.Light.PointLight;
import game.Shaders.ShaderProgram;
import game.Toolbox.Transformation;

import org.joml.*;
public class MainGraphic {
	private static long window;
	private Camera camera;
	private static ArrayList<Floors> floorsArray;
	private static List<Item> listGameItems = new ArrayList<Item>();
	private static MainText gameText;
	private Character character;
	private static Vector3f ambientLight = new Vector3f();
	private static PointLight[] pointLightList;
	private ShaderProgram sceneShader;
	private ShaderProgram skyboxShader;
	private SkyBox skyBox;
	private Screen2D screen2D;
	private MouseInput mouseEvent;
	private KeyboardInput keyboardEvent;
    private float fps;
	private double lastFPS;
	private Loader loader;

	public static void main(String[] args){
          MainGraphic game = new MainGraphic();
          gameText = new MainText();                          // Call the text-version
          gameText.initGame();                                // Read file, set up board in text-version
          floorsArray = gameText.listFloors;                  // Get the data of floors from text-version
          // Loop for accessing every floors
          for (int floorNum = 0; floorNum < floorsArray.size(); floorNum++) {
        	  Floors currentFloor = floorsArray.get(floorNum);
        	  ArrayList<Objects> objectsList = currentFloor.getObjects();
  			  Objects character = floorsArray.get(0).getObjects().get(0);
  			  gameText.displayObject(objectsList);
  			  game.setUpCharacter(character);
  	          game.run();
          }
	}

	public void run(){
		    initWindow();
		    initOpenGL();
		    initLoader();
			init3DsceneShader();
			initSkyBox();
			setUpCamera();
			setUpLight();
			setUpModels();
			setUpEventHandler();
			try {
				loop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Free the window callbacks and destroy the window
			glfwFreeCallbacks(window);
			glfwDestroyWindow(window);
			// Terminate GLFW and free the error callback
			glfwTerminate();
			glfwSetErrorCallback(null).free();
	}
	
	public void initLoader() {
		loader = new Loader();
	}

	public double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }

	public void updateFPS() {
        if (getTime() - lastFPS > 1) {
        	glfwSetWindowTitle(window, "FPS: " + fps);
        	fps = 0;
            lastFPS += 1;
        }
        fps++;
    }
	
	public void setUpCharacter(Objects characterData) {
		character = new Character(characterData, gameText);
	}
	
	/**
	 * setupCamera method
	 * Get character's position (in Row and Column) from text-version and transform to the 3D plan
	 */
	public void setUpCamera() {
		float initialAngle = -45f;
		int row = character.getData().getCol();
		int col = character.getData().getRow();
		float xPos = A.x + xPointOnAD*row;
		float zPos = A.z + zPointOnAB*col;
		//Vector3f position = new Vector3f(xPos, characterHeight, zPos);
		Vector3f position = new Vector3f(xPos, character.getHeight(), zPos);
		Vector3f rotation = new Vector3f(0.0f, initialAngle, 0.0f);
		//Set up position for camera
		camera = new Camera (position, rotation);
		character.addCamera(camera);
	}
	
	/**
	 * setUpLight method
	 * Set up the ambient light, light colour, position and intensity
	 */
	public void setUpLight() {
		ambientLight = new Vector3f(0.5f, 0.5f, 0.5f);
		Vector3f lightColour = new Vector3f(1f, 1f, 1f);
        float lightIntensity = 1f;
		// Point Light
        Vector3f lightPosition = new Vector3f(0f, 3f, 0f);
        PointLight pointLight1 = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(2f, 0.5f, 0.01f);
        pointLight1.setAttenuation(att);
        Vector3f lightPosition2 =  new Vector3f(0f, -3f, 0f);
        PointLight pointLight2 = new PointLight(lightColour, lightPosition2, lightIntensity - 0.5f);
        PointLight.Attenuation att2 = new PointLight.Attenuation(2f, 0.1f, 0.01f);
        pointLight2.setAttenuation(att2);
        pointLightList = new PointLight[]{pointLight1, pointLight2};
	}
	
	/**
     * setUpModels method
     * Get the list has all models type Model
     */
	public void setUpModels() {
		GameModels models = new GameModels(loader);
		listGameItems = models.getListGameItems();
	}
	
	public void setUpEventHandler() {
		mouseEvent = new MouseInput(window);
		keyboardEvent = new KeyboardInput(window);	
	}
	
	public void initSkyBox() {
		skyboxShader = new ShaderProgram();
		skyBox = new SkyBox("models/skybox.obj", "textures/skybox.png", loader);
		skyBox.scale(40f);
		skyBox.setAmbientLight(new Vector3f(1f, 1f, 1f));
		int vertexSkyboxShaderID = skyboxShader.loadShader("/game/Shaders/skybox_vertex.txt", GL20.GL_VERTEX_SHADER);
        int fragmentSkyboxShaderID  = skyboxShader.loadShader("/game/Shaders/skybox_fragment.txt", GL20.GL_FRAGMENT_SHADER);
        skyboxShader.setVertexShader(vertexSkyboxShaderID);
        skyboxShader.setFragmentShader(fragmentSkyboxShaderID);
        skyboxShader.link();
        skyboxShader.createUniform("projectionMatrix");
        skyboxShader.createUniform("modelViewMatrix");
        skyboxShader.createUniform("texture_sampler");
        skyboxShader.createUniform("ambientLight");
	}
	
	/** 
	 * Enable Depth test 
	 */
	public void initOpenGL() {
	    glEnable(GL_DEPTH_TEST);
	    glEnable(GL_STENCIL_TEST);
	    //glEnable(GL_CULL_FACE);
	    //glCullFace(GL_BACK);
	    glEnable(GL_BLEND);
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	/**
	 * Set up window
	 */
	private void initWindow() {
		Window win = new Window();
		win.init(window);
		window = win.getWindow();
		GL.createCapabilities();
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
	}
	
	/**
	 * init3DsceneShader method
	 * Set ID for vertex sceneShader and fragment sceneShader files
	 * Create uniforms
	 */
	public void init3DsceneShader() {
		sceneShader = new ShaderProgram();
		final int MAX_POINT_LIGHTS = 5;
        int vertexsceneShaderID = sceneShader.loadShader("/game/Shaders/vertex.vs", GL20.GL_VERTEX_SHADER);
        int fragmentsceneShaderID  = sceneShader.loadShader("/game/Shaders/fragment.fs", GL20.GL_FRAGMENT_SHADER);
        sceneShader.setVertexShader(vertexsceneShaderID);
        sceneShader.setFragmentShader(fragmentsceneShaderID);
        sceneShader.link();
        // Create uniforms for modelView and projection matrices and texture
        sceneShader.createUniform("modelViewMatrix");
        sceneShader.createUniform("projectionMatrix");
        sceneShader.createUniform("texture_sampler");
        // Create uniform for material
        sceneShader.createMaterialUniform("material");
        sceneShader.createUniform("normalMap");
        // Create lighting related uniforms
        sceneShader.createUniform("specularPower");
        sceneShader.createUniform("ambientLight");
        sceneShader.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
    }
	
	private void updateGameLogic(GameLogic game) {
		game.update(character, mouseEvent, keyboardEvent);
	}
	
	
	/**
	 * Update method
	 * Update character's range, get user's input (keyboard and mouse), update light
	 */
	public void update() {
		gameText.updateRange(floorsArray.get(0), character.getData()); // Get the current range of Character from Text version
        character.updateRangeXZ();                                     // Update to the 3D plane
        updateEvent(mouseEvent, keyboardEvent, camera, character);
        character.getData().setCurrentPos(camera.getPosition());       // Update character position base on the Camera
	}
	
	private void updateEvent(MouseInput mouse, KeyboardInput keyboard, Camera camera, Character character) {
			mouse.updateMouse(camera, character);
		    keyboard.updateKeyboard(camera, character);
	}
	
	/**
	 * enableCamera method
	 * Set the camera's matrix after transformation 
	 */
	public void enableCamera() {
		// Update matrix
		Matrix4f cameraViewMatrix = Transformation.getCameraViewMatrix(camera);
		// Save it
		camera.setCameraMatrix(cameraViewMatrix);
	}
		
	/**
	 * render method
	 * Enable camera and starting rendering models
	 * @param renderer
	 * @param projectionMatrix
	 */
	public void render3D(Renderer renderer, LightRenderer lightRenderer, Matrix4f projectionMatrix, GameLogic game){
		// Render Objects
		sceneShader.bind();
        lightRenderer.renderLights(camera.getCameraMatrix(), ambientLight, pointLightList);
        sceneShader.setUniform("projectionMatrix", projectionMatrix);
	    sceneShader.setUniform("texture_sampler", 0);
	    sceneShader.setUniform("normalMap", 1);
        for (Item model : listGameItems) {
    		sceneShader.setUniform("modelViewMatrix", Transformation.getModelViewMatrix(model, camera));
    		game.updateItemsPosition(model);
    		for (Mesh mesh : model.getMeshes()) {
        		sceneShader.setUniform("material", mesh.getMaterial());
                renderer.render(mesh);          
    		}
        }
        sceneShader.unbind();
	}
	
	public void render2D() {
		screen2D.getItem().toolBar();
		if (GAME_OVER) {
			screen2D.getItem().textGameOver();
		}
	}
	
	/**
	 * Render SkyBox
	 * @param renderer
	 * @param projectionMatrix
	 */
	public void renderSkyBox(Renderer renderer, Matrix4f projectionMatrix) {
		skyboxShader.bind();
        skyboxShader.setUniform("projectionMatrix", projectionMatrix);
        skyboxShader.setUniform("texture_sampler", 0);
        Camera copiedCamera = camera.getCopy();
        copiedCamera.getCameraMatrix().m30(0);   // The first element in the fourth row
        copiedCamera.getCameraMatrix().m31(0);   // The second element in the fourth row
        copiedCamera.getCameraMatrix().m32(0);   // The third element in the fourth row
		skyboxShader.setUniform("modelViewMatrix", Transformation.getModelViewMatrix(skyBox, copiedCamera));
		skyboxShader.setUniform("ambientLight", skyBox.getAmbientLight());
		renderer.render(skyBox.getMeshes()[0]);
		skyboxShader.unbind();
	}

	/**
	 * Game Loop
	 */
	private void loop() {
		Renderer renderer = new Renderer();
		LightRenderer lightRenderer = new LightRenderer(sceneShader, SPECULAR_POWER);
		Matrix4f projectionMatrix = Window.getProjectionMatrix();
		screen2D = new Screen2D();
		lastFPS = getTime();
		GameLogic gameLogic = new GameLogic(listGameItems, window);
		while (!glfwWindowShouldClose(window)) {
			glfwSwapBuffers(window); // It is ready to present what was rendered
			glClear(GL_COLOR_BUFFER_BIT| GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);		
			enableCamera();
			renderSkyBox(renderer, projectionMatrix);
			render3D(renderer, lightRenderer, projectionMatrix, gameLogic);
			render2D();
			glfwPollEvents();
			update();
			updateGameLogic(gameLogic);
			updateFPS();

		}
		sceneShader.cleanup();
		skyboxShader.cleanup();
		loader.cleanup();
	}
}
