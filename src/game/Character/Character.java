package game.Character;

import static GameGraphic.CoordsAndData.*;

import org.joml.Vector3f;

import GameText.MainText;
import GameText.Objects;
import game.Camera.Camera;

/**
 * Update movement of Character
 *
 */
public class Character {
	
	private Camera camera;
	protected Objects characterData;
	private MainText gameText;
	private int state = CHAR_STAND;
	private int preState = CHAR_STAND;
	private int step;
	private int counter;
	private boolean isDoing;
	private float height;
	private boolean worldViewMode = false;
	
	public Character(Objects characterData, MainText gameText) {
		this.characterData = characterData;
		this.gameText = gameText;
		this.isDoing = false;
		this.height = 0.8f;
	}
	
	public Character() {}
	
	public void addCamera(Camera camera) {
		this.camera = camera;
	}
	
	public Objects getData() {
		return this.characterData;
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public int getState() {
		return this.state;
	}
	
	
	public void setState(int state) {
		if (this.state != state) {
			this.preState = this.state;
			this.state = state;
		}
	}
	
 	
	public void restoreState() {
		this.state = this.preState;
	}
	
	public void setStep(int step) {
		this.step = step;
	}
	
	public int getStep() {
		return this.step;
	}
	
	public float getHeight() {
		return this.height;
	}
	
	public void setHeight(float h) {
		this.height = h;
	}
	
	public boolean isDoing() {
		return this.isDoing;
	}
	
	public void setProcess(boolean p) {
		this.isDoing = p;
	}
	
	public void setWorldViewMode(boolean w) {
		this.worldViewMode = w;
	}
	
	public boolean getWorldViewMode() {
		return this.worldViewMode;
	}
	
	public int getCounter() {
		return this.counter;
	}
	
	public void setCounter(int count) {
		this.counter = count;
	}
	/**
	 * offSet method
	 * Convert the distance from Third View to distance from Camera's view (Character) 
	 * @param distance
	 * @return Vector3f 
	 */
	public Vector3f offSet(Vector3f distance) {
		// Return the vector distance
		float diffX = (float)Math.sin(Math.toRadians(camera.getRotation().y)) * distance.z 
				+ (float)Math.sin(Math.toRadians(camera.getRotation().y - 90)) * distance.x;
		float diffZ = (float)Math.cos(Math.toRadians(camera.getRotation().y)) * distance.z 
				+ (float)Math.cos(Math.toRadians(camera.getRotation().y - 90)) * distance.x;
		float diffY = distance.y;
		Vector3f offSet = new Vector3f(diffX, diffY, diffZ);
		return offSet;
	}
	
	/**
	 * Update the range for character in 3D Plane (X and Z are updated)
	 * @param character
	 */
	public void updateRangeXZ() {
		int[] rangeRow = characterData.getrangeRow();
		int[] rangeCol = characterData.getrangeCol();
		float[] rangeX = {xPointOnAD*rangeRow[0], xPointOnAD*rangeRow[1]};
		float[] rangeZ = {zPointOnAB*rangeCol[0], zPointOnAB*rangeCol[1]};
		characterData.setrangeX(rangeX);
		characterData.setrangeZ(rangeZ);
	}
	
	/**
	 * getNextPosition method
	 * @param distance
	 * @return next position of character (int Row and Column)
	 */
	public int[] getNextPosition(Vector3f distance) {
		Vector3f currentPos = characterData.getCurrentPos();
		//Get the next rows and column Character will move
		Vector3f offSet = offSet(new Vector3f(distance));
		int nextRow = (int) Math.floor((currentPos.x + offSet.x)/xPointOnAD) + numRow/2; // Round Down to the nearest Row
		int nextCol = (int) Math.floor((currentPos.z + offSet.z)/zPointOnAB) + numCol/2; // Round Down to the nearest Column
		int[] nextPos =  {nextRow, nextCol};
		return nextPos;
	}
	
	
	/**
	 * moveCharacter method
	 * Check if the new position can move and then move the camera
	 * @param direction
	 * @param newPosition
	 */
	public void move(int[] newPosition, Vector3f distance) {
		// Move character and Camera 
		int newRow  = newPosition[0];
		int newCol  = newPosition[1];
		if(state == CHAR_STAND && gameText.moveNewPosition(newPosition)) {
			// Update Character's position in Text-version
			characterData.setRow(newRow);
			characterData.setCol(newCol);
			camera.movePosition(distance.x, distance.y, distance.z);
		}
	}
}
