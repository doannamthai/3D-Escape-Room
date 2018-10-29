package GameText;

import java.util.ArrayList;
// Class objects with methods to return the characteristics of any object used
import org.joml.Vector3f;
public class Objects {
	private String objectName;
	private String objectChar;
	private int objectRow;
	private int objectCol;
	private int objectWidth;
	private int objectLength;
	private float objectHeight;
	private ArrayList<Objects> bag = new ArrayList<Objects>();
	private int[] rangeRow;
	private int[] rangeCol;
	private float[] rangeX;
	private float[] rangeZ;
	private boolean isFunctional;
	private Vector3f currentPos = new Vector3f();
	public void setupData(String lineData) {
		// set up data from given line
		String[] splitedData = lineData.split("\\s");
		this.objectName = splitedData[0];
		this.objectChar = splitedData[1];
		this.objectRow = Integer.valueOf(splitedData[2]);
		this.objectCol = Integer.valueOf(splitedData[3]);
		this.objectWidth = Integer.valueOf(splitedData[4]);
		this.objectLength = Integer.valueOf(splitedData[5]);
		this.objectHeight = Float.valueOf(splitedData[6]);
		this.isFunctional = Boolean.valueOf(splitedData[7]);
	}
	public boolean putinBag(Objects objectTaken) {
		boolean aboveOrbelow = (this.objectCol == objectTaken.objectCol && Math.abs(this.objectRow - objectTaken.objectRow) == 1);
		boolean rightOrleft  = (this.objectRow == objectTaken.objectRow && Math.abs(this.objectCol - objectTaken.objectCol) == 1);
		if ((aboveOrbelow || rightOrleft) && objectTaken.isFunctional) {
			this.bag.add(objectTaken);
			objectTaken.objectCol = this.objectCol;
			objectTaken.objectRow = this.objectRow;
			return true;
		}
		return false;
	}

	public String getName() {
		return this.objectName;
	}

	public String getChar() {
		return this.objectChar;
	}

	public int getRow() {
		return this.objectRow;
	}

	public int getCol() {
		return this.objectCol;
	}

	public int getWidth() {
		return this.objectWidth;
	}

	public int getLength() {
		return this.objectLength;
	}
	public float getHeight() {
		return this.objectHeight;
	}
	public void setRow(int newRow) {
		this.objectRow = newRow;
	}

	public void setCol(int newCol) {
		this.objectCol = newCol;
	}

	public void moveObject(int numRow, int numCol) {
		this.objectRow += numRow;
		this.objectCol += numCol;
	}

	public void setrangeRow(int[] row) {
		this.rangeRow = row;
	}

	public void setrangeCol(int[] col) {
		this.rangeCol = col;
	}
	public void setrangeX(float[] x) {
		this.rangeX = x;
	}
	public void setrangeZ(float[] z) {
		this.rangeZ = z;
	}
	public int[] getrangeRow() {
		return this.rangeRow;
	}

	public int[] getrangeCol() {
		return this.rangeCol;
	}
	public float[] getrangeX() {
		return this.rangeX;
	}
	public float[] getrangeZ() {
		return this.rangeZ;
	}
	public ArrayList<Objects> getBag(){
		return this.bag;
	}
	public Vector3f getCurrentPos() {
		return this.currentPos;
	}
	public void setCurrentPos(Vector3f pos) {
		currentPos.x = pos.x;
		currentPos.y = pos.y;
		currentPos.z = pos.z;
	}
}
