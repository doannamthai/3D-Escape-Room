package GameText;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
public class GameTest {

	@Test
	public void test_CharacterName() {
		MainText game = new MainText();
		game.initGame();
		ArrayList<Floors> floorsArray = game.listFloors;        
		for (int floorNum = 0; floorNum < floorsArray.size(); floorNum++) {
      	  Floors currentFloor = floorsArray.get(floorNum);
      	  Objects character = currentFloor.getObjects().get(0);
      	  Assert.assertEquals("Character", character.getName());
        }
	}
	
	@Test
	public void test_CharacterMovementRange() {
		MainText game = new MainText();
		game.initGame();
		ArrayList<Floors> floorsArray = game.listFloors;        
		for (int floorNum = 0; floorNum < floorsArray.size(); floorNum++) {
      	  Floors currentFloor = floorsArray.get(floorNum);
      	  Objects character = currentFloor.getObjects().get(0);
      	  game.updateRange(currentFloor, character);
      	  Assert.assertArrayEquals(new int[]{0,15}, character.getrangeRow());
        }
	}
	
	@Test
	public void test_GameOver() {
		MainText game = new MainText();
		game.initGame();
		ArrayList<Floors> floorsArray = game.listFloors;        
		for (int floorNum = 0; floorNum < floorsArray.size(); floorNum++) {
      	  Floors currentFloor = floorsArray.get(floorNum);
      	  Objects character = currentFloor.getObjects().get(0);
      	  character.setCol(15);
      	  character.setRow(9);
      	  Assert.assertTrue(game.isOver(currentFloor));
        }
	}
}
