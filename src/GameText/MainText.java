package GameText;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

public class MainText {
	private final String availableMove = "awsdAWSD"; // Possible inputs
	private final char pickUpControl = '@';
	private final String[] listFile = {"/text_data/ObjectsData1.txt"};
	public final static int boardCol = 16, boardRow = 16; // Number of Columns and row
	public static String[][] boardGame;
	public ArrayList<Floors> listFloors;

	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		MainText game = new MainText();
		game.initGame();
		// Iterates through objects and adds them to the board
		for (int floorNum = 0; floorNum < game.listFloors.size(); floorNum++) {
			Floors currentFloor = game.listFloors.get(floorNum);
			ArrayList<Objects> objectsList = currentFloor.getObjects();
			game.displayObject(objectsList);
			Objects character = objectsList.get(0);
			game.updateRange(currentFloor, character);
			System.out.println("Welcome to the Escape Room. \nTo move the character, please type down the direction in A S D and W + number of steps (s2: down 2 units).\nFor picking up any objects: @+Object symbol");
			game.displayBoard(boardGame);
			// Main loop where the game runs until the key is picked up and the character is at the door
			while (true) {
				boolean shouldEndGame = game.mainGame(currentFloor, boardGame);
				System.out.println("RangeROw: " + Arrays.toString(character.getrangeRow()) + ", RangeCOl" + Arrays.toString(character.getrangeCol()));
				if (shouldEndGame) {
					break;
				}
				boardGame = game.displayCharacter(currentFloor, boardGame);
				game.displayBoard(boardGame);
			}
			System.out.println("Congratulations! You have passed this door");
		}
	}
	public void initGame() {
		boardGame = this.setupBoard();		
		listFloors = this.setupFloors(this, this.listFile);
	}
	public ArrayList<Floors> setupFloors(MainText obj, String[] listFile) {
		ArrayList<Floors> listFloors = new ArrayList<Floors>();
		for (int a = 0; a < listFile.length; a++) {
			ArrayList<Objects> objectsData = new ArrayList<Objects>();
			Floors floor = new Floors();
			objectsData = obj.readFile(listFile[a]); // Reads file with object characteristics
			floor.addObjects(objectsData);           // Add objects list to the current floor
			floor.setName("Floor"+(a+1));            // Set name for the current floor
			listFloors.add(floor);                   // Add current floor to the array
		}
		return listFloors;
	}
    // File reader with error handling
	public ArrayList<Objects> readFile(String fileName) {
		BufferedReader content = null;
		ArrayList<Objects> objectData = new ArrayList<Objects>();
		try {
			InputStream in = Class.class.getResourceAsStream(fileName);
			InputStreamReader theFile = new InputStreamReader(in);
			content = new BufferedReader(theFile);
			String currentLine;
			while ((currentLine = content.readLine()) != null) {
				Objects object = new Objects();
				object.setupData(currentLine);
				objectData.add(object);
			}
			theFile.close();
		} catch (IOException e) {
			System.out.println("Cannot read the file");
		}
		return objectData;
	}

	@SuppressWarnings("resource")
	// Handles character movement as well as ensuring legal moves
	// This includes the frame and moving through objects
	public String getKeyboard() {
		// Get user's input and return the move
		while (true) {
			Scanner keyboard = new Scanner(System.in);
			System.out.println("Your move: ");
			String move = keyboard.nextLine();
			if (move.length() == 2) {
				boolean isGoodMove = this.availableMove.indexOf(move.charAt(0)) != -1 && "0123456789".indexOf(move.charAt(1)) != -1;
				boolean isPickUp   = this.pickUpControl == move.charAt(0) && "0123456789".indexOf(move.charAt(1)) == -1;
				if (isGoodMove || isPickUp) {
					char userMove1 = move.toUpperCase().charAt(0);
					char userMove2 = move.toUpperCase().charAt(1);
					return ""+userMove1+userMove2;
				}
				else {
					System.out.println("Your move is not available.");					
				}
			} else {
				System.out.println("Your move is not available.");
			}
		}
	}
