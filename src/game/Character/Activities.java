package game.Character;

import GameText.Objects;

public class Activities{
	
	//private Objects characterData;
	private static final int[] sitRange = {0,15};
	
	private static final int[][] turnOnComputer = new int[][] {{3,6},{3,7},{4,6}};
	
	protected static boolean isInExitRange(Objects characterData) {
		int currentRow = characterData.getRow();
		int currentCol = characterData.getCol();
		if (sitRange[0] == currentRow && sitRange[1] == currentCol) {
				return true;
			}
		return false;
	}
	
	protected static boolean canTurnOnComputer(Objects characterData) {
		int currentRow = characterData.getRow();
		int currentCol = characterData.getCol();
		for (int i = 0; i < turnOnComputer.length; i++) {
			if (turnOnComputer[i][0] == currentRow && turnOnComputer[i][1] == currentCol) {
				return true;
			}
		}
		return false;
	}
	
	
}
