package GameGraphic;


import org.joml.Vector2d;
import org.joml.Vector3f;

import GameText.MainText;
import static org.lwjgl.glfw.GLFW.*;


public class CoordsAndData {
	public static final int numRow = MainText.boardRow, numCol = MainText.boardCol;
	public static final int HEIGHT = 920, WIDTH = 1600;
	public static final float WALL_HEIGHT = 1.5f;	
	public static float VELOCITY = 1/7f;
	public static final float ROTATE_VELOCITY = 1/15f;
	public static final float SPECULAR_POWER = 1f;
	public static final Vector3f A = new Vector3f(-2.0f, 0.0f, 2.0f);
	public static final Vector3f D = new Vector3f(2.0f, 0.0f, 2.0f);
	public static final Vector3f C = new Vector3f(2.0f, 0.0f, -2.0f);
	public static final Vector3f B = new Vector3f(-2.0f, 0.0f, -2.0f);
	public static final Vector3f M = new Vector3f(-2.0f, WALL_HEIGHT, 2.0f);
	public static final Vector3f N = new Vector3f(-2.0f, WALL_HEIGHT, -2.0f);
	public static final Vector3f P = new Vector3f(2.0f, WALL_HEIGHT, -2.0f);
	public static final Vector3f Q = new Vector3f(2.0f, WALL_HEIGHT, 2.0f);
	public static final float zAB = B.z - A.z;
	public static final float xAD = D.x - A.x;
	public static final float xPointOnAD = xAD/numRow;
	public static final float zPointOnAB = zAB/numCol;
	public static final int FPS = 60;
	public static final float[] turnOnButton = {0.8f, 0.8f, 100, 100};
	public static final int CHAR_STAND = 0;
	public static final int CHAR_SIT = 1;
	public static final int CHAR_ON_COMPUTER = 2;
	public static boolean GAME_OVER = false;
}