// Sets up the board array in order to be displayed
	public String[][] setupBoard() {
		// Creating an array to save all position of objects
		String[][] boardArray;
		boardArray = new String[this.boardRow][this.boardCol];
		for (int i = 0; i < this.boardRow; i++) {
			for (int k = 0; k < this.boardCol; k++) {
				boardArray[i][k] = "-";
			}
		}
		return boardArray;
	}
    // Displays the board based on the array
	public void displayBoard(String[][] boardArray) {
		// Display the board at terminal
		for (int i = 0; i < this.boardRow; i++) {
			String rowLine = String.join("   ", boardArray[i]);
			System.out.println(rowLine);
		}
	}
    // Displays the objects onto the board
	public void displayObject(ArrayList<Objects> listObjects) {
		for (int numObj = 0; numObj < listObjects.size(); numObj++) {
			Objects currentObject = listObjects.get(numObj);
			String name = currentObject.getName();
			String symbol = currentObject.getChar();
			int currentRow = currentObject.getRow();
			int currentCol = currentObject.getCol();
			int width = currentObject.getWidth();
			int length = currentObject.getLength();
			if ((width == length) & width == 1) {
				boardGame[currentRow][currentCol] = symbol;
				continue;
			}
			// Draw rows and fill columns
			for (int i = 0; i < width; i++) {
				if ((currentRow + i) < boardRow && boardGame[currentRow + i][currentCol] == "-") {
					// Fill this row with (length - 1) numbers of its symbol
					for (int a = 1; a < length; a++) {
						boardGame[currentRow + i][currentCol + a] = symbol;
					}
					boardGame[currentRow + i][currentCol] = symbol;
				} else {
					System.out.println("Error: Object: " + name + " is out of index (ROW)");
				}
			}
			
		}
	}
	// Adds the character into the board
	public String[][] displayCharacter(Floors floorData, String[][] board) {
		Objects Character = floorData.getObjects().get(0);
		int currentRow    = Character.getRow();
		int currentCol    = Character.getCol();
		String symbol     = Character.getChar();
		board[currentRow][currentCol] = symbol;
		return board;
	}
	// [New] for 3D Graphic Version
	// Check if Character can move to the new position
	public boolean moveNewPosition(int[] newPosition) {
		int newRow = newPosition[0];
		int newCol = newPosition[1];
		boolean inIndex = (newRow >= 0 && newRow < boardRow) && (newCol >= 0 && newCol < boardCol);
		boolean notObstacle = false;
		if (inIndex) {
			notObstacle = boardGame[newRow][newCol].equals("-") ||  boardGame[newRow][newCol].equals("*");
		}
		return (inIndex && notObstacle);
	}
	// Ensures the character is within bounds
	public void updateRange(Floors floorData, Objects currentObject) {
		String[][] board = boardGame;
		Objects Door  = floorData.getObjects().get(1);
		int currentRow = currentObject.getRow();
		int currentCol = currentObject.getCol();
		int rightBound = currentCol;
		int leftBound = currentCol;
		int upperBound = currentRow;
		int lowerBound = currentRow;
		for (int a = currentCol + 1; a < this.boardCol; a++) {
			String currentBound = board[currentRow][a];
			if (!currentBound.equals("-") && !currentBound.equals(Door.getChar())) {
				break;
			}
			rightBound = a;
		}
		for (int b = currentCol - 1; b >= 0; b--) {
			String currentBound = board[currentRow][b];
			if (!currentBound.equals("-") && !currentBound.equals(Door.getChar())) {
				break;
			}
			leftBound = b;
		}
		for (int c = currentRow + 1; c < this.boardRow; c++) {
			String currentBound = board[c][currentCol];
			if (!currentBound.equals("-") && !currentBound.equals(Door.getChar())) {
				break;
			}
			lowerBound = c;
		}
		for (int d = currentRow - 1; d >= 0; d--) {
			String currentBound = board[d][currentCol];
			if (!currentBound.equals("-") && !currentBound.equals(Door.getChar())) {
				break;
			}
			upperBound = d;
		}

		int rangeCol[] = { leftBound, rightBound };
		int rangeRow[] = { upperBound, lowerBound };
		currentObject.setrangeRow(rangeRow);
		currentObject.setrangeCol(rangeCol);
	}
// Checks if the character is at the door and the game ends
	public boolean doneTask(Floors floorData) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		boolean doneTask    = floorData.runMethods(floorData.getName());
		return doneTask;
	}
	public boolean isAtDoor(Floors floorData) {
		ArrayList<Objects> floorObjects = floorData.getObjects();
		Objects Character   = floorObjects.get(0); // Main Character
		Objects Door        = floorObjects.get(1); // The Door
		boolean aboveOrbelow = (Character.getCol() == Door.getCol() && Math.abs(Character.getRow() - Door.getRow()) == 1);
		boolean rightOrleft  = (Character.getRow() == Door.getRow() && Math.abs(Character.getCol() - Door.getCol()) == 1);
		return aboveOrbelow || rightOrleft;
	}
	
	public boolean isOver(Floors floorData) {
		boolean isAtDoor = isAtDoor(floorData);
		return isAtDoor;
	}
		
	// Processes the inputs and updates values
	public boolean mainGame(Floors floorData, String[][] board) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException { 
		int numMove = 0;
		Objects Character = floorData.getObjects().get(0);
		Objects Door      = floorData.getObjects().get(1);
		ArrayList<Objects> objectsList = floorData.getObjects();
		int currentRow = Character.getRow();
		int currentCol = Character.getCol();
		int[] rangeRow = Character.getrangeRow();
		int[] rangeCol = Character.getrangeCol();
		String control = getKeyboard();
		if (control.charAt(0) != this.pickUpControl) {
			numMove = control.charAt(1) -'0';
		}
		switch (control.charAt(0)) {
		case 'A':
			if (currentCol - numMove >= rangeCol[0]) {
				if (currentCol - numMove == Door.getCol() && currentRow == Door.getRow()) {
					// If Character is moving to the door
					if (doneTask(floorData)) { // If task is finished
						return true;
					}
					else {
						System.out.println("You cannot pass this door");
					}
				}
				else {
					board[currentRow][currentCol] = "-";
					Character.setCol(currentCol - numMove);
					updateRange(floorData, Character);
				}
			} else {
				System.out.println("This character cannot move to the left " + numMove + " units.");
			}
			break;
		case 'S':
			if (currentRow + numMove <= rangeRow[1]) {
				if (currentRow + numMove == Door.getRow() && currentCol == Door.getCol()) {
					// If Character is moving to the door
					if (doneTask(floorData)) { // If task is finished
						return true;
					}
					else {
						System.out.println("You cannot pass this door");
					}
				}
				else {
					board[currentRow][currentCol] = "-";
					Character.setRow(currentRow + numMove);
					updateRange(floorData, Character);
				}
			} else {
				System.out.println("This character cannot move down " + numMove + " units.");
			}
			break;
		case 'D':
			if (currentCol + numMove <= rangeCol[1]) {
				if (currentCol + numMove == Door.getCol() && currentRow == Door.getRow()) {
					// If Character is moving to the door
					if (doneTask(floorData)) { // If task is finished
						return true;
					}
					else {
						System.out.println("You cannot pass this door");
					}
				}
				else {
					board[currentRow][currentCol] = "-";
					Character.setCol(currentCol + numMove);
					updateRange(floorData, Character);
				}
			} else {
				System.out.println("This character cannot move to the right " + numMove + " units.");
			}
			break;
		case 'W':
			if (currentRow - numMove >= rangeRow[0]) {
				if (currentRow - numMove == Door.getRow() && currentCol == Door.getCol()) {
					// If Character is moving to the door
					if (doneTask(floorData)) { // If task is finished
						return true;
					}
					else {
						System.out.println("You cannot pass this door");
					}
				}
				else {
					board[currentRow][currentCol] = "-";
					Character.setRow(currentRow - numMove);
					updateRange(floorData, Character);
				}
			} else {
				System.out.println("This character cannot move up " + numMove + " units.");
			}
			break;
		case '@':
			for (int i = 0; i < objectsList.size(); i++) {
				char currentChar = objectsList.get(i).getChar().charAt(0);
				if(currentChar == control.charAt(1)) {
					int oldRow = objectsList.get(i).getRow();
					int oldCol = objectsList.get(i).getCol();
					boolean canPutinBag = Character.putinBag(objectsList.get(i));
					if (canPutinBag) {
						board[oldRow][oldCol] = "-";
						updateRange(floorData, Character);
						break;
					}
					else {
						System.out.println("The "+objectsList.get(i).getName()+" is too far from main character or you cannot take this.");
					}
				}
			}
			break;			
		}
		return false;
	}
	
	public String[] getListFile() {
		return this.listFile;
	}
}
